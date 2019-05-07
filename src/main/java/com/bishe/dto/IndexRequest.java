package com.bishe.dto;

/**
 * Created by ZYL on 2019/2/7.
 */
public class IndexRequest {
    private String actiCategory;
    private String actiStatus;
    private String actiStartEnrollTime;
    private String actiEndEnrollTime;

    public String getActiCategory() {
        return actiCategory;
    }

    public void setActiCategory(String actiCategory) {
        this.actiCategory = actiCategory;
    }

    public String getActiStatus() {
        return actiStatus;
    }

    public void setActiStatus(String actiStatus) {
        this.actiStatus = actiStatus;
    }

    public String getActiStartEnrollTime() {
        return actiStartEnrollTime;
    }

    public void setActiStartEnrollTime(String actiStartEnrollTime) {
        this.actiStartEnrollTime = actiStartEnrollTime;
    }

    public String getActiEndEnrollTime() {
        return actiEndEnrollTime;
    }

    public void setActiEndEnrollTime(String actiEndEnrollTime) {
        this.actiEndEnrollTime = actiEndEnrollTime;
    }

    @Override
    public String toString() {
        return "IndexRequest{" +
                "actiCategory='" + actiCategory + '\'' +
                ", actiStatus='" + actiStatus + '\'' +
                ", actiStartEnrollTime='" + actiStartEnrollTime + '\'' +
                ", actiEndEnrollTime='" + actiEndEnrollTime + '\'' +
                '}';
    }
}
