package com.bishe.dao;

import com.bishe.dto.StudentClassInfo;
import com.bishe.pojo.Student;
import com.bishe.pojo.StudentClass;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.swing.*;
import java.util.List;

/**
 * Created by ZYL on 2019/2/14.
 */
@Mapper
public interface StudentClassDao {
    //front
    List<StudentClass> selectGrades();
    List<StudentClass> selectDepartmentByParentId(String parentId);
    StudentClassInfo selectStudentClassInfo(Integer classId);
    //admin
    List<Student> selectStudentListByGradeId(String gradeId);
    List<Student> selectStudentListByDepartmentId(String departmentId);
    //admin 根据报名活动id年级id筛选学生
    List<Student> selectExcelStudentByGradeId(@Param("gradeId") String gradeId,@Param("actiId") String actiId);
    List<Student> selectExcelStudentByDepartmentId(@Param("departmentId") String departmentId,@Param("actiId") String actiId);
    List<Student> selectExcelStudentByClassId(@Param("classId") String classId,@Param("actiId") String actiId);
    //报名信息表的分页显示
    List<Student> selectExcelStudentByPage(@Param("gradeId") String gradeId,@Param("departmentId") String departmentId,@Param("classId") String classId,@Param("actiId") String actiId);
    List<Student> selectExcelPresentStudent(@Param("gradeId") String gradeId,@Param("departmentId") String departmentId,@Param("classId") String classId,@Param("actiId") String actiId);


}
