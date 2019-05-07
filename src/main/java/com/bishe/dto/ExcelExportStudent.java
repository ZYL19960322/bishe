package com.bishe.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

/**
 * Created by ZYL on 2019/3/7.
 */
public class ExcelExportStudent {


    //学生序号,非查询出来的数据,只是在excel表格中的排序序号
    @Excel(name = "序号", orderNum = "0" )
    private  Integer number;

    //学生学号
    @Excel(name = "学号", orderNum = "3" )
    private String studNumber;

    //学生联系电话
    @Excel(name = "电话", orderNum = "2" )
    private String studPhone;

    //学生报名信息中的名字
    @Excel(name = "姓名", orderNum = "4" )
    private String studEnrollName;

    //学生班级信息
    @Excel(name = "班级", orderNum = "1" )
    private String studClassInfo;

    public ExcelExportStudent(Integer number, String studNumber, String studPhone, String studEnrollName, String studClassInfo) {
        this.number = number;
        this.studNumber = studNumber;
        this.studPhone = studPhone;
        this.studEnrollName = studEnrollName;
        this.studClassInfo = studClassInfo;
    }

    public ExcelExportStudent() {
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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

    public String getStudEnrollName() {
        return studEnrollName;
    }

    public void setStudEnrollName(String studEnrollName) {
        this.studEnrollName = studEnrollName;
    }

    public String getStudClassInfo() {
        return studClassInfo;
    }

    public void setStudClassInfo(String studClassInfo) {
        this.studClassInfo = studClassInfo;
    }

    @Override
    public String toString() {
        return "ExcelExportStudent{" +
                "number=" + number +
                ", studNumber='" + studNumber + '\'' +
                ", studPhone='" + studPhone + '\'' +
                ", studEnrollName='" + studEnrollName + '\'' +
                ", studClassInfo='" + studClassInfo + '\'' +
                '}';
    }
}
