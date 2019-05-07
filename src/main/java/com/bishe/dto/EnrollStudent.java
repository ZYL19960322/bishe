package com.bishe.dto;

import java.util.Date;

/**
 * Created by ZYL on 2019/4/11.
 */
public class EnrollStudent {
    private String studId;
    private String studEnrollName;
    //学生学号
    private String studNumber;
    //学生班级ID
    private Integer studClassID;
    //封装后的班级信息(15电信工程4班：年级+专业班级)
    private String classInfo;


    public Integer getStudClassID() {
        return studClassID;
    }

    public void setStudClassID(Integer studClassID) {
        this.studClassID = studClassID;
    }

    public String getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(String classInfo) {
        this.classInfo = classInfo;
    }

    public String getStudId() {
        return studId;
    }

    public void setStudId(String studId) {
        this.studId = studId;
    }

    public String getStudEnrollName() {
        return studEnrollName;
    }

    public void setStudEnrollName(String studEnrollName) {
        this.studEnrollName = studEnrollName;
    }

    public String getStudNumber() {
        return studNumber;
    }

    public void setStudNumber(String studNumber) {
        this.studNumber = studNumber;
    }

    @Override
    public String toString() {
        return "EnrollStudent{" +
                "studId='" + studId + '\'' +
                ", studEnrollName='" + studEnrollName + '\'' +
                ", studNumber='" + studNumber + '\'' +
                ", studClassID=" + studClassID +
                ", classInfo='" + classInfo + '\'' +
                '}';
    }
}
