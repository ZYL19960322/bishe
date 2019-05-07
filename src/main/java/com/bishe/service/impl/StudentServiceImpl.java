package com.bishe.service.impl;

import com.bishe.dao.ActivityDao;
import com.bishe.dao.DataInfoDao;
import com.bishe.dao.StudentClassDao;
import com.bishe.dao.StudentDao;
import com.bishe.dto.*;
import com.bishe.enums.ActivityStatusEnum;
import com.bishe.enums.StudentInfoEnum;
import com.bishe.pojo.Admin;
import com.bishe.pojo.DataInfo;
import com.bishe.pojo.Student;
import com.bishe.pojo.StudentClass;
import com.bishe.service.StudentService;
import com.bishe.utils.MD5Utils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.security.krb5.internal.APOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZYL on 2018/12/12.
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);
    @Autowired
    StudentDao studentDao;
    @Autowired
    StudentClassDao studentClassDao;
    @Autowired
    DataInfoDao dataInfoDao;
    @Autowired
    ActivityDao activityDao;

    @Override
    public Student selectStudentByNameAndPassword(String studName, String studPassword) {
        return studentDao.selectStudentByNameAndPassword(studName, studPassword);
    }

    @Override
    public List<Student> selectAllStudent() {
        return studentDao.selectAllStudent();
    }

    @Override
    public int addStudentWithNameAndPassword(String studName, String studPassword) {
        return studentDao.addStudentWithNameAndPassword(studName, studPassword);
    }

    @Override
    //重新从数据库中取出学生，判断学生的报名信息是否完全，返回int的标志信息，使用枚举型接收
    public StudentInfoResponse comfirmStudentInfo(String studName, String studPassword) {
        Student student = studentDao.selectStudentByNameAndPassword(studName, studPassword);
        if (student.getStudClassID() == null) {
            return new StudentInfoResponse(StudentInfoEnum.CLASS_EMPTY.getStatusId(), StudentInfoEnum.CLASS_EMPTY.getStatusDesc());
        }
        if (student.getStudNumber() == null) {
            return new StudentInfoResponse(StudentInfoEnum.NUMBER_EMPTY.getStatusId(), StudentInfoEnum.NUMBER_EMPTY.getStatusDesc());
        }
        if (student.getStudPhone() == null) {
            return new StudentInfoResponse(StudentInfoEnum.PHONE_EMPTY.getStatusId(), StudentInfoEnum.PHONE_EMPTY.getStatusDesc());
        }
        if (student.getStudEnrollName() == null) {
            return new StudentInfoResponse(StudentInfoEnum.ENROLL_NAME_EMPTY.getStatusId(), StudentInfoEnum.ENROLL_NAME_EMPTY.getStatusDesc());
        }
        return new StudentInfoResponse(StudentInfoEnum.INFO_SUCCESS.getStatusId(), StudentInfoEnum.INFO_SUCCESS.getStatusDesc());
    }

    @Override
    public void changeStudentInfo(Student student) {
        studentDao.changeStudentInfo(student);
    }

    @Override
    public StudentInfoResponse testStudentInfo(Student student) {
        if (student.getStudEnrollName() == null || student.getStudEnrollName().length() < 2) {
            return new StudentInfoResponse(StudentInfoEnum.ENROLL_NAME_EMPTY.getStatusId(), StudentInfoEnum.ENROLL_NAME_EMPTY.getStatusDesc());
        }
        if (student.getStudPhone() == null || (student.getStudPhone().length() != 6 & student.getStudPhone().length() != 11)) {
            return new StudentInfoResponse(StudentInfoEnum.PHONE_EMPTY.getStatusId(), StudentInfoEnum.PHONE_EMPTY.getStatusDesc());
        }
        if (student.getStudClassID() == null) {
            return new StudentInfoResponse(StudentInfoEnum.CLASS_EMPTY.getStatusId(), StudentInfoEnum.CLASS_EMPTY.getStatusDesc());
        }
        if (student.getStudNumber() == null || student.getStudNumber().length() != 13) {
            return new StudentInfoResponse(StudentInfoEnum.NUMBER_EMPTY.getStatusId(), StudentInfoEnum.NUMBER_EMPTY.getStatusDesc());
        }
        return new StudentInfoResponse(StudentInfoEnum.INFO_SUCCESS.getStatusId(), StudentInfoEnum.INFO_SUCCESS.getStatusDesc());
    }

    @Override
    public StudentInfoResponse changeStudentPassword(Student student, ChangePassword changePassword) {
        //账号名有错 主要功能是修改密码，所以如果用户名没有修改也可以，但新密码不能与原密码相同，必须有改变
        if (changePassword.getName() == null || changePassword.getName().length() < 2) {
            return new StudentInfoResponse(StudentInfoEnum.ACCOUNT_NAME_ERROR.getStatusId(), StudentInfoEnum.ACCOUNT_NAME_ERROR.getStatusDesc());
        }
        //原密码有错：这里字符串比较不能用== 原因你懂的
        if (!MD5Utils.md5(changePassword.getPassword()).equals(student.getStudPassword())) {
            return new StudentInfoResponse(StudentInfoEnum.PASSWORD_ERROR.getStatusId(), StudentInfoEnum.PASSWORD_ERROR.getStatusDesc());
        }
        //新密码有错
        if (changePassword.getNewPassword() == null || changePassword.getNewPassword().length() < 6 || changePassword.getNewPassword().equals(student.getStudPassword()))
            return new StudentInfoResponse(StudentInfoEnum.NEW_PASSWORD_ERROR.getStatusId(), StudentInfoEnum.NEW_PASSWORD_ERROR.getStatusDesc());
        //确认密码有错
        if (changePassword.getComfirmPassword() == null || !changePassword.getComfirmPassword().equals(changePassword.getNewPassword())) {
            return new StudentInfoResponse(StudentInfoEnum.CONFIRM_PASSWORD_ERROR.getStatusId(), StudentInfoEnum.CONFIRM_PASSWORD_ERROR.getStatusDesc());
        }
        //均没有错误，执行修改用户名或修改密码
        student.setStudName(changePassword.getName());
        student.setStudPassword(MD5Utils.md5(changePassword.getNewPassword()));
        studentDao.changeStudentPassword(student);
        return new StudentInfoResponse(StudentInfoEnum.INFO_SUCCESS.getStatusId(), StudentInfoEnum.INFO_SUCCESS.getStatusDesc());
    }


    @Override
    public PageInfo<Student> selectStudentListPageShow(@Param("gradeId") String gradeId, @Param("departmentId") String departmentId, @Param("classId") String classId, @Param("pageNum") int pageNum) {
        //定义每页显示多少条数据
        // int pageSize = 11;
        PageHelper.startPage(pageNum, AdminPage.pageSize);
        //根据传入的数据选择使用的方法
        if (classId != null && classId != "") {
            List<Student> studentList = studentDao.selectStudentListByClassId(classId);
            studentList = studentOtherInfo(studentList);
            //进行分页查询
            PageInfo<Student> students = new PageInfo<Student>(studentList);
            return students;
        }
        if (departmentId != null && departmentId != "") {
            List<Student> studentList = studentClassDao.selectStudentListByDepartmentId(departmentId);
            studentList = studentOtherInfo(studentList);
            PageInfo<Student> students = new PageInfo<Student>(studentList);
            return students;
        }
        if (gradeId != null && gradeId != "") {
            List<Student> studentList = studentClassDao.selectStudentListByGradeId(gradeId);
            studentList = studentOtherInfo(studentList);
            PageInfo<Student> students = new PageInfo<Student>(studentList);
            return students;
        }
        //没有年级、院系、班级的筛选条件，则查询出所有学生
        List<Student> studentList = studentDao.selectAllNormalStudent();
        studentList = studentOtherInfo(studentList);


        //如果学生没有班级等报名信息则自定义填写以便给前端较好的效果展示

        PageInfo<Student> students = new PageInfo<Student>(studentList);
        return students;
    }

    private List<Student> studentOtherInfo(List<Student> studentList) {
        //如果学生没有班级等报名信息则自定义填写以便给前端较好的效果展示
        for (Student student : studentList) {
            if (student.getStudClassID() == null) {
                student.setStudentClassInfo(new StudentClassInfo("未", "填写"));
                student.setStudNumber("未填写");
                student.setStudPhone("未填写");
                student.setStudEnrollName("未填写");
            }
            if (student.getStudClassID() != null) {
                StudentClassInfo studentClassInfo = studentClassDao.selectStudentClassInfo(student.getStudClassID());
                student.setStudentClassInfo(studentClassInfo);
            }
        }
        return studentList;
    }

    @Override
    public PageInfo<Student> selectAllStudentPageShow() {
        PageHelper.startPage(1, AdminPage.pageSize);
        List<Student> studentList = studentDao.selectAllNormalStudent();
        //如果学生没有班级等报名信息则自定义填写以便给前端较好的效果展示
        for (Student student : studentList) {
            if (student.getStudClassID() == null) {
                student.setStudentClassInfo(new StudentClassInfo("未", "填写"));
                student.setStudNumber("未填写");
                student.setStudEnrollName("未填写");
                student.setStudPhone("未填写");
            }
            if (student.getStudClassID() != null) {
                StudentClassInfo studentClassInfo = studentClassDao.selectStudentClassInfo(student.getStudClassID());
                student.setStudentClassInfo(studentClassInfo);
            }
        }

        PageInfo<Student> students = new PageInfo<>(studentList);
        return students;
    }


    @Override
    public PageInfo<Student> selectStudentListByClassId(String classId) {
        PageHelper.startPage(1, AdminPage.pageSize);
        List<Student> studentList = studentDao.selectStudentListByClassId(classId);
        PageInfo<Student> students = new PageInfo<>(studentList);
        return students;
    }

    @Override
    public PageInfo<Student> selectStudentListByActivityId(String actiId) {
        PageHelper.startPage(1, AdminPage.pageSize);
        List<Student> studentList = studentDao.selectStudentListByActivityId(actiId);
        PageInfo<Student> studentPageInfo = new PageInfo<>(studentList);
        return studentPageInfo;
    }

    @Override
    public List<ExcelExportStudent> exportStudentToExcel(String actiId) {
        List<Student> studentList = studentDao.selectStudentListByActivityId(actiId);
        ArrayList<ExcelExportStudent> excelExportStudentList = new ArrayList<>();

        //学生序号,非查询出来的数据,只是在excel表格中的排序序号
        int number = 1;
        for (Student student : studentList) {
            //封装学生的班级信息
            if (student.getStudClassID() != null) {
                student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
            }

            ExcelExportStudent excelStudent = new ExcelExportStudent();
            //学生序号
            excelStudent.setNumber(number++);
            //学生班级信息：年级+专业班级
            excelStudent.setStudClassInfo(student.getStudentClassInfo().getStudGradeName() + student.getStudentClassInfo().getStudClassName());
            //学生报名信息
            excelStudent.setStudEnrollName(student.getStudEnrollName());
            //学生学号
            excelStudent.setStudNumber(student.getStudNumber());
            //学生电话
            excelStudent.setStudPhone(student.getStudPhone());
            excelExportStudentList.add(excelStudent);
        }
        //将封装好的数据返回
        return excelExportStudentList;
    }

    @Override
    //根据年级、院系、班级、筛选出来的结果，将数据导出excel表格
    public List<ExcelExportStudent> exportPresentStudentsToExcel(String gradeId, String departmentId, String classId, String actiId) {

        //学生数据
        List<Student> studentList = new ArrayList<>();
        ArrayList<ExcelExportStudent> excelExportStudentList = new ArrayList<>();
        //学生序号,非查询出来的数据,只是在excel表格中的排序序号
        int number = 1;
        studentList = studentClassDao.selectExcelPresentStudent(gradeId, departmentId, classId, actiId);
        for (Student student : studentList) {
            if (student.getStudClassID() != null) {
                student.setStudentClassInfo(studentClassDao.selectStudentClassInfo(student.getStudClassID()));
            }

            ExcelExportStudent excelStudent = new ExcelExportStudent();
            //学生序号
            excelStudent.setNumber(number++);
            //学生班级信息：年级+专业班级
            excelStudent.setStudClassInfo(student.getStudentClassInfo().getStudGradeName() + student.getStudentClassInfo().getStudClassName());
            //学生报名信息
            excelStudent.setStudEnrollName(student.getStudEnrollName());
            //学生学号
            excelStudent.setStudNumber(student.getStudNumber());
            //学生电话
            excelStudent.setStudPhone(student.getStudPhone());
            excelExportStudentList.add(excelStudent);
        }
        log.info("要打印的数据" + excelExportStudentList);

        return excelExportStudentList;
    }

    @Override
    public List<EnrollStudent> selectEnrollStudentByActiId(String actiId) {
        //根据活动id查询data_info表然后查询student查询出学生id、学生姓名、学生学号，学生班级id,然后根据班级id查询出具体的班级信息并且封装到EnrollStudent中后返回
        List<EnrollStudent> enrollStudentList = studentDao.selectEnrollStudentByActiId(actiId);
        for (EnrollStudent enrollStudent : enrollStudentList) {
            StudentClassInfo studentClassInfo = studentClassDao.selectStudentClassInfo(enrollStudent.getStudClassID());
            //从studentClassInfo取出要显示的数据
            String classInfo = studentClassInfo.getStudGradeName().substring(2, 4) + studentClassInfo.getStudClassName();
            //封装到enrollStudent中
            enrollStudent.setClassInfo(classInfo);
        }
        //返回
        return enrollStudentList;

    }

    @Override
    public Boolean cancelStudentByStudId(String studId) {
        if (studentDao.cancelStudentByStudId(studId) != 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public PageInfo<Student> selectCancelStudentList() {
        PageHelper.startPage(AdminPage.pageNum, AdminPage.pageSize);
        List<Student> studentList = studentDao.selectCancelStudentList();
        //如果学生没有班级等报名信息则自定义填写以便给前端较好的效果展示
        for (Student student : studentList) {
            if (student.getStudClassID() == null) {
                student.setStudentClassInfo(new StudentClassInfo("未", "填写"));
                student.setStudNumber("未填写");
                student.setStudEnrollName("未填写");
                student.setStudPhone("未填写");
            }
            if (student.getStudClassID() != null) {
                StudentClassInfo studentClassInfo = studentClassDao.selectStudentClassInfo(student.getStudClassID());
                student.setStudentClassInfo(studentClassInfo);
            }
        }
        PageInfo<Student> pageInfo = new PageInfo<>(studentList);
        return pageInfo;
    }

    @Override
    public PageInfo<Student> pageShowCancelStudent(int pageNum) {
        PageHelper.startPage(pageNum, AdminPage.pageSize);
        List<Student> studentList = studentDao.selectCancelStudentList();
        //如果学生没有班级等报名信息则自定义填写以便给前端较好的效果展示
        for (Student student : studentList) {
            if (student.getStudClassID() == null) {
                student.setStudentClassInfo(new StudentClassInfo("未", "填写"));
                student.setStudNumber("未填写");
                student.setStudEnrollName("未填写");
                student.setStudPhone("未填写");
            }
            if (student.getStudClassID() != null) {
                StudentClassInfo studentClassInfo = studentClassDao.selectStudentClassInfo(student.getStudClassID());
                student.setStudentClassInfo(studentClassInfo);
            }
        }
        PageInfo<Student> pageInfo = new PageInfo<>(studentList);
        return pageInfo;
    }

    @Override
    public StudentInfoResponse selectStudentDetailBystudId(String studId) {
        //查询学生的发布活动情况和报名参加活动情况然后返回给前端，用于判断是否真的要删除学生
        String studentDetail = "";
        String publishDetail = "";
        String enrollDetail = "";
        int i = 0;
        //发布的活动
        //审核中的数量
        i = activityDao.selectActivityCountByStatus(studId, ActivityStatusEnum.ACTIVITY_CHECKING.getStatusId());
        if (i != 0) {
            publishDetail = publishDetail + "审核中=" + i;
        }
        i = activityDao.selectActivityCountByStatus(studId, ActivityStatusEnum.ACTIVITY_ENROLLING.getStatusId());
        if (i != 0) {
            publishDetail = publishDetail + "报名中=" + i;
        }
        i = activityDao.selectActivityCountByStatus(studId, ActivityStatusEnum.ACTIVITY_PREPARING.getStatusId());
        if (i != 0) {
            publishDetail = publishDetail + "筹办中=" + i;
        }
        i = activityDao.selectActivityCountByStatus(studId, ActivityStatusEnum.ACTIVITY_HOLDING.getStatusId());
        if (i != 0) {
            publishDetail = publishDetail + "基办中=" + i;
        }
        i = activityDao.selectActivityCountByStatus(studId, ActivityStatusEnum.ACTIVITY_HASENDED.getStatusId());
        if (i != 0) {
            publishDetail = publishDetail + "已结束中=" + i;
        }
        i = dataInfoDao.selectEndCount(studId);
        if (i != 0) {
            enrollDetail = enrollDetail + "报名中=" + i;
        }
        i = dataInfoDao.selectDuringCount(studId);
        if (i != 0) {
            enrollDetail = enrollDetail + "进行中=" + i;
        }
        if (i != 0) {
            enrollDetail = enrollDetail + "已结束=" + i;
        }

        if (publishDetail != "") {
            studentDetail =studentDetail+ "发布的活动:" + publishDetail;
        }
        if (enrollDetail != "" && publishDetail != "") {
            studentDetail = studentDetail + "--参加的活动:" + enrollDetail;
        }
        if (enrollDetail != "" && publishDetail == "") {
            studentDetail = studentDetail + "参加的活动:" + enrollDetail;
        }

        if (studentDetail.equals("")) {
            studentDetail =  "没有发布活动和参加活动";
        }

        return new StudentInfoResponse(1, studentDetail);
    }

    @Override
    public Boolean deleteStudentBystudId(String studId) {
        if (studentDao.deleteStudentBystudId(studId) != 0) {
            return true;
        } else {
            return false;
        }
    }

}