package com.bishe.dao;

import com.bishe.dto.StudentMessageResult;
import com.bishe.pojo.Activity;
import com.bishe.pojo.StudentMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by ZYL on 2019/3/19.
 */
@Mapper
public interface StudentMessageDao {
    int addAMessageToStudent(StudentMessage studentMessage);
    List<StudentMessage> selectAllUnreadMessageByStudId(String studId);
    List<StudentMessageResult> selectAllMessageByStudId(String studId);
    int changeMessageStatusByStudId(String studId);
    int deleteMessageByMessageId(String studMessageId);
    Activity selectActivityByStudMessageId(String studMessageId);
}
