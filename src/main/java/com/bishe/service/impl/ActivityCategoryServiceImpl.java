package com.bishe.service.impl;

import com.bishe.dao.ActivityCategoryDao;
import com.bishe.pojo.ActivityCategory;
import com.bishe.service.ActivityCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ZYL on 2019/2/7.
 */
@Service
@Transactional
public class ActivityCategoryServiceImpl implements ActivityCategoryService{
    @Autowired
    ActivityCategoryDao activityCategoryDao;

    @Override
    public List<ActivityCategory> findAllActivityCategory() {
        return activityCategoryDao.findAllActivityCategory();
    }
}
