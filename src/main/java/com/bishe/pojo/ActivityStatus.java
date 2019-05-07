package com.bishe.pojo;

/**
 * Created by ZYL on 2019/1/29.
 */
public class ActivityStatus {
    private Integer actiStatusId;
    private String actiStatusName;

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

    @Override
    public String toString() {
        return "ActivityStatus{" +
                "actiStatusId=" + actiStatusId +
                ", actiStatusName='" + actiStatusName + '\'' +
                '}';
    }
}
