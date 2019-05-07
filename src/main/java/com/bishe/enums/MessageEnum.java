package com.bishe.enums;

/**
 * Created by ZYL on 2019/3/20.
 */
public enum MessageEnum {
    CHECK_PASS(1,"您的活动通过了审核"),CHECK_AGAIN(2,"活动信息发生了更改,请检查"),CHECK_AGAIN_PASS(3,"信息发生了更改，现审核成功"),
    CHECK_AGAINING(4,"申请已提交，审核中，请等待");

    private int statusId ;
    private String statusDesc;

    public int getStatusId() {
        return statusId;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    MessageEnum(int statusId, String statusDesc) {
        this.statusId = statusId;
        this.statusDesc = statusDesc;
    }

    public static MessageEnum stateOf(int index) {
        for (MessageEnum state : values()) {
            if (state.getStatusId() == index) {
                return state;
            }
        }
        return null;
    }

}
