package com.bishe.enums;

/**
 * Created by ZYL on 2019/2/19.
 */
public enum ActivityStatusEnum {
    ACTIVITY_CHECKING(1,"审核中"),ACTIVITY_ENROLLING(2,"报名中"),ACTIVITY_PREPARING(3,"筹办中"),ACTIVITY_HOLDING(4,"举办中"),ACTIVITY_HASENDED(5,"已结束");


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

    ActivityStatusEnum(int statusId, String statusDesc) {
        this.statusId = statusId;
        this.statusDesc = statusDesc;
    }

    public static ActivityStatusEnum stateOf(int index) {
        for (ActivityStatusEnum state : values()) {
            if (state.getStatusId() == index) {
                return state;
            }
        }
        return null;
    }
}
