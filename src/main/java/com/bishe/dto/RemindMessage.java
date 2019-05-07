package com.bishe.dto;

/**
 * Created by ZYL on 2019/4/12.
 */
public class RemindMessage {
    private String remindId;

    //活动id主题(activity表)
    private String actiId;
    private String actiTheme;
    //上次申请时间(message表中)
    private String messageCreateTime;
    //remind表
    //本次申请时间
    private String remindCreateTime;
    private String remindComment;
    private Integer remindIfDeal;
    //student表
    private String studName;
    //学生班级ID
    private Integer studClassID;

    //新定义
    //根据是否已处理的0、1转成前端用来判断的flase、true
    private Boolean dealFlag;
    //班级信息(格式：15+电信工程4班) 根据student表的classId再次查询和封装
    private String classInfo;

    //审核时的消息状态：新审核、再审核、未处理、已完成、已被删除
    private String beforeMessageStatus;

    public String getActiTheme() {
        return actiTheme;
    }

    public void setActiTheme(String actiTheme) {
        this.actiTheme = actiTheme;
    }

    public String getMessageCreateTime() {
        return messageCreateTime;
    }

    public void setMessageCreateTime(String messageCreateTime) {
        this.messageCreateTime = messageCreateTime;
    }

    public String getRemindCreateTime() {
        return remindCreateTime;
    }

    public void setRemindCreateTime(String remindCreateTime) {
        this.remindCreateTime = remindCreateTime;
    }

    public String getRemindComment() {
        return remindComment;
    }

    public void setRemindComment(String remindComment) {
        this.remindComment = remindComment;
    }

    public Integer getRemindIfDeal() {
        return remindIfDeal;
    }

    public void setRemindIfDeal(Integer remindIfDeal) {
        this.remindIfDeal = remindIfDeal;
    }

    public String getStudName() {
        return studName;
    }

    public void setStudName(String studName) {
        this.studName = studName;
    }

    public Integer getStudClassID() {
        return studClassID;
    }

    public void setStudClassID(Integer studClassID) {
        this.studClassID = studClassID;
    }

    public Boolean getDealFlag() {
        return dealFlag;
    }

    public void setDealFlag(Boolean dealFlag) {
        this.dealFlag = dealFlag;
    }

    public String getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(String classInfo) {
        this.classInfo = classInfo;
    }

    public String getActiId() {
        return actiId;
    }

    public void setActiId(String actiId) {
        this.actiId = actiId;
    }

    public String getRemindId() {
        return remindId;
    }

    public void setRemindId(String remindId) {
        this.remindId = remindId;
    }

    public String getBeforeMessageStatus() {
        return beforeMessageStatus;
    }

    public void setBeforeMessageStatus(String beforeMessageStatus) {
        this.beforeMessageStatus = beforeMessageStatus;
    }


    @Override
    public String toString() {
        return "RemindMessage{" +
                "remindId='" + remindId + '\'' +
                ", actiId='" + actiId + '\'' +
                ", actiTheme='" + actiTheme + '\'' +
                ", messageCreateTime='" + messageCreateTime + '\'' +
                ", remindCreateTime='" + remindCreateTime + '\'' +
                ", remindComment='" + remindComment + '\'' +
                ", remindIfDeal=" + remindIfDeal +
                ", studName='" + studName + '\'' +
                ", studClassID=" + studClassID +
                ", dealFlag=" + dealFlag +
                ", classInfo='" + classInfo + '\'' +
                ", beforeMessageStatus='" + beforeMessageStatus + '\'' +
                '}';
    }
}
