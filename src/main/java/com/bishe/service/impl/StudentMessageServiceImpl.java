package com.bishe.service.impl;

import com.bishe.controller.WebSocket.AdminThread;
import com.bishe.controller.WebSocket.AdminWebSocket;
import com.bishe.controller.WebSocket.StudentThread;
import com.bishe.controller.WebSocket.StudentWebSocket;
import com.bishe.dao.*;
import com.bishe.dto.StudentMessageResult;
import com.bishe.enums.ActivityStatusEnum;
import com.bishe.enums.MessageEnum;
import com.bishe.pojo.*;
import com.bishe.service.StudentMessageService;
import com.bishe.utils.TimeUtils;
import com.bishe.utils.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by ZYL on 2019/3/19.
 */
@Service
@Transactional
public class StudentMessageServiceImpl implements StudentMessageService {
    Logger log = LoggerFactory.getLogger(StudentMessageServiceImpl.class);
    @Autowired
    AdminMessageDao adminMessageDao;
    @Autowired
    ActivityDao activityDao;
    @Autowired
    StudentMessageDao studentMessageDao;
    @Autowired
    DataInfoDao dataInfoDao;
    @Autowired
    RemindDao remindDao;
    @Autowired
    MessageDao messageDao;

    @Override
    public Boolean adminCheckActivityPass(String adminMessageId) {
        try {


            //根据adminMessageId查询出申请审核活动时产生的消息进而查询出管理员对应的这个消息
            AdminMessage adminMessage = adminMessageDao.selectAdminMessageByAdminMessageId(adminMessageId);
            //AdminMessage adminMessage = adminMessageDao.selectMessageByActiIdAndAdminId(actiId, adminId);
            log.info("查询出管理员消息" + adminMessage);

            //管理员根据这个消息的id给所有管理员的这条消息添加本次处理的处理信息
            //这个消息是否被处理默认0表示未处理，1表示已处理
            adminMessage.setAdminMessageIfDeal(1);
            //处理人
            adminMessage.setAdminMessageDealerId(adminMessage.getAdminId());
            //处理时间(跟下面学生的消息的产生时间保持一致)
            String time = TimeUtils.getStringDate();
            adminMessage.setAdminMessageDealTime(time);
            //更新到数据库中
            int i = adminMessageDao.updateAllAdminMessageByAdminMessage(adminMessage);
            if (i != 0) {
                log.info("更新所有的管理员的消息成功");
            }

            //根据adminMessage中的messageId恢复活动的原状态
            //各级adminMessage中的messageId查询出message
            Message message = adminMessageDao.selectMessageByAdminMessageId(adminMessageId);
            //根据message将对应的remind设置为已处理
            remindDao.updateRemindStatusByMessageId(message.getMessageId());


            //将活动的状态改为报名中
            activityDao.changeActivityStatusByActiId(message.getMessageActiId(),ActivityStatusEnum.ACTIVITY_ENROLLING.getStatusId());
            //给发布活动的学生添加一条消息
            StudentMessage studentMessage = new StudentMessage();
            //封装学生的消息数据
            //消息id
            studentMessage.setStudMessageId(UUIDUtils.getId());
            //学生id
            studentMessage.setStudId(message.getMessageBuilderId());
            //关联的消息id(即申请活动时插入的消息id)
            studentMessage.setMessageId(adminMessage.getMessageId());
            //这条消息的产生时间(跟上面管理更改管理员消息的时间保持一致)
            studentMessage.setStudMessageCreateTime(time);
            //消息的内容(这里是使用枚举型的内容)
            studentMessage.setStudMessageComment(MessageEnum.CHECK_PASS.getStatusDesc());
            //消息的默认状态未读
            studentMessage.setStudMessageStatus(0);
            //将这条消息插入数据库中
            studentMessageDao.addAMessageToStudent(studentMessage);
            log.info("给学生添加的消息" + studentMessage);

            log.info("活动初审核通过开启学生的线程");
            StudentThread studentThread = StudentWebSocket.studentThreadMap.get(message.getMessageBuilderId());
            if (studentThread != null) {
                studentThread.run();
                studentThread.join();
                log.info("活动初审核通过开启学生的线程结束");
            }


            return true;
        } catch (Exception e) {
            e.getMessage();
        }


        return false;
    }


    @Override
    public Boolean backMessage( String adminMessageId, String messageComment) {
        //在管理员下标记这条消息为已读
        try {
            //根据活动id和管理的id通过查询出申请审核活动时产生的消息进而查询出管理员对应的这个消息
            AdminMessage adminMessage = adminMessageDao.selectAdminMessageByAdminMessageId(adminMessageId);
            log.info("查询出管理员消息" + adminMessage);

            //管理员根据这个消息的id给所有管理员的这条消息添加本次处理的处理信息
            //这个消息是否被处理默认0表示未处理，1表示已处理
            adminMessage.setAdminMessageIfDeal(1);
            //处理人
            adminMessage.setAdminMessageDealerId(adminMessage.getAdminId());
            //处理时间(跟下面学生的消息的产生时间保持一致)
            String time = TimeUtils.getStringDate();
            adminMessage.setAdminMessageDealTime(time);
            //更新到数据库中
            int i = adminMessageDao.updateAllAdminMessageByAdminMessage(adminMessage);
            if (i != 0) {
                log.info("更新所有的管理员的消息成功");
            }

            //给对应的学生插入一条消息
            //1.根据管理员消息id查询message下的这条message数据
            Message message = adminMessageDao.selectMessageByAdminMessageId(adminMessageId);
            //根据message将对应的remind设置为已处理
            remindDao.updateRemindStatusByMessageId(message.getMessageId());
            String builderId = message.getMessageBuilderId();
            StudentMessage studentMessage = new StudentMessage();
            //封装学生的消息数据
            //消息id
            studentMessage.setStudMessageId(UUIDUtils.getId());
            //学生id
            studentMessage.setStudId(builderId);
            //关联的消息id(即申请活动时插入的消息id)
            studentMessage.setMessageId(adminMessage.getMessageId());
            //这条消息的产生时间(跟上面管理更改管理员消息的时间保持一致)
            studentMessage.setStudMessageCreateTime(time);
            //消息的内容(这里是使用管理审核的内容)
            studentMessage.setStudMessageComment(messageComment);
            //消息的默认状态未读
            studentMessage.setStudMessageStatus(0);
            //将这条消息插入数据库中
            studentMessageDao.addAMessageToStudent(studentMessage);
            log.info("给学生添加的消息" + studentMessage);

            //将消息内容插入到学生的消息表中
            //根据活动id查询出学生，如果学生在线给学生发送一条消息通知
            log.info("服务器开启学生的线程");
            StudentThread studentThread = StudentWebSocket.studentThreadMap.get(builderId);
            if(studentThread !=null){
                studentThread.run();
                studentThread.join();
                log.info("服务器开启学生的线程结束");
            }


            //给在线的管理员发送消息
            log.info("开启所有管理员的线程");

            Iterator<Map.Entry<String, AdminThread>> iterator = AdminWebSocket.adminThreadMap.entrySet().iterator();
            while (iterator.hasNext()) {
                AdminThread adminThread = iterator.next().getValue();
                try {
                    adminThread.run();
                    adminThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("所有的管理员的线程开启完成");





            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean adminCheckAgainActivityPass(String adminMessageId) {

        //根据管理员消息id连接message表查询出活动id和活动的原始状态，将这个活动的状态改为原始状态
        //更新其他管理员这条消息的内容
        //给对应的学生插入一条消息
        //判断这个学生是否在线，如果在线则执行这个学生的线程返回一个消息
        try {


            //1，根据adminMessageId查询出这条消息内容
            AdminMessage adminMessage = adminMessageDao.selectAdminMessageByAdminMessageId(adminMessageId);
            //管理员根据这个消息的id给所有管理员的这条消息添加本次处理的处理信息
            //这个消息是否被处理默认0表示未处理，1表示已处理
            adminMessage.setAdminMessageIfDeal(1);
            //处理人
            adminMessage.setAdminMessageDealerId(adminMessage.getAdminId());
            //处理时间(跟下面学生的消息的产生时间保持一致)
            String time = TimeUtils.getStringDate();
            adminMessage.setAdminMessageDealTime(time);
            //更新到数据库中
            int i = adminMessageDao.updateAllAdminMessageByAdminMessage(adminMessage);
            if (i != 0) {
                log.info("更新所有的管理员的消息成功");
            }

            //根据adminMessage中的messageId恢复活动的原状态
            //各级adminMessage中的messageId查询出message
            Message message = adminMessageDao.selectMessageByAdminMessageId(adminMessageId);

            //根据message将对应的remind设置为已处理
            remindDao.updateRemindStatusByMessageId(message.getMessageId());
            //恢复活动的状态
            activityDao.changeActivityStatusByActiId(message.getMessageActiId(), message.getMessageActiStatus());
            //给发布活动的学生添加一条消息
            StudentMessage studentMessage = new StudentMessage();
            //封装学生的消息数据
            //消息id
            studentMessage.setStudMessageId(UUIDUtils.getId());
            //学生id
            studentMessage.setStudId(message.getMessageBuilderId());
            //关联的消息id(即申请活动时插入的消息id)
            studentMessage.setMessageId(adminMessage.getMessageId());
            //这条消息的产生时间(跟上面管理更改管理员消息的时间保持一致)
            studentMessage.setStudMessageCreateTime(time);
            //消息的内容(这里是使用枚举型的内容)
            studentMessage.setStudMessageComment(MessageEnum.CHECK_PASS.getStatusDesc());
            //消息的默认状态未读
            studentMessage.setStudMessageStatus(0);
            //将这条消息插入数据库中
            studentMessageDao.addAMessageToStudent(studentMessage);
            log.info("给学生添加的消息" + studentMessage);

            log.info("服务器开启活动发布的学生的线程");
            StudentThread studentThread = StudentWebSocket.studentThreadMap.get(message.getMessageBuilderId());
            if (studentThread != null) {
                studentThread.run();
                studentThread.join();
                log.info("服务器开启活动发布学生的线程结束");
            }

            //给所有报名参加这项活动的学生添加一条消息，并给在线的学生发送消息
            //1、查询出报名了此项活动的所有学生
            List<String> studentIdList = dataInfoDao.selectStudIdListByActiId(message.getMessageActiId());
            if (studentIdList.size() != 0) {
                log.info("报名参加项活动的学生数量" + studentIdList.size() + "学生详情" + studentIdList);
                for (String studentId : studentIdList) {
                    StudentMessage studentMessages = new StudentMessage();
                    //封装学生的消息数据
                    //消息id
                    studentMessages.setStudMessageId(UUIDUtils.getId());
                    //学生id
                    studentMessages.setStudId(studentId);
                    //关联的消息id(即修改活动时插入的消息id)
                    studentMessages.setMessageId(message.getMessageId());
                    //这条消息的产生时间(跟上面活动修改时产生消息的时间保持一致)
                    studentMessages.setStudMessageCreateTime(time);
                    //消息的内容(这里是直接通过使用由枚举型准备好的内容)
                    studentMessages.setStudMessageComment(MessageEnum.CHECK_AGAIN_PASS.getStatusDesc());
                    //消息的默认状态未读
                    studentMessages.setStudMessageStatus(0);
                    //将这条消息插入数据库中
                    studentMessageDao.addAMessageToStudent(studentMessages);
                    log.info("活动再审核通过给学生添加的消息" + studentMessages);
                }
            }
            //给报名了这项活动的并且在线的学生发送消息
            if (studentIdList.size() != 0) {
                log.info("开启报了这项活动的在线学生的线程");
                log.info("是否有学生==" + "学生数量" + studentIdList.size());
                ConcurrentHashMap studentThreadMap = StudentWebSocket.studentThreadMap;

                for ( int a = 0; a < studentIdList.size(); a++) {
                    StudentThread studThread =(StudentThread)studentThreadMap.get(studentIdList.get(a));
                    if (studThread != null) {
                        log.info("学生线程不为空nnn ");
                        studThread.run();
                        try {
                            studThread.join();
                            log.info("学生线程开启了ccc");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
                return true;
            }catch(Exception e){
                e.printStackTrace();
            }
            return false;
        }

    @Override
    public Boolean backMessageByadminMessageId(String adminMessageId, String messageComment) {
        //在管理员下标记这条消息为已读
        try {
            //根据管理员的消息id查询出管理员对应的这个消息
            AdminMessage adminMessage = adminMessageDao.selectAdminMessageByAdminMessageId(adminMessageId);
            log.info("查询出管理员消息" + adminMessage);

            //管理员根据这个消息的id给所有管理员的这条消息添加本次处理的处理信息
            //这个消息是否被处理默认0表示未处理，1表示已处理
            adminMessage.setAdminMessageIfDeal(1);
            //处理人
            adminMessage.setAdminMessageDealerId(adminMessage.getAdminId());
            //处理时间(跟下面学生的消息的产生时间保持一致)
            String time = TimeUtils.getStringDate();
            adminMessage.setAdminMessageDealTime(time);
            //更新到数据库中
            int i = adminMessageDao.updateAllAdminMessageByAdminMessage(adminMessage);
            if (i != 0) {
                log.info("更新所有的管理员的消息成功");
            }

            //给对应的学生插入一条消息
            //1.根据管理员消息id查询message下的这条message数据
            Message message = adminMessageDao.selectMessageByAdminMessageId(adminMessageId);


            //根据message将对应的remind设置为已处理
            remindDao.updateRemindStatusByMessageId(message.getMessageId());

            String builderId = message.getMessageBuilderId();
            StudentMessage studentMessage = new StudentMessage();
            //封装学生的消息数据
            //消息id
            studentMessage.setStudMessageId(UUIDUtils.getId());
            //学生id
            studentMessage.setStudId(builderId);
            //关联的消息id(即申请活动时插入的消息id)
            studentMessage.setMessageId(message.getMessageId());
            //这条消息的产生时间(跟上面管理更改管理员消息的时间保持一致)
            studentMessage.setStudMessageCreateTime(time);
            //消息的内容(这里是使用管理审核的内容)
            studentMessage.setStudMessageComment(messageComment);
            //消息的默认状态未读
            studentMessage.setStudMessageStatus(0);
            //将这条消息插入数据库中
            studentMessageDao.addAMessageToStudent(studentMessage);
            log.info("给学生添加的消息" + studentMessage);

            //将消息内容插入到学生的消息表中
            //根据活动id查询出学生，如果学生在线给学生发送一条消息通知
            log.info("服务器开启学生的线程");
            StudentThread studentThread = StudentWebSocket.studentThreadMap.get(builderId);
            if(studentThread !=null){
                studentThread.run();
                studentThread.join();
                log.info("服务器开启学生的线程结束");
            }


            //给在线的管理员发送消息
            log.info("开启所有管理员的线程");

            Iterator<Map.Entry<String, AdminThread>> iterator = AdminWebSocket.adminThreadMap.entrySet().iterator();
            while (iterator.hasNext()) {
                AdminThread adminThread = iterator.next().getValue();
                try {
                    adminThread.run();
                    adminThread.join();
                    log.info("开启管理员线程");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("所有的管理员的线程开启完成");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean checkPassByActiIdAndAdminId(String actiId, String adminId) {
        //1根据actiId连接message表查询出活动id和活动的原始状态，将这个活动的状态改为报名中
        //2根据messageId查询出这条活动对应的管理消息然后进行更新管理的消息(取出一条即可，判断是否为空、避免全部管理员都把消息删除了)
        //3更新管理员这条消息的内容
        //4更新remind表
        //5给对应的发布者学生插入一条消息
        //6判断这个发布者学生是否在线，如果在线则执行这个学生的线程返回一个消息
        //7判断是否有学生报名给所有报名参加这项活动的学生添加一条消息，并给在线的学生发送消息

        try {

            //1根据actiId连接message表查询出一条message 根据message的活动id和活动的原始状态，将这个活动的状态改为报名状态
            Message message = messageDao.selectMessageByActiId(actiId);
            activityDao.changeActivityStatusByActiId(message.getMessageActiId(),ActivityStatusEnum.ACTIVITY_ENROLLING.getStatusId());

            //2根据messageId查询出这条活动对应的管理消息然后进行更新管理的消息(取出一条即可，判断是否为空、避免全部管理员都把消息删除了;如果全部管理员都把消息删除了，则不进行更新)
            AdminMessage adminMessage = adminMessageDao.selectAdminMessageByMessageId(message.getMessageId());
            //处理时间(跟下面学生的消息的产生时间保持一致)
            String time = TimeUtils.getStringDate();
            if(adminMessage==null){
                //处理人
                adminMessage.setAdminMessageDealerId(adminId);
                adminMessage.setAdminMessageIfDeal(1);

                adminMessage.setAdminMessageDealTime(time);
                //3更新管理员这条消息的内容
                int i = adminMessageDao.updateAllAdminMessageByAdminMessage(adminMessage);
                if (i != 0) {
                    log.info("更新所有的管理员的消息成功");
                }
            }


            //4更新remind表
            //根据message将对应的remind设置为已处理
            remindDao.updateRemindStatusByMessageId(message.getMessageId());
            //5给对应的发布者学生插入一条消息
            //给发布活动的学生添加一条消息
            StudentMessage studentMessage = new StudentMessage();
            //封装学生的消息数据
            //消息id
            studentMessage.setStudMessageId(UUIDUtils.getId());
            //学生id
            studentMessage.setStudId(message.getMessageBuilderId());
            //关联的消息id(即申请活动时插入的消息id)
            studentMessage.setMessageId(message.getMessageId());
            //这条消息的产生时间(跟上面管理更改管理员消息的时间保持一致)
            studentMessage.setStudMessageCreateTime(time);
            //消息的内容(这里是使用枚举型的内容)
            studentMessage.setStudMessageComment(MessageEnum.CHECK_PASS.getStatusDesc());
            //消息的默认状态未读
            studentMessage.setStudMessageStatus(0);
            //将这条消息插入数据库中
            studentMessageDao.addAMessageToStudent(studentMessage);
            log.info("给学生添加的消息" + studentMessage);
            //6判断这个发布者学生是否在线，如果在线则执行这个学生的线程返回一个消息

            StudentThread studentThread = StudentWebSocket.studentThreadMap.get(message.getMessageBuilderId());
            if (studentThread != null) {
                log.info("服务器开启活动发布的学生的线程");
                studentThread.run();
                studentThread.join();
                log.info("服务器开启活动发布学生的线程结束");
            }

            //7判断是否有学生报名给所有报名参加这项活动的学生添加一条消息，并给在线的学生发送消息
            List<String> studentIdList = dataInfoDao.selectStudIdListByActiId(message.getMessageActiId());
            if (studentIdList.size() != 0) {
                log.info("报名参加项活动的学生数量" + studentIdList.size() + "学生详情" + studentIdList);
                for (String studentId : studentIdList) {
                    StudentMessage studentMessages = new StudentMessage();
                    //封装学生的消息
                    //消息id
                    studentMessages.setStudMessageId(UUIDUtils.getId());
                    //学生id
                    studentMessages.setStudId(studentId);
                    //关联的消息id(即修改活动时插入的消息id)
                    studentMessages.setMessageId(message.getMessageId());
                    //这条消息的产生时间(跟上面活动修改时产生消息的时间保持一致)
                    studentMessages.setStudMessageCreateTime(time);
                    //消息的内容(这里是直接通过使用由枚举型准备好的内容)
                    studentMessages.setStudMessageComment(MessageEnum.CHECK_AGAIN_PASS.getStatusDesc());
                    //消息的默认状态未读
                    studentMessages.setStudMessageStatus(0);
                    //将这条消息插入数据库中
                    studentMessageDao.addAMessageToStudent(studentMessages);
                    log.info("活动再审核通过给学生添加的消息" + studentMessages);
                }
            }
            //给报名了这项活动的并且在线的学生发送消息
            if (studentIdList.size() != 0) {
                log.info("开启报了这项活动的在线学生的线程");
                log.info("是否有学生==" + "学生数量" + studentIdList.size());
                ConcurrentHashMap studentThreadMap = StudentWebSocket.studentThreadMap;

                for ( int a = 0; a < studentIdList.size(); a++) {
                    StudentThread studThread =(StudentThread)studentThreadMap.get(studentIdList.get(a));
                    if (studThread != null) {
                        log.info("学生线程不为空nnn ");
                        studThread.run();
                        try {
                            studThread.join();
                            log.info("学生线程开启了ccc");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean backMessageByActidId(String actiId, String messageComment,String adminId) {

        //根据活动id查询出message
        //根据message的message查询出一条adminMessage,判断是否为空，不为空则更新管理员消息，为空则不更新，
        //更新remind消息
        //给活动的发布者者一条消息
    try {
        Message message = messageDao.selectMessageByActiId(actiId);
        AdminMessage adminMessage = adminMessageDao.selectAdminMessageByMessageId(message.getMessageId());
        //处理时间(跟下面学生的消息的产生时间保持一致)
        String time = TimeUtils.getStringDate();
        if(adminMessage!=null){
            //这个消息是否被处理默认0表示未处理，1表示已处理
            adminMessage.setAdminMessageIfDeal(1);
            //处理人
            adminMessage.setAdminMessageDealerId(adminId);

            adminMessage.setAdminMessageDealTime(time);
            //更新到数据库
            int i = adminMessageDao.updateAllAdminMessageByAdminMessage(adminMessage);
            if (i != 0) {
                log.info("更新所有的管理员的消息成功");
            }
        }

        //根据message将对应的remind设置为已处理
        remindDao.updateRemindStatusByMessageId(message.getMessageId());

        //给发布者添加消息
        String builderId = message.getMessageBuilderId();
        StudentMessage studentMessage = new StudentMessage();
        //封装学生的消息数据
        //消息id
        studentMessage.setStudMessageId(UUIDUtils.getId());
        //学生id
        studentMessage.setStudId(builderId);
        //关联的消息id(即申请活动时插入的消息id)
        studentMessage.setMessageId(message.getMessageId());
        //这条消息的产生时间(跟上面管理更改管理员消息的时间保持一致)
        studentMessage.setStudMessageCreateTime(time);
        //消息的内容(这里是使用管理审核的内容)
        studentMessage.setStudMessageComment(messageComment);
        //消息的默认状态未读
        studentMessage.setStudMessageStatus(0);
        //将这条消息插入数据库中
        studentMessageDao.addAMessageToStudent(studentMessage);
        log.info("给学生添加的消息" + studentMessage);

        //将消息内容插入到学生的消息表中
        //根据活动id查询出学生，如果学生在线给学生发送一条消息通知
        log.info("服务器开启学生的线程");
        StudentThread studentThread = StudentWebSocket.studentThreadMap.get(builderId);
        if(studentThread !=null){
            studentThread.run();
            studentThread.join();
            log.info("服务器开启学生的线程结束");
        }

        //给在线的管理员发送消息
        log.info("开启所有管理员的线程");

        Iterator<Map.Entry<String, AdminThread>> iterator = AdminWebSocket.adminThreadMap.entrySet().iterator();
        while (iterator.hasNext()) {
            AdminThread adminThread = iterator.next().getValue();
            try {
                adminThread.run();
                adminThread.join();
                log.info("开启管理员线程");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("所有的管理员的线程开启完成");
        return true;
    }catch (Exception e){
        e.printStackTrace();
    }
        return false;
    }


    @Override
    public List<StudentMessage> selectAllUnreadMessageByStudId(String studId) {
        //根据学生id查询该学生的所有未读消息(最新消息)
        return studentMessageDao.selectAllUnreadMessageByStudId(studId);
    }

    @Override
    public List<StudentMessageResult> selectAllMessageByStudId(String studId) {
        return studentMessageDao.selectAllMessageByStudId(studId);
    }

    @Override
    public int changeMessageStatusByStudId(String studId) {
        return studentMessageDao.changeMessageStatusByStudId(studId);
    }

    @Override
    public int deleteMessageByMessageId(String studMessageId) {
        return studentMessageDao.deleteMessageByMessageId(studMessageId);
    }

    @Override
    public Activity selectActivityByStudMessageId(String studMessageId) {
        return studentMessageDao.selectActivityByStudMessageId(studMessageId);
    }


}
