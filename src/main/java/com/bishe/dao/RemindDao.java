package com.bishe.dao;

import com.bishe.pojo.Remind;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by ZYL on 2019/4/11.
 */
@Mapper
public interface RemindDao {
    int addARemind(Remind remind);
    Remind selectRemindByActiId(String actiId);
    int deleteRemindByActiId(String actiId);
    List<Remind> selectAllRemindByAdminId(String adminId);
    int deleteRemindByRemindId(String remindId);
    int updateRemindStatusByMessageId(String messageId);
}
