package com.bishe.service;

import com.bishe.dto.StudentMessageResult;
import com.bishe.pojo.Activity;
import com.bishe.pojo.StudentMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ZYL on 2019/3/19.
 */
public interface StudentMessageService {
   Boolean adminCheckActivityPass(@Param("adminMessageId") String adminMessageId);
   List<StudentMessage> selectAllUnreadMessageByStudId(String studId);
   List<StudentMessageResult> selectAllMessageByStudId(String studId);
   int changeMessageStatusByStudId(String studId);
   int deleteMessageByMessageId(String studMessageId);
   Activity selectActivityByStudMessageId(String studMessageId);
   Boolean backMessage(@Param("adminMessageId") String adminMessageId,@Param("messageComment") String messageComment);
   Boolean adminCheckAgainActivityPass(@Param("adminMessageId") String adminMessageId);
   Boolean backMessageByadminMessageId(@Param("adminMessageId") String adminMessageId,@Param("messageComment") String messageComment);
   Boolean checkPassByActiIdAndAdminId(@Param("actiId") String actiId,@Param("adminId") String adminId);
   Boolean backMessageByActidId(@Param("actiId") String actiId,@Param("messageComment") String messageComment,@Param("adminId") String adminId);


}
