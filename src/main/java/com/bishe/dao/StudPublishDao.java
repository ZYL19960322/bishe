package com.bishe.dao;

import com.bishe.pojo.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ZYL on 2019/1/21.
 */
@Mapper
public interface StudPublishDao {
    Integer studPublishActivity (@Param("activity") Activity activity);
}
