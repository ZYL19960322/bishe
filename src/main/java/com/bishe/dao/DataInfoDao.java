package com.bishe.dao;

import com.bishe.dto.UserResponse;
import com.bishe.pojo.Activity;
import com.bishe.pojo.DataInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Created by ZYL on 2019/2/13.
 */
@Mapper
public interface DataInfoDao {
    void addDataInfo(DataInfo data);
    List<String> selectStudIdListByActiId(String actiId);
    int selectCheckCount(String studId);
    int selectEnrolledCount(String studId);
    int selectDuringCount(String studId);
    int selectEndCount(String studId);
    int deleteEnroll(@Param("actiId") String actiId, @Param("studId") String studId);


}
