package com.bishe.pojo;

/**
 * Created by ZYL on 2019/3/18.
 */
public class AdminMessage {
    //消息的基本信息
    private String adminMessageId;
    private String adminId;
    private String messageId;
    private Integer adminMessageStatus;
    private String adminMessageCreateTime;
    //消息被处理的信息
    private Integer adminMessageIfDeal;
    private String adminMessageDealerId;
    private String adminMessageDealTime;
    //活动是初审核还是再审核
    private Integer adminMessageDealAgain;

    public AdminMessage() {
    }

    public String getAdminMessageId() {
        return adminMessageId;
    }

    public void setAdminMessageId(String adminMessageId) {
        this.adminMessageId = adminMessageId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Integer getAdminMessageStatus() {
        return adminMessageStatus;
    }

    public void setAdminMessageStatus(Integer adminMessageStatus) {
        this.adminMessageStatus = adminMessageStatus;
    }

    public String getAdminMessageCreateTime() {
        return adminMessageCreateTime;
    }

    public void setAdminMessageCreateTime(String adminMessageCreateTime) {
        this.adminMessageCreateTime = adminMessageCreateTime;
    }

    public Integer getAdminMessageIfDeal() {
        return adminMessageIfDeal;
    }

    public void setAdminMessageIfDeal(Integer adminMessageIfDeal) {
        this.adminMessageIfDeal = adminMessageIfDeal;
    }

    public String getAdminMessageDealerId() {
        return adminMessageDealerId;
    }

    public void setAdminMessageDealerId(String adminMessageDealerId) {
        this.adminMessageDealerId = adminMessageDealerId;
    }

    public String getAdminMessageDealTime() {
        return adminMessageDealTime;
    }

    public void setAdminMessageDealTime(String adminMessageDealTime) {
        this.adminMessageDealTime = adminMessageDealTime;
    }

    public Integer getAdminMessageDealAgain() {
        return adminMessageDealAgain;
    }

    public void setAdminMessageDealAgain(Integer adminMessageDealAgain) {
        this.adminMessageDealAgain = adminMessageDealAgain;
    }

    @Override
    public String toString() {
        return "AdminMessage{" +
                "adminMessageId='" + adminMessageId + '\'' +
                ", adminId='" + adminId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", adminMessageStatus=" + adminMessageStatus +
                ", adminMessageCreateTime='" + adminMessageCreateTime + '\'' +
                ", adminMessageIfDeal=" + adminMessageIfDeal +
                ", adminMessageDealerId='" + adminMessageDealerId + '\'' +
                ", adminMessageDealTime='" + adminMessageDealTime + '\'' +
                ", adminMessageDealAgain=" + adminMessageDealAgain +
                '}';
    }
}
