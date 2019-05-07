package com.bishe.service.impl;

import com.bishe.dao.ActivityDao;
import com.bishe.dao.StudentDao;
import com.bishe.dto.*;
import com.bishe.enums.ActivityStatusEnum;
import com.bishe.pojo.Activity;
import com.bishe.service.ActivityService;
import com.bishe.utils.TimeUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by ZYL on 2019/1/29.
 */
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    ActivityDao activityDao;

    @Override
    public Integer studPublishActivity(Activity activity) {
        return activityDao.studPublishActivity(activity);
    }

    //按照时间排序查询出所有的活动显示在首页
    @Override
    public List<Activity> findAllActivity() {
        return activityDao.findAllActivity();
    }

    @Override
    public List<Activity> indexSelect(IndexRequest indexRequest) {
        return activityDao.indexSelect(indexRequest);
    }

    @Override
    public List<Activity> indexSearch(String searchCondition) {
        return activityDao.indexSearch(searchCondition);
    }

    @Override
    public List<Activity> indexRecommend() {
        return activityDao.indexRecommend();
    }

    @Override
    public List<Activity> indexHot() {
        return activityDao.indexHot();
    }

    @Override
    public Activity selectActivityById(String actiId) {
        return activityDao.selectActivityById(actiId);
    }

    @Override
    public void addEnrollStudent(String actiId) {
        activityDao.addEnrollStudent(actiId);
    }


    @Override
    public List<Activity> selectActivityListBySign(int sign,String studId) {
        //根据sign的值查询不同状态的活动
        //去user.html时显示的
        // =0时:查询学生的所有报名了的活动
        // =1时:查询学生的所有报名了的在筹办中、举办中的活动
        // =2时:查询学生的所有报名了的已结束的活动
        // =8时：查询学生的所有报名的但申请了修改还是审核中的(新增)
        if(sign==0){
            return activityDao.selectEnrollingActivityList(studId);
        }if(sign==1){
            return activityDao.selectDuringActivityList(studId);
        }if(sign==2){
            return activityDao.selectEndActivityList(studId);
        }
        if(sign==8){
            return activityDao.selectCheckingActivityList(studId);
        }
        //去publish.html时显示的
        //=3时:查询学生自己发布的审核中的活动
        //=4时:查询学生自己发布的报名中的活动
        //=5时:查询学生自己发布的筹办中的活动
        //=6时:查询学生自己发布的举办中的活动
        //=7时:查询学生自己发布的已结束中的活动
        if(sign==3){
            return activityDao.selectActivityListByStatus(studId,ActivityStatusEnum.ACTIVITY_CHECKING.getStatusId());
        }
        if(sign==4){
            return activityDao.selectActivityListByStatus(studId,ActivityStatusEnum.ACTIVITY_ENROLLING.getStatusId());
        }
        if(sign==5){
            return activityDao.selectActivityListByStatus(studId,ActivityStatusEnum.ACTIVITY_PREPARING.getStatusId());
        }
        if(sign==6){
            return activityDao.selectActivityListByStatus(studId,ActivityStatusEnum.ACTIVITY_HOLDING.getStatusId());
        }if(sign==7){
            return activityDao.selectActivityListByStatus(studId,ActivityStatusEnum.ACTIVITY_HASENDED.getStatusId());
        }
        return null;
    }

    @Override
    public UserResponse toPublishPage(String studId) {
        UserResponse userResponse = new UserResponse();
        //根据学生id和活动的状态id查询出学生发布的活动处于该状态的活动数量
        userResponse.setCheckingCount(activityDao.selectActivityCountByStatus(studId, ActivityStatusEnum.ACTIVITY_CHECKING.getStatusId()));
        userResponse.setEnrollingCount(activityDao.selectActivityCountByStatus(studId,ActivityStatusEnum.ACTIVITY_ENROLLING.getStatusId()));
        userResponse.setPreparingCount(activityDao.selectActivityCountByStatus(studId,ActivityStatusEnum.ACTIVITY_PREPARING.getStatusId()));
        userResponse.setHoldingCount(activityDao.selectActivityCountByStatus(studId,ActivityStatusEnum.ACTIVITY_HOLDING.getStatusId()));
        userResponse.setHasEndedCount(activityDao.selectActivityCountByStatus(studId,ActivityStatusEnum.ACTIVITY_HASENDED.getStatusId()));
        return userResponse;
    }

    @Override
    public int deletePublishById(String actiId) {
        return activityDao.deletePublishById(actiId);
    }

    @Override
    public int changeActivityThemeImage(String actiId, String actiThemeImage) {
        return activityDao.changeActivityThemeImage(actiId,actiThemeImage, TimeUtils.getStringDate());
    }

    @Override
    public int changeActivityInfo(Activity activity) {
        return activityDao.changeActivityInfo(activity);
    }

    @Override
    public PublishResponse checkActivityInfo(Activity activity) {
        PublishResponse publishResponse = new PublishResponse();

        if (activity.getActiTheme().isEmpty() || activity.getActiDesc().isEmpty() || activity.getActiAddress().isEmpty() || activity.getActiComment().isEmpty() || activity.getActiCategoryId() == null || activity.getActiPhone().isEmpty() || (activity.getActiPhone().length() !=6 & activity.getActiPhone().length()!=11) ||
                activity.getActiHolder().isEmpty() || activity.getActiStartTime().isEmpty() || activity.getActiEndrollTime().isEmpty() || activity.getActiMaxEnroll() == null || activity.getActiCategoryId()==0) {
            //如果必填的信息填写不完全则返回并且带上失败信息
            publishResponse.setSuccess(false);
            publishResponse.setMsg("请检查必填参数是否填写完全");
            return publishResponse;
        }
        //信息合理
        publishResponse.setSuccess(true);
        return publishResponse;
    }

//==================后台========================

    @Override
    public PageInfo<Activity> pageShowByStatusId(int pageNum,String statusId ) {
        PageHelper.startPage(pageNum,AdminPage.pageSize);
        List<Activity> activityList = activityDao.selectActivityListByStatusId(statusId);
        PageInfo<Activity> activityPageInfo = new PageInfo<>(activityList);
        return  activityPageInfo;
    }
    @Override
    public PageInfo<Activity> selectActivityListByStatusId(String statusId) {
        PageHelper.startPage(AdminPage.pageNum,AdminPage.pageSize);
        List<Activity> activityList = activityDao.selectActivityListByStatusId(statusId);
        PageInfo<Activity> activityPageInfo = new PageInfo<>(activityList);
        return  activityPageInfo;
    }

    @Override
    public int changeLevelByActivityId(String changeLevel, String actiId) {
        String actiChangeTime=TimeUtils.getStringDate();
        return activityDao.changeLevelByActivityId(changeLevel,actiChangeTime,actiId);
    }

}
