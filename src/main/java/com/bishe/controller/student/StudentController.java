package com.bishe.controller.student;


import com.bishe.dto.IndexResponse;
import com.bishe.dto.StudentInfoResponse;
import com.bishe.enums.StudentInfoEnum;
import com.bishe.pojo.*;
import com.bishe.service.*;
import com.bishe.utils.CheckCodeUtil;
import com.bishe.utils.HttpServletRequestUtil;
import com.bishe.utils.MD5Utils;
import com.bishe.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Created by ZYL on 2018/12/12.
 */
//@RestController=@Controller+@Reponsebody
@Controller
public class StudentController {
    Logger log=LoggerFactory.getLogger(StudentController.class);

    @Autowired
    StudentService studentService;

    @Autowired
    ActivityService activityService;

    @Autowired
    ActivityStatusService activityStatusService;

    @Autowired
    ActivityCategoryService activityCategoryService;


    @PostMapping("/student/login")
    @ResponseBody
    public StudentInfoResponse studentLogin(@PathParam("studName")String studName, @PathParam("studPassword") String studPassword,@PathParam("checkCode") String checkCode ,HttpServletRequest request) {

        log.info("验证码===="+checkCode);
        //1、验证码是否有误
       if (!CheckCodeUtil.checkVerifyCode(request,checkCode)) {

            return new StudentInfoResponse(StudentInfoEnum.CheckCodeError.getStatusId(),StudentInfoEnum.CheckCodeError.getStatusDesc());
        }

        //2、判断用户密码和密码是否为空
        //密码需进行相同的加密
        if ("" != studName && "" != studPassword) {
            Student student = studentService.selectStudentByNameAndPassword(studName, MD5Utils.md5(studPassword));
            //账号或密码不正确
            if (null == student) {
                return new StudentInfoResponse(StudentInfoEnum.AccountNameOrPasswordError.getStatusId(), StudentInfoEnum.AccountNameOrPasswordError.getStatusDesc());
            }
            //账号已被注销
            if (student.getStudStatus() == 0) {
                return new StudentInfoResponse(StudentInfoEnum.CanceledAccount.getStatusId(), StudentInfoEnum.CanceledAccount.getStatusDesc());
            }

            //设置学生的这次登录时间并更新到数据库中
            student.setStudLoginTime(TimeUtils.getStringDate());
            studentService.changeStudentInfo(student);
            //登录成功，将学生存到session中
            request.getSession().setAttribute("student", student);
            //返回登录成功
            return new StudentInfoResponse(StudentInfoEnum.LoginSuccess.getStatusId(),StudentInfoEnum.LoginSuccess.getStatusDesc());
        }else {
            //用户密码或密码为空
            return new StudentInfoResponse(StudentInfoEnum.NameOrPasswordEmpty.getStatusId(),StudentInfoEnum.NameOrPasswordEmpty.getStatusDesc());
        }

    }

    @PostMapping("/student/register")
    @ResponseBody
    public StudentInfoResponse studRegister(@PathParam("studName")String studName, @PathParam("studPassword") String studPassword,@PathParam("confirmPassword") String confirmPassword ,@PathParam("checkCode") String checkCode,HttpServletRequest request) {

        //1、验证码是否有误
       if (!CheckCodeUtil.checkVerifyCode(request,checkCode)) {
            return new StudentInfoResponse(StudentInfoEnum.CheckCodeError.getStatusId(),StudentInfoEnum.CheckCodeError.getStatusDesc());
        }
        //2、用户名格式
        if( studName.length()<2 || studName.length()>4){
            return new StudentInfoResponse(StudentInfoEnum.AccountNameError.getStatusId(),StudentInfoEnum.AccountNameError.getStatusDesc());
        }
        //3、密码格式
        if (studPassword.length() < 6 || studPassword.length() >12) {
            return new StudentInfoResponse(StudentInfoEnum.AccountPasswordError.getStatusId(),StudentInfoEnum.AccountPasswordError.getStatusDesc());
        }
        //4、确认密码
        if( !confirmPassword.equals(studPassword)){
            return new StudentInfoResponse(StudentInfoEnum.ConfirmPasswordError.getStatusId(),StudentInfoEnum.ConfirmPasswordError.getStatusDesc());
        }
        //5、账号已被注册
        //密码经过相同的MD5加密才能比较
        List<Student> studentList = studentService.selectAllStudent();
        for (Student student : studentList) {
            if (studName.equals(student.getStudName()) && MD5Utils.md5(studPassword).equals(student.getStudPassword())){
               return new StudentInfoResponse(StudentInfoEnum.HasRegistered.getStatusId(),StudentInfoEnum.HasRegistered.getStatusDesc());
            }
        }
        //6、注册成功
        //将密码经过MD5加密存储
        studentService.addStudentWithNameAndPassword(studName, MD5Utils.md5(studPassword));
        return  new StudentInfoResponse(StudentInfoEnum.RegisterSuccess.getStatusId(),StudentInfoEnum.RegisterSuccess.getStatusDesc());

    }
}
