package com.bishe.service.impl;

import com.bishe.controller.WebSocket.*;
import com.bishe.dao.*;
import com.bishe.enums.ActivityStatusEnum;
import com.bishe.enums.MessageEnum;
import com.bishe.pojo.*;
import com.bishe.service.AdminMessageService;
import com.bishe.service.MessageService;
import com.bishe.utils.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ZYL on 2019/3/18.
 */
@Service
@Transactional
public class MessageServiceImpl implements MessageService {
    Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);
    @Autowired
    MessageDao messageDao;
    @Autowired
    AdminMessageDao adminMessageDao;
    @Autowired
    AdminDao adminDao;
    @Autowired
    DataInfoDao dataInfoDao;
    @Autowired
    StudentMessageDao studentMessageDao;

    @Override
    public void addAMessage(Message message) {

        try {
            //向message表中添加一条数据
            messageDao.addAMessage(message);
            //查询出所有的管理员
            List<Admin> adminList = adminDao.selectAllAdmin();
            //根据管理员id给所有的管理员添加一条消息
            AdminMessage adminMessage = new AdminMessage();
            for (Admin admin : adminList) {
                //管理员id
                adminMessage.setAdminId(admin.getAdminId());
                //消息id
                adminMessage.setMessageId(message.getMessageId());
                //管理员消息的建立时间和message的时间一样
                adminMessage.setAdminMessageCreateTime(message.getMessageCreateTime());
                //管理员消息的Id
                adminMessage.setAdminMessageId(UUIDUtils.getId());
                //消息的初始状态：未读
                adminMessage.setAdminMessageStatus(0);
                //消息的活动是否是再审核0表示初次审核，1表示再审核，修改活动是再审核
                adminMessage.setAdminMessageDealAgain(0);
                //封装好数据之后插入数据库中
                adminMessageDao.addAMessageToAdmin(adminMessage);
            }
            log.info("开启所有管理员的线程");

            Iterator<Map.Entry<String, AdminThread>> iterator = AdminWebSocket.adminThreadMap.entrySet().iterator();
            while (iterator.hasNext()) {
                AdminThread adminThread = iterator.next().getValue();
                adminThread.run();
                try {
                    adminThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("所有的管理员的线程开启完成");
        } catch (Exception e) {
            log.info("messageServiceImpl出错了");
            e.getMessage();
        }
    }

    @Override
    public void addMessagesByChangeActivity(Activity activity) {
        //判断此次修改的活动是初审核还是再审核活动，如果无审核通过记录则为初审核，有审核通过记录则为再审核，删除管理员对应这个活动的消息，再给管理员添加新的消息，
        // 如果活动为再审核且有报名参加者则给所有的报名者一条消息，开启在线的管理线程和开启与活动有关的并且在线的学生线程
        //原则：一条活动对应一条message消息N个管理员的N条admin_message消息和N个student_message(学生的消息可以有多条)
        //根据活动id查询出message消息，然后根据message_id查询出管理员的admin_message
        //判断该活动是初审核还是再审核
        //更新message，保存活动的原状态，修改message_create_time时间
        try {
            String changeTime = activity.getActiChangeTime();

            Message message = messageDao.selectMessageByActiId(activity.getActiId());
            log.info("message信息" + message);
            AdminMessage adminMessage1 = adminMessageDao.selectAdminMessageByMessageId(message.getMessageId());
            log.info("adminMessage信息" + adminMessage1);
            //根据message_id删除管理员对应的这条消息
            adminMessageDao.deleteAdminMessageByMessageId(message.getMessageId());

            //查询出所有的管理员，给所有的管理员一条消息
            List<Admin> adminList = adminDao.selectAllAdmin();
            //根据管理员id给所有的管理员添加一条消息
            AdminMessage adminMessage = new AdminMessage();
            //封装管理员消息共同数据
            //消息对应的messageId
            adminMessage.setMessageId(message.getMessageId());
            //消息状态未读
            adminMessage.setAdminMessageStatus(0);
            //消息未处理
            adminMessage.setAdminMessageIfDeal(0);
            //消息建立时间
            adminMessage.setAdminMessageCreateTime(changeTime);

            if (adminMessage1.getAdminMessageIfDeal() == 1) {
                //再审核
                adminMessage.setAdminMessageDealAgain(1);
            }
            if (adminMessage1.getAdminMessageIfDeal() == 0) {
                //初审核
                adminMessage.setAdminMessageDealAgain(0);
            }

            for (Admin admin : adminList) {
                //管理员id
                adminMessage.setAdminId(admin.getAdminId());
                //管理员消息的Id
                adminMessage.setAdminMessageId(UUIDUtils.getId());
                //封装好数据之后插入数据库中
                adminMessageDao.addAMessageToAdmin(adminMessage);
            }
            log.info("开启所有管理员线程查询审核消息");
            Iterator<Map.Entry<String, AdminThread>> iterator = AdminWebSocket.adminThreadMap.entrySet().iterator();
            //设置管理员线程的查询操作参数标志flagParam
            //1：查询最新消息 2：查询未处理消息 3：查询已完成消息 4：查询提醒型消息 5：查询在审核消息
            AdminThread.flagParam = 5;
            while (iterator.hasNext()) {
                AdminThread adminThread = iterator.next().getValue();
                adminThread.run();
                try {
                    adminThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("查询审核消息结束");

            //给当前学生(活动部分者)发送消息
            StudentMessage studentMessage1 = new StudentMessage();
            //消息id
            studentMessage1.setStudMessageId(UUIDUtils.getId());
            //学生id
            studentMessage1.setStudId(activity.getActiBuilderId());
            //关联的messageId
            studentMessage1.setMessageId(message.getMessageId());
            //消息未读状态
            studentMessage1.setStudMessageStatus(0);
            //消息建立时间
            studentMessage1.setStudMessageCreateTime(changeTime);
            //消息内容
            studentMessage1.setStudMessageComment(MessageEnum.CHECK_AGAINING.getStatusDesc());
            //插入到数据库中
            studentMessageDao.addAMessageToStudent(studentMessage1);

            //给所有报名参加这项活动的学生添加一条消息，并给在线的学生发送消息
            //1、查询出报名了此项活动的所有学生
            List<String> studentIdList = dataInfoDao.selectStudIdListByActiId(activity.getActiId());
            if (studentIdList.size()!=0) {
                log.info("报名参加项活动的学生数量" + studentIdList.size() + "学生详情" + studentIdList);
                //封装消息的相同部分
                StudentMessage studentMessage = new StudentMessage();
                //这条消息的产生时间(跟上面活动修改时产生消息的时间保持一致)
                studentMessage.setStudMessageCreateTime(changeTime);
                //消息的内容(这里是直接通过使用由枚举型准备好的内容)
                studentMessage.setStudMessageComment(MessageEnum.CHECK_AGAIN.getStatusDesc());
                //消息的默认状态未读
                studentMessage.setStudMessageStatus(0);
                for (String studentId : studentIdList) {
                    //封装学生不同的消息数据
                    //消息id
                    studentMessage.setStudMessageId(UUIDUtils.getId());
                    //学生id
                    studentMessage.setStudId(studentId);
                    //关联的消息id(即修改活动时插入的消息id)
                    studentMessage.setMessageId(message.getMessageId());
                    //将这条消息插入数据库中
                    studentMessageDao.addAMessageToStudent(studentMessage);
                    log.info("给学生添加的消息" + studentMessage);
                }
            }

            if (studentIdList.size()!=0) {
                log.info("开启报了这项活动的在线学生的线程");
                log.info("是否有学生====" + "学生数量" + studentIdList.size());
                ConcurrentHashMap studentThreadMap = StudentWebSocket.studentThreadMap;
                for (int i = 0; i < studentIdList.size(); i++) {
                    StudentThread studentThread = (StudentThread) studentThreadMap.get(studentIdList.get(i));
                    if (studentThread != null) {
                        log.info("学生线程不为空");
                        studentThread.run();
                        try {
                            studentThread.join();
                            log.info("学生线程开启了");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                log.info("开启报了这项活动的在线学生的线程结束");
            }
            //更新mssage
            //保存活动的原状态
            message.setMessageActiStatus(activity.getActiStatusId());
            //活动修改时间
            message.setMessageCreateTime(changeTime);
            //更新到数据库
            messageDao.changeMessageByMessageId(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
