package com.bishe.service;

import com.bishe.dto.ExportStudentShow;
import com.bishe.dto.StudentClassInfo;
import com.bishe.pojo.Student;
import com.bishe.pojo.StudentClass;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Created by ZYL on 2019/2/14.
 */
public interface StudentClassService {
    //front
    List<StudentClass> selectGrades();
    List<StudentClass> selectDepartmentByParentId(String parentId);
    StudentClassInfo selectStudentClassInfo(Integer classId);

    //admin
    PageInfo<Student> selectStudentListByGradeId(String gradeId);
    PageInfo<Student> selectStudentListByDepartmentId(String departmentId);
    ExportStudentShow selectExcelStudentByClassInfoId(@Param("gradeId") String gradeId, @Param("departmentId") String departmentId, @Param("classId") String classId, @Param("actiId") String actiId);
    ExportStudentShow exportStudentPageShow(@Param("gradeId") String gradeId, @Param("departmentId") String departmentId, @Param("classId") String classId, @Param("actiId") String actiId, @Param("pageNum") int pageNum);

    ExportStudentShow selectExcelStudentByGradeId(@Param("gradeId") String gradeId, @Param("actiId") String actiId);
    ExportStudentShow selectExcelStudentByDepartmentId(@Param("gradeId") String gradeId, @Param("departmentId") String departmentId, @Param("actiId") String actiId);
    ExportStudentShow selectExcelStudentByClassId( @Param("departmentId") String departmentId, @Param("classId") String classId, @Param("actiId") String actiId);
    ExportStudentShow selectExcelStudentByPage(@Param("gradeId") String gradeId, @Param("departmentId") String departmentId, @Param("classId") String classId, @Param("actiId") String actiId, @PathParam("pageNum") int pageNum);


}