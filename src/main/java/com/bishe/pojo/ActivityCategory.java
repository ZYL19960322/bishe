package com.bishe.pojo;

/**
 * Created by ZYL on 2019/1/29.
 */
public class ActivityCategory {
    private Integer actiCategoryId;
    private String actiCategoryName;

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

    @Override
    public String toString() {
        return "ActivityCategory{" +
                "actiCategoryId=" + actiCategoryId +
                ", actiCategoryName='" + actiCategoryName + '\'' +
                '}';
    }
}
