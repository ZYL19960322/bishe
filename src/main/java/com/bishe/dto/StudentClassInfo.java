package com.bishe.dto;

/**
 * Created by ZYL on 2019/2/16.
 */
public class StudentClassInfo {
    //年级
    private Integer studGradeId;
    private String studGradeName;
    //院系
    private Integer studDepartmentId;
    private String studDepartmentName;
    //专业班级
    private Integer studClassId;
    private String studClassName;

    public StudentClassInfo(String studGradeName,String studClassName) {
        this.studGradeName = studGradeName;
        this.studClassName = studClassName;
    }

    public StudentClassInfo() {
    }

    public Integer getStudGradeId() {
        return studGradeId;
    }

    public void setStudGradeId(Integer studGradeId) {
        this.studGradeId = studGradeId;
    }

    public String getStudGradeName() {
        return studGradeName;
    }

    public void setStudGradeName(String studGradeName) {
        this.studGradeName = studGradeName;
    }

    public Integer getStudDepartmentId() {
        return studDepartmentId;
    }

    public void setStudDepartmentId(Integer studDepartmentId) {
        this.studDepartmentId = studDepartmentId;
    }

    public String getStudDepartmentName() {
        return studDepartmentName;
    }

    public void setStudDepartmentName(String studDepartmentName) {
        this.studDepartmentName = studDepartmentName;
    }

    public Integer getStudClassId() {
        return studClassId;
    }

    public void setStudClassId(Integer studClassId) {
        this.studClassId = studClassId;
    }

    public String getStudClassName() {
        return studClassName;
    }

    public void setStudClassName(String studClassName) {
        this.studClassName = studClassName;
    }

    @Override
    public String toString() {
        return "StudentClassInfo{" +
                "studGradeId=" + studGradeId +
                ", studGradeName='" + studGradeName + '\'' +
                ", studDepartmentId=" + studDepartmentId +
                ", studDepartmentName='" + studDepartmentName + '\'' +
                ", studClassId=" + studClassId +
                ", studClassName='" + studClassName + '\'' +
                '}';
    }
}
