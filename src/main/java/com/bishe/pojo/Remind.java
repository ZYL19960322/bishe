package com.bishe.pojo;

/**
 * Created by ZYL on 2019/4/11.
 */
public class Remind {
    private String remindId;
    private String remindAdminId;
    private String remindActiId;
    private String messageId;
    private String remindComment;
    private String remindCreateTime;
    private Integer remindIfDeal;

    public String getRemindId() {
        return remindId;
    }

    public void setRemindId(String remindId) {
        this.remindId = remindId;
    }

    public String getRemindAdminId() {
        return remindAdminId;
    }

    public void setRemindAdminId(String remindAdminId) {
        this.remindAdminId = remindAdminId;
    }

    public String getRemindActiId() {
        return remindActiId;
    }

    public void setRemindActiId(String remindActiId) {
        this.remindActiId = remindActiId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getRemindComment() {
        return remindComment;
    }

    public void setRemindComment(String remindComment) {
        this.remindComment = remindComment;
    }

    public String getRemindCreateTime() {
        return remindCreateTime;
    }

    public void setRemindCreateTime(String remindCreateTime) {
        this.remindCreateTime = remindCreateTime;
    }

    public Integer getRemindIfDeal() {
        return remindIfDeal;
    }

    public void setRemindIfDeal(Integer remindIfDeal) {
        this.remindIfDeal = remindIfDeal;
    }

    @Override
    public String toString() {
        return "Remind{" +
                "remindId='" + remindId + '\'' +
                ", remindAdminId='" + remindAdminId + '\'' +
                ", remindActiId='" + remindActiId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", remindComment='" + remindComment + '\'' +
                ", remindCreateTime='" + remindCreateTime + '\'' +
                ", remindIfDeal=" + remindIfDeal +
                '}';
    }
}
