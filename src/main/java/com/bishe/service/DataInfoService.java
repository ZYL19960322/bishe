package com.bishe.service;

import com.bishe.dto.UserResponse;
import com.bishe.pojo.DataInfo;

import java.util.List;

/**
 * Created by ZYL on 2019/2/13.
 */
public interface DataInfoService {
    void addDataInfo (DataInfo dataInfo);
    List<String> selectStudIdListByActiId(String actiId);
    UserResponse toUserPage(String studId);
    int deleteEnroll(String actiId,String studId );
    Boolean deleteEnrollRecord(String actiId,String studId );

}
