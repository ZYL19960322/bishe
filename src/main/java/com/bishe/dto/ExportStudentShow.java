package com.bishe.dto;

import com.bishe.pojo.Student;
import com.bishe.pojo.StudentClass;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by ZYL on 2019/3/8.
 */
public class ExportStudentShow {
    private List<StudentClass> studentClassList;
    private PageInfo<Student> pageInfo;

    public ExportStudentShow() {
    }

    public ExportStudentShow(PageInfo<Student> pageInfo) {
        this.pageInfo = pageInfo;
    }

    public ExportStudentShow(List<StudentClass> studentClassList, PageInfo<Student> pageInfo) {
        this.studentClassList = studentClassList;
        this.pageInfo = pageInfo;
    }

    public List<StudentClass> getStudentClassList() {
        return studentClassList;
    }

    public void setStudentClassList(List<StudentClass> studentClassList) {
        this.studentClassList = studentClassList;
    }

    public PageInfo<Student> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<Student> pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "ExportStudentShow{" +
                "studentClassList=" + studentClassList +
                ", pageInfo=" + pageInfo +
                '}';
    }
}
