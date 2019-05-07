package com.bishe.dao;

import com.bishe.dto.IndexRequest;
import com.bishe.pojo.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ZYL on 2019/1/29.
 */
@Mapper
public interface ActivityDao {

    Integer studPublishActivity (@Param("activity") Activity activity);
    List<Activity> findAllActivity();
    List<Activity> indexSelect( @Param("indexRequest") IndexRequest indexRequest);
    List<Activity> indexSearch( @Param("searchCondition") String searchCondition);
    List<Activity> indexRecommend();
    List<Activity> indexHot();
    Activity selectActivityById(String actiId);
    void addEnrollStudent(String actiId);
    void removeEnrollStudent(String actiId);
    List<Activity> selectCheckingActivityList(String studId);
    List<Activity> selectEnrollingActivityList(String studId);
    List<Activity> selectDuringActivityList(String studId);
    List<Activity> selectEndActivityList(String studId);
    List<Activity> selectActivityListByStatus(@Param("studId")String studId,@Param("statusId") int statusId);
    int selectActivityCountByStatus(@Param("studId")String studId,@Param("statusId") int statusId);
    int deletePublishById(String actiId);
    int  changeActivityThemeImage(@Param("actiId") String actiId,@Param("actiThemeImage")String actiThemeImage,@Param("changeTime")String changeTime);
    int changeActivityInfo(Activity activity);

    //admin
    List<Activity> selectEnrollingActivity();
    List<Activity> selectActivityListByStatusId(String statusId);
    int changeLevelByActivityId(@Param("changeLevel") String changeLevel,@Param("actiChangeTime")String actiChangeTime,@Param("actiId") String actiId);

    String selectBuilderIdByActiId(String actiId);
    int changeActivityStatusByActiId(@Param("actiId")String actiId,@Param("statusId") int statusId);
    String selectActiThemeByActiId(String actiId);
}
