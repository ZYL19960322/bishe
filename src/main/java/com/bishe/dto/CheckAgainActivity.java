package com.bishe.dto;

import com.bishe.pojo.Activity;

/**
 * Created by ZYL on 2019/4/7.
 */
public class CheckAgainActivity {
    private String actiId;

    //即从sessionq取出的学生
    private String actiBuilderId;
    //活动权重
    private String actiLevel;
    //活动主题
    private String actiTheme;
    private String actiDesc;
    private String actiAddress;
    private String actiExtraInfo;
    private String actiComment;
    private Integer actiCategoryId;
    private String actiCategoryName;
    //活动状态：已在数据库中默认为1，表示审核中
    private Integer actiStatusId;
    private String actiStatusName;
    private String actiPhone;
    //活动主题图片，存储的是的图片的相对地址+文件名+后缀
    private String actiThemeImage;
    private String actiHolder;
    //当活动结束时才设置值
    private String actiEndTime;
    private String actiStartTime;
    //活动建立时间，即当前时间
    private String actiBuildTime;
    private String actiEndrollTime;
    private String actiChangeTime;
    private Integer actiMaxEnroll;
    private Integer actiNowEnroll;
    private String adminMessageId;



    public String getAdminMessageId() {
        return adminMessageId;
    }

    public void setAdminMessageId(String adminMessageId) {
        this.adminMessageId = adminMessageId;
    }

    public String getActiId() {
        return actiId;
    }

    public void setActiId(String actiId) {
        this.actiId = actiId;
    }

    public String getActiBuilderId() {
        return actiBuilderId;
    }

    public void setActiBuilderId(String actiBuilderId) {
        this.actiBuilderId = actiBuilderId;
    }

    public String getActiLevel() {
        return actiLevel;
    }

    public void setActiLevel(String actiLevel) {
        this.actiLevel = actiLevel;
    }

    public String getActiTheme() {
        return actiTheme;
    }

    public void setActiTheme(String actiTheme) {
        this.actiTheme = actiTheme;
    }

    public String getActiDesc() {
        return actiDesc;
    }

    public void setActiDesc(String actiDesc) {
        this.actiDesc = actiDesc;
    }

    public String getActiAddress() {
        return actiAddress;
    }

    public void setActiAddress(String actiAddress) {
        this.actiAddress = actiAddress;
    }

    public String getActiExtraInfo() {
        return actiExtraInfo;
    }

    public void setActiExtraInfo(String actiExtraInfo) {
        this.actiExtraInfo = actiExtraInfo;
    }

    public String getActiComment() {
        return actiComment;
    }

    public void setActiComment(String actiComment) {
        this.actiComment = actiComment;
    }

    public Integer getActiCategoryId() {
        return actiCategoryId;
    }

    public void setActiCategoryId(Integer actiCategoryId) {
        this.actiCategoryId = actiCategoryId;
    }

    public String getActiCategoryName() {
        return actiCategoryName;
    }

    public void setActiCategoryName(String actiCategoryName) {
        this.actiCategoryName = actiCategoryName;
    }

    public Integer getActiStatusId() {
        return actiStatusId;
    }

    public void setActiStatusId(Integer actiStatusId) {
        this.actiStatusId = actiStatusId;
    }

    public String getActiStatusName() {
        return actiStatusName;
    }

    public void setActiStatusName(String actiStatusName) {
        this.actiStatusName = actiStatusName;
    }

    public String getActiPhone() {
        return actiPhone;
    }

    public void setActiPhone(String actiPhone) {
        this.actiPhone = actiPhone;
    }

    public String getActiThemeImage() {
        return actiThemeImage;
    }

    public void setActiThemeImage(String actiThemeImage) {
        this.actiThemeImage = actiThemeImage;
    }

    public String getActiHolder() {
        return actiHolder;
    }

    public void setActiHolder(String actiHolder) {
        this.actiHolder = actiHolder;
    }

    public String getActiEndTime() {
        return actiEndTime;
    }

    public void setActiEndTime(String actiEndTime) {
        this.actiEndTime = actiEndTime;
    }

    public String getActiStartTime() {
        return actiStartTime;
    }

    public void setActiStartTime(String actiStartTime) {
        this.actiStartTime = actiStartTime;
    }

    public String getActiBuildTime() {
        return actiBuildTime;
    }

    public void setActiBuildTime(String actiBuildTime) {
        this.actiBuildTime = actiBuildTime;
    }

    public String getActiEndrollTime() {
        return actiEndrollTime;
    }

    public void setActiEndrollTime(String actiEndrollTime) {
        this.actiEndrollTime = actiEndrollTime;
    }

    public String getActiChangeTime() {
        return actiChangeTime;
    }

    public void setActiChangeTime(String actiChangeTime) {
        this.actiChangeTime = actiChangeTime;
    }

    public Integer getActiMaxEnroll() {
        return actiMaxEnroll;
    }

    public void setActiMaxEnroll(Integer actiMaxEnroll) {
        this.actiMaxEnroll = actiMaxEnroll;
    }

    public Integer getActiNowEnroll() {
        return actiNowEnroll;
    }

    public void setActiNowEnroll(Integer actiNowEnroll) {
        this.actiNowEnroll = actiNowEnroll;
    }

    @Override
    public String toString() {
        return "CheckAgainActivity{" +
                "actiId='" + actiId + '\'' +
                ", actiBuilderId='" + actiBuilderId + '\'' +
                ", actiLevel='" + actiLevel + '\'' +
                ", actiTheme='" + actiTheme + '\'' +
                ", actiDesc='" + actiDesc + '\'' +
                ", actiAddress='" + actiAddress + '\'' +
                ", actiExtraInfo='" + actiExtraInfo + '\'' +
                ", actiComment='" + actiComment + '\'' +
                ", actiCategoryId=" + actiCategoryId +
                ", actiCategoryName='" + actiCategoryName + '\'' +
                ", actiStatusId=" + actiStatusId +
                ", actiStatusName='" + actiStatusName + '\'' +
                ", actiPhone='" + actiPhone + '\'' +
                ", actiThemeImage='" + actiThemeImage + '\'' +
                ", actiHolder='" + actiHolder + '\'' +
                ", actiEndTime='" + actiEndTime + '\'' +
                ", actiStartTime='" + actiStartTime + '\'' +
                ", actiBuildTime='" + actiBuildTime + '\'' +
                ", actiEndrollTime='" + actiEndrollTime + '\'' +
                ", actiChangeTime='" + actiChangeTime + '\'' +
                ", actiMaxEnroll=" + actiMaxEnroll +
                ", actiNowEnroll=" + actiNowEnroll +
                ", adminMessageId='" + adminMessageId + '\'' +
                '}';
    }
}
