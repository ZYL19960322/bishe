package com.bishe.pojo;

/**
 * Created by ZYL on 2019/2/14.
 */
public class StudentClass {
    private Integer parentId;
    private Integer classId;
    private String className;

    public StudentClass() {

    }

    public StudentClass(Integer parentId, Integer classId, String className) {
        this.parentId = parentId;
        this.classId = classId;
        this.className = className;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
