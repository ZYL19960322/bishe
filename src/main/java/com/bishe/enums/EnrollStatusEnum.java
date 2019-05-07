package com.bishe.enums;

/**
 * Created by ZYL on 2019/2/13.
 */
public enum EnrollStatusEnum {
    //活动状态=报名中
    //1、是否已经报名：    是——>（取消报名，更改报名信息）
    //                    否——>
    //                    否——>(我要报名)
    //2、报名人数是否已满：是——>(活动报名人数已满)

    //活动状态=筹办中/举办中
    // 1、判断是否自己发布的：是——>(这是您发布的活动) 否——>(活动在筹办中)(活动在举办中)

    //活动状态=已结束（活动已结束）

    STATUS1(1,"取消报名、更改报名信息"),STATUS2(2,"我要报名"),STATUS3(3,"活动报名人数已满"),
    STATUS4(4,"这是您发布的活动"), STATUS5(5,"活动在筹办中"), STATUS6(6,"活动在举办中"),STATUS7(7,"活动已结束");

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

    EnrollStatusEnum(int statusId, String statusDesc) {
        this.statusId = statusId;
        this.statusDesc = statusDesc;
    }

    public static EnrollStatusEnum stateOf(int index) {
        for (EnrollStatusEnum state : values()) {
            if (state.getStatusId() == index) {
                return state;
            }
        }
        return null;
    }


}
