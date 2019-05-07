package com.bishe.service;

import com.bishe.dto.RemindMessage;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ZYL on 2019/4/11.
 */
public interface RemindService {
   Boolean addARemindMessage(@Param("actiId")String actiId,@Param("remindComment")String remindComment);
    PageInfo<RemindMessage> selectAllRemindMessageByAdminId(String adminId);
   Boolean deleteRemindByRemindId(String remindId);

}
