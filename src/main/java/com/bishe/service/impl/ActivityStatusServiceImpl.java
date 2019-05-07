package com.bishe.service.impl;

import com.bishe.dao.ActivityStatusDao;
import com.bishe.pojo.ActivityStatus;
import com.bishe.service.ActivityStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ZYL on 2019/2/7.
 */
@Service
@Transactional
public class ActivityStatusServiceImpl implements ActivityStatusService {
    @Autowired
    ActivityStatusDao activityStatusDao;
    @Override
    public List<ActivityStatus> findAllActivityStatus() {
        return activityStatusDao.findAllActivityStatus() ;
    }
}
