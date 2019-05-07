package com.bishe.service.impl;

import com.bishe.dao.ActivityDao;
import com.bishe.dao.DataInfoDao;
import com.bishe.dto.UserResponse;
import com.bishe.pojo.DataInfo;
import com.bishe.service.DataInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ZYL on 2019/2/13.
 */
@Service
@Transactional
public class DataInfoServiceImpl implements DataInfoService {
    @Autowired
    DataInfoDao dataInfoDao;
    @Autowired
    ActivityDao activityDao;

    @Override
    public void addDataInfo(DataInfo dataInfo) {
        dataInfoDao.addDataInfo(dataInfo);
    }

    @Override
    public List<String> selectStudIdListByActiId(String actiId) {
        return dataInfoDao.selectStudIdListByActiId(actiId);
    }

    @Override
    public UserResponse toUserPage( String studId ) {
        UserResponse userResponse=new UserResponse();
        userResponse.setCheckCount(dataInfoDao.selectCheckCount(studId));
        userResponse.setEnrolledCount(dataInfoDao.selectEnrolledCount(studId));
        userResponse.setDuringCount(dataInfoDao.selectDuringCount(studId));
        userResponse.setEndCount(dataInfoDao.selectEndCount(studId));
        return userResponse;
    }

    @Override
    public int deleteEnroll(String studId, String actiId) {
        //根据活动id将活动的报名人数减1
        activityDao.removeEnrollStudent(actiId);
        return dataInfoDao.deleteEnroll(actiId ,studId);
    }

    @Override
    public Boolean deleteEnrollRecord(String actiId, String studId) {
        if (dataInfoDao.deleteEnroll(actiId,studId)!=0){
            return true;

        }else {
            return false;
        }
    }
}
