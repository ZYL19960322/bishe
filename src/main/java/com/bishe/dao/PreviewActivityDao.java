package com.bishe.dao;

import com.bishe.pojo.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ZYL on 2019/2/21.
 */
@Mapper
public interface PreviewActivityDao {
    void storePreviewActivity(@Param("activity") Activity activity);
    Activity selectPreviewActivityByStudId(String studId);
    void deletePreviewActivityByStudId(String studId);

}
