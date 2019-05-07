package com.bishe.dao;

import com.bishe.dto.EnrollStudent;
import com.bishe.pojo.Activity;
import com.bishe.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ZYL on 2018/12/12.
 */
@Mapper
public interface StudentDao {
    //front
    Student selectStudentByNameAndPassword(@Param("studName") String studName, @Param("studPassword") String studPassword);
    List<Student> selectAllStudent();
    int addStudentWithNameAndPassword(@Param("studName") String studName, @Param("studPassword") String studPassword);
    void changeStudentInfo(Student student);
    void changeStudentPassword(Student student);

    //admin
    List<Student> selectStudentListByClassId(String classId);
    List<Student> selectStudentListByActivityId(String actiId);
    List<EnrollStudent> selectEnrollStudentByActiId(String actiId);
    Student selectStudentByStudentId(String studId);
    int cancelStudentByStudId(String studId);
    List<Student> selectAllNormalStudent();
    List<Student>selectCancelStudentList();
    int deleteStudentBystudId(String studId);


}

