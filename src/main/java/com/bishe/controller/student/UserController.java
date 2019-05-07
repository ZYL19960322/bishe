package com.bishe.controller.student;

import com.bishe.dto.*;
import com.bishe.enums.ActivityStatusEnum;
import com.bishe.enums.StudentInfoEnum;
import com.bishe.pojo.*;
import com.bishe.service.ActivityService;
import com.bishe.service.DataInfoService;
import com.bishe.service.StudentClassService;
import com.bishe.service.StudentService;
import com.bishe.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by ZYL on 2019/2/17.
 */
@Controller
public class UserController {

    Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    DataInfoService dataInfoService;

    @Autowired
    StudentClassService studentClassService;

    @Autowired
    StudentService studentService;

    @Autowired
    ActivityService activityService;



    @GetMapping("/toUserPage")
    //封装在用户页面的数据
    public String toUserPage(HttpServletRequest request, ModelMap modelMap) {
        //1、session中取出学生
        Student student = (Student) request.getSession().getAttribute("student");
        //2、根据学生id查询出与学生相关的活动数量
        UserResponse userResponse = dataInfoService.toUserPage(student.getStudId());
        //3、封装学生数据
        userResponse.setStudent(student);

        modelMap.put("userResponse", userResponse);
        log.info("返回user页面的数据userResponse" + userResponse);
        return "front/user";
    }

    @GetMapping("/userInfo")
    //封装学生数据到页面显示
    public String userInfo(ModelMap modelMap, HttpServletRequest request) {
        //1、从session中取出学生数据
        Student student = (Student) request.getSession().getAttribute("student");
        //2、查询出学生的班级信息
        StudentClassInfo studentClassInfo = new StudentClassInfo();
        //学生班级信息为空时需设置班级的信息为空
        if (student.getStudClassID() == null) {
            studentClassInfo.setStudGradeName("");
            studentClassInfo.setStudDepartmentName("");
            studentClassInfo.setStudClassName("");
        } else {
            studentClassInfo = studentClassService.selectStudentClassInfo(student.getStudClassID());
        }
        modelMap.put("student", student);
        modelMap.put("studentClassInfo", studentClassInfo);
        return "front/userinfo";
    }

    @GetMapping("/changestudentinfo")
    //封装学生数据到页面显示
    public String changestudentinfo(ModelMap modelMap, HttpServletRequest request) {
        //log.info("接收到的id"+actiId);
        //将学生要报名参加的活动的活动id存入到session中，页面定向到输入学生报名信息页面
        // request.getSession().setAttribute("EnrollActiId",actiId);
        //从session取出学生的个人信息
        Student student = (Student) request.getSession().getAttribute("student");
        modelMap.put("student", student);
        log.info("这是学生" + student);
        //查询出所有的年级返回到报名信息页面
        List<StudentClass> gradeList = studentClassService.selectGrades();
        modelMap.put("gradeList", gradeList);
        log.info("查询出的年级" + gradeList);

        StudentClassInfo studentClassInfo = new StudentClassInfo();
        if (student.getStudClassID() != null) {
            //如果学生有班级数据则从数据库中取出
            studentClassInfo = studentClassService.selectStudentClassInfo(student.getStudClassID());
        } else {
            //如果学生没有班级数据，则自己初始化，以便页面显示效果正常
            studentClassInfo.setStudGradeName("年级选择");
            studentClassInfo.setStudDepartmentName("院系选择");
            studentClassInfo.setStudClassName("专业班级选择");
        }
        modelMap.put("studentClassInfo", studentClassInfo);
        log.info("学生班级" + studentClassInfo);
        return "front/changestudentinfo";
    }

    //修改个人信息之修改头像
    @PostMapping("/changeStudentImage")
    @ResponseBody
    public Boolean changeStudentImage(@RequestParam("file") MultipartFile studImageFile, HttpServletRequest request) {
        //判断学生是否已存在头像，如果有则删除后再添加，没有则直接添加
        //从session中取出学生
        Student student = (Student) request.getSession().getAttribute("student");
        log.info("session中的对象" + student);
        try {
            if (student.getStudImagePath() != null) {
                //学生存在头像则先删除然后添加
                ImageUtil.myDeleteImage(student.getStudImagePath());
            }
            //调用自己的工具类存储(添加)头像
            String studImagePath = ImageUtil.myStoreStudentImage(studImageFile, FileUtil.getStudentImageRelativePath());
            student.setStudImagePath(studImagePath);
            //调用修改学生信息的公用方法修改学生
            studentService.changeStudentInfo(student);
        } catch (Exception e) {
            return false;
        }

        Student student2 = (Student) request.getSession().getAttribute("student");
        return true;
    }

   //修改个人信息之修改文字信息
    @PostMapping("/changeStudentInfo")
    @ResponseBody
    public  StudentInfoResponse changeStudentInfo(Student student1,HttpServletRequest request,DataInfo dataInfo) {
        log.info("使用了我的亮点:MyBeanUtil的方法" + student1);
        //从session中取出学生
        Student student2 = (Student) request.getSession().getAttribute("student");
        Student student = (Student) MyBeanUtil.combineSydwCore(student1, student2);
        log.info("修改学生信息的合并对象的结果" + student);
        //1、验证接收到学生数据的有效性
        //如果学生的信息不合理，直接返回错误信息，合理则继续执行下列操作
        StudentInfoResponse studentInfoResponse = studentService.testStudentInfo(student);
        if(studentInfoResponse.getStatus()!=StudentInfoEnum.INFO_SUCCESS.getStatusId()){
            return studentInfoResponse;
        }
        //信息无误，插入(更新)到数据中
        studentService.changeStudentInfo(student);
        return studentInfoResponse;
    }

    @GetMapping("/toChangeStudentPassword")
    public  String toChangeStudentPassword(HttpServletRequest request,ModelMap modelMap){
        Student student =(Student)request.getSession().getAttribute("student");
        modelMap.put("student",student);
        return "front/changepassword";

    }
    @PostMapping("/changeStudentPassword")
    @ResponseBody
    public  StudentInfoResponse changeStudentPassword(ChangePassword changePassword,HttpServletRequest request, ModelMap modelMap){
        log.info("接受的数据"+changePassword);

        Student student =(Student)request.getSession().getAttribute("student");
        log.info("更改学生账目密码前"+student);
        StudentInfoResponse studentInfoResponse = studentService.changeStudentPassword(student, changePassword);
        //modelMap.put("student",student);
        Student student1=(Student)request.getSession().getAttribute("student");
        log.info("更改学生账目密码后"+student1);
        return studentInfoResponse;

    }

    @GetMapping("/selectUserActivityListBySign")
    public String selectUserActivityListBySign(@PathParam("sign") int sign,ModelMap modelMap ,HttpServletRequest request ,IndexResponse indexResponse){
        log.info("接收的sign参数"+sign);
        Student student=(Student)request.getSession().getAttribute("student");
        indexResponse.setActivityList(activityService.selectActivityListBySign(sign,student.getStudId()));
        modelMap.put("indexResponse", indexResponse);
        //log.info("查询出的数据"+indexResponse.getActivityList()+"总共数量"+indexResponse.getActivityList().size());
        return "front/useractivitylist";

    }
    @GetMapping("/userActivityMessage")
    //@ResponseBody
    //根据活动ID查询出活动，使其显示在活动列表页面
    public String userActivityMessage(@PathParam("actiId") String actiId, HttpServletRequest request, ModelMap modelMap){
        //1、从数据库中查询出活动
        Activity activity=activityService.selectActivityById(actiId);
        //2、根据活动的状态显示给用户不同的信息和功能
        //根据活动的状态id，（映射）得到对应状态的枚举型实体，根据这个生成返回到前端的对象
        ActivityStatusEnum activityStatusEnum = ActivityStatusEnum.stateOf(activity.getActiStatusId());
        //借用StudentInfoResponse是因为这个的属性刚好符合需求与类的名字无关
        StudentInfoResponse msg = new StudentInfoResponse(activityStatusEnum.getStatusId(), activityStatusEnum.getStatusDesc());
        //3、将信息带回给前端页面
        modelMap.put("msg",msg);
        modelMap.put("activity",activity);
        return "front/useractivitydetail";
    }





    @PostMapping("/deleteEnrollRecord")
    @ResponseBody
    public Boolean deleteEnrollRecord(@PathParam("actiId") String actiId,HttpServletRequest request){
        Student student = (Student) request.getSession().getAttribute("student");
        return dataInfoService.deleteEnrollRecord(actiId,student.getStudId());
    }





    @GetMapping("/loginOut")
    public String loginOut (HttpServletRequest request){
        //情况session数据
        request.getSession().invalidate();
        //重定向到登录页面
        return "front/login";
    }

}
