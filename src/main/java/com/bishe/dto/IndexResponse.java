package com.bishe.dto;

import com.bishe.pojo.Activity;
import com.bishe.pojo.ActivityCategory;
import com.bishe.pojo.ActivityStatus;

import java.util.List;

/**
 * Created by ZYL on 2019/1/29.
 */
public class IndexResponse {
    private List<Activity> activityList;
    private List<ActivityCategory> activityCategoryList;
    private List<ActivityStatus> activityStatusList;

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    public List<ActivityCategory> getActivityCategoryList() {
        return activityCategoryList;
    }

    public void setActivityCategoryList(List<ActivityCategory> activityCategoryList) {
        this.activityCategoryList = activityCategoryList;
    }

    public List<ActivityStatus> getActivityStatusList() {
        return activityStatusList;
    }

    public void setActivityStatusList(List<ActivityStatus> activityStatusList) {
        this.activityStatusList = activityStatusList;
    }
}
