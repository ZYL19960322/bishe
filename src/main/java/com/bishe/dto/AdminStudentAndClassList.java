package com.bishe.dto;

import com.bishe.pojo.Student;
import com.bishe.pojo.StudentClass;

import java.util.List;

/**
 * Created by ZYL on 2019/3/3.
 */
public class AdminStudentAndClassList {
    private List<Student> studentList;
    private List<StudentClass> studentClassList;
    //筛选出来的学生数量
    private  Long count;

    public AdminStudentAndClassList(List<Student> studentList, List<StudentClass> studentClassList, Long count) {
        this.studentList = studentList;
        this.studentClassList = studentClassList;
        this.count = count;
    }

    public AdminStudentAndClassList(List<Student> studentList,  Long count) {
        this.studentList = studentList;
        this.count = count;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public List<StudentClass> getStudentClassList() {
        return studentClassList;
    }

    public void setStudentClassList(List<StudentClass> studentClassList) {
        this.studentClassList = studentClassList;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AdminStudentAndClassList{" +
                "studentList=" + studentList +
                ", studentClassList=" + studentClassList +
                ", count=" + count +
                '}';
    }
}
