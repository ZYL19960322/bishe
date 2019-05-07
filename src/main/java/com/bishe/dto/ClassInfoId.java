package com.bishe.dto;

/**
 * Created by ZYL on 2019/3/12.
 */
public class ClassInfoId {
    private String gradeId;
    private String departmentId;
    private String classId;

    public ClassInfoId(String gradeId) {
        this.gradeId = gradeId;
    }

    public ClassInfoId(String gradeId, String departmentId, String classId) {
        this.gradeId = gradeId;
        this.departmentId = departmentId;
        this.classId = classId;
    }

    public ClassInfoId() {
    }

    public ClassInfoId(String departmentId, String classId) {
        this.departmentId = departmentId;
        this.classId = classId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Override
    public String toString() {
        return "ClassInfoId{" +
                "gradeId='" + gradeId + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", classId='" + classId + '\'' +
                '}';
    }
}
