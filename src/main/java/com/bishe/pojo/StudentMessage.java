package com.bishe.pojo;

/**
 * Created by ZYL on 2019/3/19.
 */
public class StudentMessage {
    private String studMessageId;
    private String studId;
    //学生申请发布时生成的消息id
    private String messageId;
    //学生消息的是否已读的状态
    private Integer studMessageStatus;
    private String studMessageCreateTime;
    //学生消息的具体内容可以是审核通过由枚举型产生的消息也可以说是管理员提交上来的消息
    private String studMessageComment;

    public StudentMessage() {
    }

    public String getStudMessageId() {
        return studMessageId;
    }

    public void setStudMessageId(String studMessageId) {
        this.studMessageId = studMessageId;
    }

    public String getStudId() {
        return studId;
    }

    public void setStudId(String studId) {
        this.studId = studId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Integer getStudMessageStatus() {
        return studMessageStatus;
    }

    public void setStudMessageStatus(Integer studMessageStatus) {
        this.studMessageStatus = studMessageStatus;
    }

    public String getStudMessageCreateTime() {
        return studMessageCreateTime;
    }

    public void setStudMessageCreateTime(String studMessageCreateTime) {
        this.studMessageCreateTime = studMessageCreateTime;
    }

    public String getStudMessageComment() {
        return studMessageComment;
    }

    public void setStudMessageComment(String studMessageComment) {
        this.studMessageComment = studMessageComment;
    }

    @Override
    public String toString() {
        return "StudentMessage{" +
                "studMessageId='" + studMessageId + '\'' +
                ", studId='" + studId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", studMessageStatus=" + studMessageStatus +
                ", studMessageCreateTime='" + studMessageCreateTime + '\'' +
                ", studMessageComment='" + studMessageComment + '\'' +
                '}';
    }
}
