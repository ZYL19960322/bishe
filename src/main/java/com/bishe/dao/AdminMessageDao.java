package com.bishe.dao;


import com.bishe.dto.CheckAgainActivity;
import com.bishe.pojo.Activity;
import com.bishe.pojo.AdminMessage;
import com.bishe.pojo.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ZYL on 2019/3/18.
 */
@Mapper
public interface AdminMessageDao {
    void addAMessageToAdmin(AdminMessage adminMessage);
    List<AdminMessage> selectAllUnreadMessageByAdminId(String adminId);
    List<CheckAgainActivity>selectCheckingActivityByAdminId(String adminId);
    AdminMessage selectMessageByActiIdAndAdminId(@Param("actiId")String actiId,@Param("adminId") String adminId);
    int changeMessageStatusByAdminId(String adminId);
    int updateAllAdminMessageByAdminMessage(AdminMessage adminMessage);
    int deleteMessageByActiId(@Param("actiId")String actiId,@Param("adminId")String adminId);
    List<AdminMessage> selectAllCheckAgainMessageByAdminId(String adminId);
    List<CheckAgainActivity>selectCheckAgainActivityByAdminId(String adminId);
    int deleteMessageByAdminMessageId(String adminMessageId);
    AdminMessage selectAdminMessageByAdminMessageId(String adminMessageId);
    Message selectMessageByAdminMessageId(String adminMessageId);
    List<CheckAgainActivity>selectUndealMessageByAdminId(String adminId);
    List<CheckAgainActivity>selectFinishedMessageByAdminId(String adminId);
    int setMessageHasReaded(String adminMessageId);
    AdminMessage selectAdminMessageByMessageId(String messgeId);
    int deleteAdminMessageByMessageId(String messageId);
}
