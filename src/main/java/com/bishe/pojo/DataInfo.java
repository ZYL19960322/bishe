package com.bishe.pojo;

/**
 * Created by ZYL on 2019/2/13.
 */
public class DataInfo {
    private String dataId;
    private String dataActiId;
    private String dataStudId;
    private String dataCreateTime;
    private Integer dataRelationId;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getDataActiId() {
        return dataActiId;
    }

    public void setDataActiId(String dataActiId) {
        this.dataActiId = dataActiId;
    }

    public String getDataStudId() {
        return dataStudId;
    }

    public void setDataStudId(String dataStudId) {
        this.dataStudId = dataStudId;
    }

    public String getDataCreateTime() {
        return dataCreateTime;
    }

    public void setDataCreateTime(String dataCreateTime) {
        this.dataCreateTime = dataCreateTime;
    }

    public Integer getDataRelationId() {
        return dataRelationId;
    }

    public void setDataRelationId(Integer dataRelationId) {
        this.dataRelationId = dataRelationId;
    }

    @Override
    public String toString() {
        return "DataInfo{" +
                "dataId='" + dataId + '\'' +
                ", dataActiId='" + dataActiId + '\'' +
                ", dataStudId='" + dataStudId + '\'' +
                ", dataCreateTime='" + dataCreateTime + '\'' +
                ", dataRelationId=" + dataRelationId +
                '}';
    }
}
