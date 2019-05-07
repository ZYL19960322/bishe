package com.bishe.pojo;

import com.bishe.dto.StudentClassInfo;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * Created by ZYL on 2018/12/12.
 */
public class Student {
    private String studId;
    private String studName;
    private String studPassword;
    //学生状态：默认为启用状态1，停用状态为0
    private Integer studStatus;
    //学生班级ID
    private Integer studClassID;
    //学生学号
    private String studNumber;
    private String studPhone;
    //学生上次查看消息时间
    private String studLoginTime;
    private String studImagePath;
    //学生报名信息中的名字
    private String studEnrollName;

    private StudentClassInfo studentClassInfo;

    public StudentClassInfo getStudentClassInfo() {
        return studentClassInfo;
    }

    public void setStudentClassInfo(StudentClassInfo studentClassInfo) {
        this.studentClassInfo = studentClassInfo;
    }

    public String getStudId() {
        return studId;
    }

    public void setStudId(String studId) {
        this.studId = studId;
    }

    public String getStudName() {
        return studName;
    }

    public void setStudName(String studName) {
        this.studName = studName;
    }

    public String getStudPassword() {
        return studPassword;
    }

    public void setStudPassword(String studPassword) {
        this.studPassword = studPassword;
    }

    public Integer getStudStatus() {
        return studStatus;
    }

    public void setStudStatus(Integer studStatus) {
        this.studStatus = studStatus;
    }

    public Integer getStudClassID() {
        return studClassID;
    }

    public void setStudClassID(Integer studClassID) {
        this.studClassID = studClassID;
    }

    public String getStudNumber() {
        return studNumber;
    }

    public void setStudNumber(String studNumber) {
        this.studNumber = studNumber;
    }

    public String getStudPhone() {
        return studPhone;
    }

    public void setStudPhone(String studPhone) {
        this.studPhone = studPhone;
    }

    public String getStudLoginTime() {
        return studLoginTime;
    }

    public void setStudLoginTime(String studLoginTime) {
        this.studLoginTime = studLoginTime;
    }

    public String getStudImagePath() {
        return studImagePath;
    }

    public void setStudImagePath(String studImagePath) {
        this.studImagePath = studImagePath;
    }

    public String getStudEnrollName() {
        return studEnrollName;
    }

    public void setStudEnrollName(String studEnrollName) {
        this.studEnrollName = studEnrollName;
    }

    public Student() {
    }

    @Override
    public String toString() {
        return "Student{" +
                "studId='" + studId + '\'' +
                ", studName='" + studName + '\'' +
                ", studPassword='" + studPassword + '\'' +
                ", studStatus=" + studStatus +
                ", studClassID=" + studClassID +
                ", studNumber='" + studNumber + '\'' +
                ", studPhone='" + studPhone + '\'' +
                ", studLoginTime='" + studLoginTime + '\'' +
                ", studImagePath='" + studImagePath + '\'' +
                ", studEnrollName='" + studEnrollName + '\'' +
                ", studentClassInfo=" + studentClassInfo +
                '}';
    }
}
