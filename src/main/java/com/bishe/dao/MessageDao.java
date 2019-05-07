package com.bishe.dao;

import com.bishe.pojo.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by ZYL on 2019/3/18.
 */
@Mapper
public interface MessageDao {
     void addAMessage(Message message);
     Message selectMessageByActiId(String actiId);
     int  changeMessageByMessageId(Message message);
     Message selectMessageByRemindId(String remindId);


}
