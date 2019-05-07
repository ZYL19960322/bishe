package com.bishe.dao;


import com.bishe.pojo.ActivityStatus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by ZYL on 2019/2/7.
 */
@Mapper
public interface ActivityStatusDao {
    List<ActivityStatus> findAllActivityStatus();
}
