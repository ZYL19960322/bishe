package com.bishe.service;

import com.bishe.dto.CheckAgainActivity;
import com.bishe.pojo.Activity;
import com.bishe.pojo.AdminMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ZYL on 2019/3/18.
 */
public interface AdminMessageService {
    List<AdminMessage> selectAllUnreadMessageByAdminId(String adminId);
    List<CheckAgainActivity>selectCheckingActivityByAdminId(String adminId);
    int deleteMessageByActiId(@Param("actiId")String actiId, @Param("adminId")String adminId);
    List<AdminMessage> selectAllCheckAgainMessageByAdminId(String adminId);
    List<CheckAgainActivity>selectCheckAgainActivityByAdminId(String adminId);
    int deleteMessageByAdminMessageId(String adminMessageId);
    List<CheckAgainActivity>selectUndealMessageByAdminId(String adminId);
    List<CheckAgainActivity>selectFinishedMessageByAdminId(String adminId);
    int setMessageHasReaded(String adminMessageId);
}
