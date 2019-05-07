package com.bishe.pojo;

/**
 * Created by ZYL on 2019/3/17.
 */
public class Message {
    private String messageId;
    private String messageActiId;
    private String messageBuilderId;
    private String messageCreateTime;
    //活动的原始状态
    private Integer messageActiStatus;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageActiId() {
        return messageActiId;
    }

    public void setMessageActiId(String messageActiId) {
        this.messageActiId = messageActiId;
    }

    public String getMessageBuilderId() {
        return messageBuilderId;
    }

    public void setMessageBuilderId(String messageBuilderId) {
        this.messageBuilderId = messageBuilderId;
    }

    public String getMessageCreateTime() {
        return messageCreateTime;
    }

    public void setMessageCreateTime(String messageCreateTime) {
        this.messageCreateTime = messageCreateTime;
    }

    public Integer getMessageActiStatus() {
        return messageActiStatus;
    }

    public void setMessageActiStatus(Integer messageActiStatus) {
        this.messageActiStatus = messageActiStatus;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", messageActiId='" + messageActiId + '\'' +
                ", messageBuilderId='" + messageBuilderId + '\'' +
                ", messageCreateTime='" + messageCreateTime + '\'' +
                ", messageActiStatus='" + messageActiStatus + '\'' +
                '}';
    }
}
