package com.bishe.service.impl;

import com.bishe.dao.AdminMessageDao;
import com.bishe.dto.CheckAgainActivity;
import com.bishe.pojo.Activity;
import com.bishe.pojo.AdminMessage;
import com.bishe.service.AdminMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ZYL on 2019/3/18.
 */
@Service
@Transactional
public class AdminMessageServiceImpl implements AdminMessageService {

    @Autowired
    AdminMessageDao adminMessageDao;
    @Override
    public List<AdminMessage> selectAllUnreadMessageByAdminId(String adminId) {
        return adminMessageDao.selectAllUnreadMessageByAdminId(adminId);
    }

    @Override
    public List<CheckAgainActivity> selectCheckingActivityByAdminId(String adminId) {
        //查询出所有未读的未处理的消息活动
        List<CheckAgainActivity> activityList = adminMessageDao.selectCheckingActivityByAdminId(adminId);
        //管理员将自己的所有初审核消息的状态值为已读
        //adminMessageDao.changeMessageStatusByAdminId(adminId);
        return  activityList;
    }

    @Override
    public int deleteMessageByActiId(String actiId, String adminId) {
        return adminMessageDao.deleteMessageByActiId(actiId,adminId);
    }

    @Override
    public List<AdminMessage> selectAllCheckAgainMessageByAdminId(String adminId) {
        return adminMessageDao.selectAllCheckAgainMessageByAdminId(adminId);
    }

    @Override
    public List<CheckAgainActivity> selectCheckAgainActivityByAdminId(String adminId) {
        return adminMessageDao.selectCheckAgainActivityByAdminId(adminId);
    }

    @Override
    public int deleteMessageByAdminMessageId(String adminMessageId) {
        return adminMessageDao.deleteMessageByAdminMessageId(adminMessageId);
    }

    @Override
    public List<CheckAgainActivity> selectUndealMessageByAdminId(String adminId) {
        return adminMessageDao.selectUndealMessageByAdminId(adminId);
    }

    @Override
    public List<CheckAgainActivity> selectFinishedMessageByAdminId(String adminId) {
       return adminMessageDao.selectFinishedMessageByAdminId(adminId);

    }

    @Override
    public int setMessageHasReaded(String adminMessageId) {
        return adminMessageDao.setMessageHasReaded(adminMessageId);
    }
}
