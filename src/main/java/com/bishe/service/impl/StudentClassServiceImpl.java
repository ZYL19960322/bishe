package com.bishe.service.impl;

import com.bishe.controller.admin.AdminActivityController;
import com.bishe.dao.StudentClassDao;
import com.bishe.dao.StudentDao;
import com.bishe.dto.AdminPage;
import com.bishe.dto.ExportStudentShow;
import com.bishe.dto.StudentClassInfo;
import com.bishe.pojo.Student;
import com.bishe.pojo.StudentClass;
import com.bishe.service.StudentClassService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZYL on 2019/2/14.
 */
@Service
@Transactional
public class StudentClassServiceImpl implements StudentClassService {

    Logger log = LoggerFactory.getLogger(StudentClassServiceImpl.class);
    @Autowired
    StudentClassDao studentClassDao;
    @Autowired
    StudentDao studentDao;

    @Override
    public List<StudentClass> selectGrades() {
        return studentClassDao.selectGrades();
    }

    @Override
    public List<StudentClass> selectDepartmentByParentId(String parentId) {
        return studentClassDao.selectDepartmentByParentId(parentId);
    }

    @Override
    public StudentClassInfo selectStudentClassInfo(Integer classId) {
        return studentClassDao.selectStudentClassInfo(classId);
    }

    @Override
    public PageInfo<Student> selectStudentListByGradeId(String gradeId) {
        PageHelper.startPage(1, AdminPage.pageSize);
        List<Student> studentList;
        //如果年级id为空则查询全部
        if(gradeId==""){
            studentList = studentDao.selectAllNormalStudent();
        }else {
            studentList = studentClassDao.selectStudentListByGradeId(gradeId);
        }

        PageInfo<Student> students = new PageInfo<>(studentList);
        return students;
    }

    @Override
    public PageInfo<Student> selectStudentListByDepartmentId(String departmentId) {

        PageHelper.startPage(1, AdminPage.pageSize);
        List<Student> studentList = studentClassDao.selectStudentListByDepartmentId(departmentId);
        PageInfo<Student> students = new PageInfo<>(studentList);
        return students;
    }

   

    @Override
    public ExportStudentShow selectExcelStudentByClassInfoId(String gradeId, String departmentId,  String classId, String actiId) {
        //根据传来的年级id，学院id，班级id，来判断筛选的具体内容（解决当id为null的情况）*范围由小到大*
        //一根据班级：1、班级id不为空 2、班级id为空，院系id不为空
        //二根据院系：1、院系id不为空 2、院系id为空，年级id不为空
        //三根据年级：1、年级id不为空
        //四选择全部：年级id，院系id，班级id都为空
        PageHelper.startPage(1, AdminPage.pageSize);
        //学生数据
        List<Student> studentList =null;
        List<StudentClass> studentClassList =null;
        if(gradeId!="" && departmentId=="" && classId==""){
            studentList=studentClassDao.selectExcelStudentByGradeId(gradeId,actiId);
            for (Student student : studentList) {
                if (student.getStudClassID()!= null) {
                    student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
                }
            }
            studentClassList=studentClassDao.selectDepartmentByParentId(gradeId);
            log.info("年级"+studentClassList);

            PageInfo<Student> students = new PageInfo<>(studentList);
            return new ExportStudentShow(studentClassList,students);
        }
        if(gradeId!="" && departmentId!="" && classId==""){
            studentList=studentClassDao.selectExcelStudentByDepartmentId(departmentId,actiId);
            for (Student student : studentList) {
                if (student.getStudClassID()!= null) {
                    student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
                }
            }
            studentClassList=studentClassDao.selectDepartmentByParentId(departmentId);
            PageInfo<Student> students = new PageInfo<>(studentList);
            return new ExportStudentShow(studentClassList,students);
        }
        if (gradeId!="" && departmentId!="" && classId !=""){
            studentList=studentClassDao.selectExcelStudentByClassId(classId,actiId);
            for (Student student : studentList) {
                if (student.getStudClassID()!= null) {
                    student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
                }
            }
            PageInfo<Student> students = new PageInfo<>(studentList);
            return new ExportStudentShow(students);
        }

        //查询全部
        studentList=studentDao.selectStudentListByActivityId(actiId);
        for (Student student : studentList) {
            if (student.getStudClassID()!= null) {
                student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
            }
        }
        PageInfo<Student> students = new PageInfo<>(studentList);
        studentClassList=studentClassDao.selectDepartmentByParentId(gradeId);
        return new ExportStudentShow(studentClassList,students);
    }





    @Override
    public ExportStudentShow selectExcelStudentByGradeId(String gradeId, String actiId) {
        //1、年级id不为空，根据年级id来查；
        //2、年级id为空则查询全部
        PageHelper.startPage(1, AdminPage.pageSize);
        List<Student> studentList =new ArrayList<>();
        List<StudentClass> studentClassList =new ArrayList<>();
        //情况2
        if(gradeId==""){
            studentList = studentDao.selectStudentListByActivityId(actiId);
            for (Student student : studentList) {
                if (student.getStudClassID()!= null) {
                    student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
                }
            }
            PageInfo<Student> students = new PageInfo<>(studentList);
            return new ExportStudentShow(studentClassList,students);
        }
        //情况1
        studentList=studentClassDao.selectExcelStudentByGradeId(gradeId,actiId);
        for (Student student : studentList) {
            if (student.getStudClassID()!= null) {
                student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
            }
        }
        studentClassList = studentClassDao.selectDepartmentByParentId(gradeId);
        PageInfo<Student> students = new PageInfo<>(studentList);
        return new ExportStudentShow(studentClassList,students);
    }

    @Override
    public ExportStudentShow selectExcelStudentByDepartmentId(String gradeId, String departmentId, String actiId) {
        //1、如果院系id为空，则根据年级id来查询，不查询专业班级的下拉框数据；2、如果院系id不为空，则根据院系id来查询数据，并查询专业班级的下拉框数据
        //3、如果年级id，院系id均为空,即院系筛选没有下拉框时还点击的时候（不要脸），则查询全部
        PageHelper.startPage(1, AdminPage.pageSize);
        List<Student> studentList =new ArrayList<>();
        List<StudentClass> studentClassList =new ArrayList<>();
        //情况3
        if(departmentId == "" && gradeId== ""){
            studentList = studentDao.selectStudentListByActivityId(actiId);
            for (Student student : studentList) {
                if (student.getStudClassID()!= null) {
                    student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
                }
            }
            PageInfo<Student> students = new PageInfo<>(studentList);
            return new ExportStudentShow(studentClassList,students);
        }
        //情况1
        if(departmentId==""){
            studentList = studentClassDao.selectExcelStudentByGradeId(gradeId,actiId);
            PageInfo<Student> students = new PageInfo<>(studentList);
            for (Student student : studentList) {
                if (student.getStudClassID()!= null) {
                    student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
                }
            }
            return new ExportStudentShow(studentClassList,students);
        }
        //情况2
        studentList=studentClassDao.selectExcelStudentByDepartmentId(departmentId,actiId);
        studentClassList = studentClassDao.selectDepartmentByParentId(departmentId);
        for (Student student : studentList) {
            if (student.getStudClassID()!= null) {
                student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
            }
        }
        PageInfo<Student> students = new PageInfo<>(studentList);
        return new ExportStudentShow(studentClassList,students);
    }

    @Override
    public ExportStudentShow selectExcelStudentByClassId(String departmentId, String classId, String actiId) {
        PageHelper.startPage(1, AdminPage.pageSize);
        List<Student> studentList =new ArrayList<>();
        List<StudentClass> studentClassList =new ArrayList<>();
        if(departmentId=="" && classId==""){
            studentList = studentDao.selectStudentListByActivityId(actiId);
            for (Student student : studentList) {
                if (student.getStudClassID()!= null) {
                    student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
                }
            }
            PageInfo<Student> students = new PageInfo<>(studentList);
            return new ExportStudentShow(studentClassList,students);
        }
        if (classId==""){
            studentList = studentClassDao.selectExcelStudentByDepartmentId(departmentId,actiId);
            PageInfo<Student> students = new PageInfo<>(studentList);
            for (Student student : studentList) {
                if (student.getStudClassID()!= null) {
                    student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
                }
            }
            return new ExportStudentShow(studentClassList,students);


        }
        studentList = studentClassDao.selectExcelStudentByClassId(classId, actiId);
        for (Student student : studentList) {
            if (student.getStudClassID()!= null) {
                student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
            }
        }
        PageInfo<Student> students = new PageInfo<>(studentList);
        return new ExportStudentShow(studentClassList,students);
    }

    @Override
    public ExportStudentShow exportStudentPageShow(String gradeId, String departmentId, String classId, String actiId, int pageNum) {
        //根据传来的年级id，学院id，班级id，来判断筛选的具体内容（解决当id为null的情况）*范围由小到大*
        //一根据班级：1、班级id不为空 2、班级id为空，院系id不为空
        //二根据院系：1、院系id不为空 2、院系id为空，年级id不为空
        //三根据年级：1、年级id不为空
        //四选择全部：年级id，院系id，班级id都为空


        PageHelper.startPage(pageNum, AdminPage.pageSize);
        //学生数据
        List<Student> studentList =new ArrayList<>();
        List<StudentClass> studentClassList =new ArrayList<>();
        //id长度为2，数据库年级的id长度为2，根据年级来查询学生信息
        if(classId!=""){
            studentList= studentClassDao.selectExcelStudentByClassId(classId,actiId);
            //封装班级信息
            for (Student student : studentList) {
                if (student.getStudClassID()!= null) {
                    student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
                }
            }
            PageInfo<Student> students = new PageInfo<>(studentList);
            //封装学生分页数据和级联查询信息返回
            return new ExportStudentShow(studentClassList,students);
        }
        if (departmentId!=""){
            studentList=studentClassDao.selectExcelStudentByDepartmentId(departmentId,actiId);
            //封装班级信息
            for (Student student : studentList) {
                if (student.getStudClassID()!= null) {
                    student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
                }
            }
            PageInfo<Student> students = new PageInfo<>(studentList);
            //封装学生分页数据和级联查询信息返回
            return new ExportStudentShow(studentClassList,students);

        }
        if(gradeId!=""){
            studentList=studentClassDao.selectExcelStudentByGradeId(gradeId,actiId);
            //封装班级信息
            for (Student student : studentList) {
                if (student.getStudClassID()!= null) {
                    student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
                }
            }
            PageInfo<Student> students = new PageInfo<>(studentList);
            //封装学生分页数据和级联查询信息返回
            return new ExportStudentShow(studentClassList,students);
        }

        //都为空查询全部
        studentList=studentDao.selectStudentListByActivityId(actiId);
        //封装班级信息
        for (Student student : studentList) {
            if (student.getStudClassID()!= null) {
                student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
            }
        }
        PageInfo<Student> students = new PageInfo<>(studentList);
        //封装学生分页数据和级联查询信息返回
        return new ExportStudentShow(studentClassList,students);
    }

    @Override
    public ExportStudentShow selectExcelStudentByPage(String gradeId, String departmentId, String classId, String actiId,int pageNum) {

        PageHelper.startPage(pageNum, AdminPage.pageSize);
        //学生数据
        List<Student> studentList =new ArrayList<>();
        studentList=  studentClassDao.selectExcelStudentByPage(gradeId, departmentId, classId, actiId);
        //List<StudentClass> studentClassList =new ArrayList<>();
            for (Student student : studentList) {
                if (student.getStudClassID()!= null) {
                    student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
                }
            }
            PageInfo<Student> students = new PageInfo<>(studentList);
            //封装学生分页数据和级联查询信息返回
            return new ExportStudentShow(students);
        }
}

