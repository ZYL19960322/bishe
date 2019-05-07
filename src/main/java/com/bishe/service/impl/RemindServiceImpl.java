package com.bishe.service.impl;

import ch.qos.logback.core.util.TimeUtil;
import com.bishe.controller.WebSocket.AdminThread;
import com.bishe.controller.WebSocket.AdminWebSocket;
import com.bishe.dao.*;
import com.bishe.dto.AdminPage;
import com.bishe.dto.RemindMessage;
import com.bishe.dto.StudentClassInfo;
import com.bishe.pojo.*;
import com.bishe.service.MessageService;
import com.bishe.service.RemindService;
import com.bishe.utils.TimeUtils;
import com.bishe.utils.UUIDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ZYL on 2019/4/11.
 */
@Service
@Transactional
public class RemindServiceImpl implements RemindService {
    @Autowired
    MessageDao messageDao;
    @Autowired
    AdminDao adminDao;
    @Autowired
    RemindDao remindDao;
    @Autowired
    ActivityDao activityDao;
    @Autowired
    StudentDao studentDao;
    @Autowired
    StudentClassDao studentClassDao;
    @Autowired
    AdminMessageDao adminMessageDao;
    @Override
    public Boolean addARemindMessage(String actiId, String remindComment) {
        Logger log = LoggerFactory.getLogger(RemindServiceImpl.class);
        //插入之前先判断之前是否发送过，如果有则先删除再插入，没有则直接插入
        Remind remind = remindDao.selectRemindByActiId(actiId);
        if (remind!=null){
            log.info("之前发送的一条消息"+remind);
            remindDao.deleteRemindByActiId(actiId);
        }
        //根据活动id查询出message,使用其message_id、学生id
        //查询出所有的管理员给所有的管理员添加一条消息
        try {
            List<Admin> adminList = adminDao.selectAllAdmin();
            Remind remindMessage = new Remind();
            //封装消息的共同部分
            Message message = messageDao.selectMessageByActiId(actiId);
            //message_id
            remindMessage.setMessageId(message.getMessageId());
            //活动id
            remindMessage.setRemindActiId(message.getMessageActiId());
            //消息内容
            remindMessage.setRemindComment(remindComment);
            //时间
            remindMessage.setRemindCreateTime(TimeUtils.getStringDate());
            //初始态：false未读,true已读
            remindMessage.setRemindIfDeal(0);
            for (Admin admin:adminList){
                //自身id
                remindMessage.setRemindId(UUIDUtils.getId());
                //管理员id
                remindMessage.setRemindAdminId(admin.getAdminId());
                //插入数据库
                remindDao.addARemind(remindMessage);
            }
            log.info("开启所有管理员线程查询审核消息");
            Iterator<Map.Entry<String, AdminThread>> iterator = AdminWebSocket.adminThreadMap.entrySet().iterator();
            //设置管理员线程的查询操作参数标志flagParam
            //1：查询最新消息 2：查询未处理消息 3：查询已完成消息 4：查询提醒型消息 5：查询在审核消息
            AdminThread.flagParam = 4;
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
            return true;
        }catch (Exception e){
            e.printStackTrace();

        }
        return false ;
    }

    @Override
    public PageInfo<RemindMessage> selectAllRemindMessageByAdminId(String adminId) {

        //1、根据管理id查询remind表查询出管理员的所有remind消息(有用的：消息内容、申请时间、是否已被处理)，按照时间排序
        //2、根据remind中的活动id查询出活动主题
        //3、根据remind中的活动id查询出message 进而上一次申请的时间
        //4、根据message中的message_builder_id查询学生信息，
        //5、查询和封装班级信息
        //6、封装是否已处理，转成布尔型
        List<RemindMessage> remindMessageList=new ArrayList<>();
        List<Remind> remindList = remindDao.selectAllRemindByAdminId(adminId);
        for (Remind remind:remindList){
            RemindMessage remindMessage = new RemindMessage();
            //remind的id
            remindMessage.setRemindId(remind.getRemindId());
            //消息内容
            remindMessage.setRemindComment(remind.getRemindComment());
            //本次申请时间
            remindMessage.setRemindCreateTime(remind.getRemindCreateTime());
            //是否已被处理
            remindMessage.setRemindIfDeal(remind.getRemindIfDeal());
            String activityTheme = activityDao.selectActiThemeByActiId(remind.getRemindActiId());
            //活动主题
            remindMessage.setActiTheme(activityTheme);
            //活动id
            remindMessage.setActiId(remind.getRemindActiId());
            Message message = messageDao.selectMessageByActiId(remind.getRemindActiId());
            //上次申请时间
            remindMessage.setMessageCreateTime(message.getMessageCreateTime());
            //审核时的消息原状态：新审核、再审核、未处理、已完成、已被删除
            AdminMessage adminMessage = adminMessageDao.selectMessageByActiIdAndAdminId(message.getMessageActiId(), adminId);
            if (adminMessage!=null){
                //不为空
                if(adminMessage.getAdminMessageIfDeal()==1){//消息已处理
                    remindMessage.setBeforeMessageStatus("已完成消息");
                }
                if(adminMessage.getAdminMessageStatus()==0 && adminMessage.getAdminMessageIfDeal()==0 && adminMessage.getAdminMessageDealAgain()==0){
                    //新审核
                    remindMessage.setBeforeMessageStatus("新审核消息");
                }
                if(adminMessage.getAdminMessageStatus()==1 && adminMessage.getAdminMessageIfDeal()==0 && adminMessage.getAdminMessageDealAgain()==0){
                    remindMessage.setBeforeMessageStatus("未处理消息");
                }
                if(adminMessage.getAdminMessageIfDeal()==0 && adminMessage.getAdminMessageDealAgain()==1){
                    remindMessage.setBeforeMessageStatus("再审核消息");
                }
            }else {
                //为空
                remindMessage.setBeforeMessageStatus("已被您删除");

            }

            //学生姓名(如果有报名信息的名字则使用报名信息的名字，否则使用登录名)
            Student student = studentDao.selectStudentByStudentId(message.getMessageBuilderId());
            if(student.getStudEnrollName()!=null){
                remindMessage.setStudName(student.getStudEnrollName());
            }else {
                remindMessage.setStudName(student.getStudName());
            }


            //学生班级信息、先判断是否有班级信息
            if (student.getStudClassID()!=null){
                StudentClassInfo studentClassInfo = studentClassDao.selectStudentClassInfo(student.getStudClassID());
                String classInfo=studentClassInfo.getStudGradeName().substring(2,4)+studentClassInfo.getStudClassName();
                remindMessage.setClassInfo(classInfo);
            }
            //消息是否已处理
            if(remind.getRemindIfDeal()==1){
                remindMessage.setDealFlag(true);
            }
            else {
                remindMessage.setDealFlag(false);
            }
            remindMessageList.add(remindMessage);
        }
        PageHelper.startPage(AdminPage.pageNum,AdminPage.pageSize);
        PageInfo<RemindMessage> remindPageInfo = new PageInfo<>(remindMessageList);
        return remindPageInfo;
    }

    @Override
    public Boolean deleteRemindByRemindId(String remindId) {
        if (remindDao.deleteRemindByRemindId(remindId)!=0){
            return true;
        }
        return false;
    }
}
