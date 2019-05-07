package com.bishe.service.impl;

import com.bishe.dao.PreviewActivityDao;
import com.bishe.pojo.Activity;
import com.bishe.service.PreviewActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ZYL on 2019/2/21.
 */
@Service
@Transactional
public class PreviewActivityServiceImpl implements PreviewActivityService {
    @Autowired
    PreviewActivityDao previewActivityDao;

    @Override
    public void storePreviewActivity(Activity activity) {
        previewActivityDao.storePreviewActivity(activity);
    }

    @Override
    public Activity selectPreviewActivityByStudId(String studId) {
        return previewActivityDao.selectPreviewActivityByStudId(studId);
    }

    @Override
    public void deletePreviewActivityByStudId(String studId) {
        previewActivityDao.deletePreviewActivityByStudId(studId);
    }
}
