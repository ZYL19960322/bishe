package com.bishe.service;

import com.bishe.dto.EnrollStudent;
import com.bishe.dto.IndexRequest;
import com.bishe.dto.PublishResponse;
import com.bishe.dto.UserResponse;
import com.bishe.pojo.Activity;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ZYL on 2019/1/29.
 */
public interface ActivityService {

    Integer studPublishActivity (Activity activity);
    List<Activity> findAllActivity();
    List<Activity> indexSelect(IndexRequest indexRequest);
    List<Activity> indexSearch( String searchCondition);
    List<Activity> indexRecommend();
    List<Activity> indexHot();
    Activity selectActivityById(String actiId);
    void addEnrollStudent(String actiId);

    List<Activity>selectActivityListBySign(int sign,String studId);
    UserResponse toPublishPage(String studId);
    int deletePublishById(String actiId);
    int  changeActivityThemeImage( String actiId,String actiThemeImage);
    int changeActivityInfo(Activity activity);
    PublishResponse checkActivityInfo(Activity activity);

    //admin
    PageInfo<Activity> pageShowByStatusId(int pageNum,String statusId);
    PageInfo<Activity> selectActivityListByStatusId(String statusId);
    int changeLevelByActivityId(@Param("changeLevel") String changeLevel,@Param("actiId") String actiId);


}
