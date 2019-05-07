package com.bishe.service;

import com.bishe.pojo.Activity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by ZYL on 2019/2/21.
 */
public interface PreviewActivityService {
    void storePreviewActivity(Activity activity);
    Activity selectPreviewActivityByStudId(String studId);
    void deletePreviewActivityByStudId(String studId);
}
