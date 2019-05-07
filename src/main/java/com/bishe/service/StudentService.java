package com.bishe.service;

import com.bishe.dto.ChangePassword;
import com.bishe.dto.EnrollStudent;
import com.bishe.dto.ExcelExportStudent;
import com.bishe.dto.StudentInfoResponse;
import com.bishe.pojo.Student;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by ZYL on 2018/12/12.
 */
public interface StudentService {

    //front
    Student selectStudentByNameAndPassword(String studName, String studPassword);
    List<Student> selectAllStudent();
    int addStudentWithNameAndPassword(String studName, String studPassword);
    //重新从数据库中取出学生，判断学生的报名信息是否完全，返回int的标志信息，使用枚举型接收
    StudentInfoResponse comfirmStudentInfo(String studName, String studPassword);
    void changeStudentInfo(Student student);
    StudentInfoResponse testStudentInfo(Student student);
    StudentInfoResponse changeStudentPassword(Student student, ChangePassword changePassword);

    //admin
    PageInfo<Student> selectStudentListByClassId(String classId);
    PageInfo<Student> selectStudentListPageShow(String gradeId, String departmentId, String classId, int  pageNumber);
    PageInfo<Student> selectAllStudentPageShow();
    PageInfo<Student> selectStudentListByActivityId(String actiId);
    //封装导出到excel表格的数据
    List<ExcelExportStudent> exportStudentToExcel(String actiId);
    //根据年级、院系、班级、筛选出来的结果，将数据导出excel表格
    List<ExcelExportStudent>exportPresentStudentsToExcel(String gradeId, String departmentId, String classId,String actiId);
    List<EnrollStudent> selectEnrollStudentByActiId(String actiId);
    Boolean cancelStudentByStudId(String studId);
    PageInfo<Student> selectCancelStudentList();
    PageInfo<Student>pageShowCancelStudent(int pageNum);
    Boolean deleteStudentBystudId(String studId);
    StudentInfoResponse selectStudentDetailBystudId(String studId);
}
