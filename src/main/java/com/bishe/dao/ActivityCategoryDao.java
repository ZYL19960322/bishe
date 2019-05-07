package com.bishe.dao;


import com.bishe.pojo.ActivityCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by ZYL on 2019/2/7.
 */
@Mapper
public interface ActivityCategoryDao {
    List<ActivityCategory> findAllActivityCategory();
}
