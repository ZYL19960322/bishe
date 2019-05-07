package com.bishe.controller.admin;

import com.bishe.dto.*;
import com.bishe.pojo.Activity;
import com.bishe.pojo.Student;
import com.bishe.pojo.StudentClass;
import com.bishe.service.ActivityService;
import com.bishe.service.StudentClassService;
import com.bishe.service.StudentService;
import com.bishe.utils.Excel.MyExportExcelUtil;
import com.bishe.utils.ImageUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.List;


import javax.servlet.http.HttpServletRequest;


/**
 * Created by ZYL on 2019/3/4.
 */
@Controller
public class AdminActivityController {
    Logger log = LoggerFactory.getLogger(AdminActivityController.class);
    @Autowired
    ActivityService activityService;
    @Autowired
    StudentService studentService;
    @Autowired
    StudentClassService studentClassService;

    @GetMapping("/selectActivityListByStatusId")
    public String selectActivityListByStatusId(@PathParam("statusId") String statusId, ModelMap modelMap) {
        log.info("活动状态"+statusId);
        PageInfo<Activity> pageInfo = activityService.selectActivityListByStatusId(statusId);
        modelMap.put("pageInfo", pageInfo);
        log.info("分页数据" + pageInfo);
        //根据传入的活动状态id，回到相应的页面
        if(statusId.equals("2")){
            return "admin/activityenrolling";
        }
        if(statusId.equals("3")){
            return "admin/activitypreparing";
        }if(statusId.equals("4")){
            return "admin/activityholding";
        }
        return "admin/activityend";
    }
    @PostMapping("/pageShowByStatusId")
    @ResponseBody
    public PageInfo<Activity> pageShowByStatusId(@PathParam("pageNum") int pageNum, @PathParam("statusId") String statusId) {
        log.info("接收的参数" + pageNum + "==" + statusId);
        PageInfo<Activity> pageInfo = activityService.pageShowByStatusId(pageNum,statusId);
        log.info("结果数据" + pageInfo);
        return pageInfo;
    }

    @GetMapping("/exportStudentDataByActivity")
    public String exportStudentDataByActivity(@PathParam("actiId") String actiId, ModelMap modelMap) {
        log.info("接收的参数" + actiId);
        PageInfo<Student> pageInfo = studentService.selectStudentListByActivityId(actiId);
        for (Student student : pageInfo.getList()) {
            //封装学生的学生班级数据
            if (student.getStudClassID() != null) {
                StudentClassInfo studentClassInfo = studentClassService.selectStudentClassInfo(student.getStudClassID());
                student.setStudentClassInfo(studentClassInfo);
            }
        }
        log.info("查询的数据" + pageInfo);
        //根据id查询出活动具体详情
        Activity activity = activityService.selectActivityById(actiId);
        //查询出所有的年级返回到报名信息页面
        List<StudentClass> gradeList = studentClassService.selectGrades();
        modelMap.put("gradeList", gradeList);
        modelMap.put("pageInfo", pageInfo);
        modelMap.put("activity", activity);
        return "admin/exportstudenttoexcel";
    }

    //根据前端的班级信息id，来筛选学生，班级信息id有三种可能：年级id，学院id，班级id，由于这三种id的(字符串)长度不同，分别2、3、4位
    //根据Service层根据字符串的长度不同决定dao层不同的查询方式
    @PostMapping("/selectExcelStudentByClassInfoId")
    @ResponseBody
    public ExportStudentShow selectExcelStudentByClassInfoId(@PathParam("gradeId")String gradeId, @PathParam("departmentId")String departmentId, @PathParam("classId")String classId, @PathParam("actiId")String actiId ){
        ExportStudentShow exportStudentShow = studentClassService.selectExcelStudentByClassInfoId(gradeId,departmentId,classId,actiId);
        return exportStudentShow;
    }
    @GetMapping("/exportStudentsToExcel")
    public void exportStudentsToExcel( @PathParam("actiId") String actiId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //service层中封装好要导出的学生数组
        List<ExcelExportStudent> exportStudentList = studentService.exportStudentToExcel(actiId);
        //活动
        Activity activity = activityService.selectActivityById(actiId);
        //每张表打印的数据条数pageCount(这里第一张sheet表最多可以打印35条数据(由于有标题)，其他的sheet1表最多可显示38条)
        // 所以需要对每页sheet显示数据条数的参数pageCount进行校验(如果pageCount为0时,则程序无响应)
        // pageCount每张sheet表显示的数据条数（A4纸能容纳36条数据）
        MyExportExcelUtil.myExportExcelMethod(exportStudentList,activity,request,response,36);
    }

    @PostMapping("/selectExcelStudentByGradeId")
    @ResponseBody
    public ExportStudentShow selectExcelStudentByGradeId(@PathParam("gradeId")String gradeId, @PathParam("actiId")String actiId,@PathParam("statusId") String statusId,HttpServletRequest request ){
        //存入session为了在exportPresentStudentsToExcel方法中使用导出筛选的结果到excel
        //根据班级筛选时是先根据年级筛选的所有在这里进行年级筛选时创建classInfoId对象，后面的院系筛选和专业班级筛选都使用这个对象，最终在exportPresentStudentsToExcel方法中使用
        if (statusId.equals("2")){
            log.info("活动状态报名中");
            ClassInfoId enrollingClassInfoId = new ClassInfoId();
            enrollingClassInfoId.setGradeId(gradeId);
            request.getSession().setAttribute("enrollingClassInfoId",enrollingClassInfoId);
        }
        if (statusId.equals("3")){
            log.info("活动状态筹办中");
            ClassInfoId preparingClassInfoId = new ClassInfoId();
            preparingClassInfoId.setGradeId(gradeId);
            request.getSession().setAttribute("preparingClassInfoId",preparingClassInfoId);
        }
        if (statusId.equals("4")){
            log.info("活动状态举办中");
            ClassInfoId holdingClassInfoId = new ClassInfoId();
            holdingClassInfoId.setGradeId(gradeId);
            request.getSession().setAttribute("holdingClassInfoId",holdingClassInfoId);
        }
        if (statusId.equals("5")){
            log.info("活动已结束");
            ClassInfoId endClassInfoId = new ClassInfoId();
            endClassInfoId.setGradeId(gradeId);
            request.getSession().setAttribute("endClassInfoId",endClassInfoId);
        }
        //存入session为了在exportPresentStudentsToExcel方法中使用导出筛选的结果到excel
        ExportStudentShow exportStudentShow = studentClassService.selectExcelStudentByGradeId(gradeId,actiId);
        return exportStudentShow;
    }

    @PostMapping("/selectExcelStudentByDepartmentId")
    @ResponseBody
    public ExportStudentShow selectExcelStudentByDepartmentId(@PathParam("gradeId")String gradeId,@PathParam("departmentId")String departmentId, @PathParam("actiId")String actiId,@PathParam("statusId") String statusId,HttpServletRequest request ){
        //从session中取出在进行年级筛选时存入的classInfoId对象，将传入的筛选信息更新到这个对象中，然后在存进去，以便在exportPresentStudentsToExcel中使用
        if (statusId.equals("2")){
            ClassInfoId enrollingClassInfoId = (ClassInfoId) request.getSession().getAttribute("enrollingClassInfoId");
            enrollingClassInfoId.setGradeId(gradeId);
            enrollingClassInfoId.setDepartmentId(departmentId);
            request.getSession().setAttribute("enrollingClassInfoId",enrollingClassInfoId);
        }
        if (statusId.equals("3")){
            ClassInfoId preparingClassInfoId = (ClassInfoId)request.getSession().getAttribute("preparingClassInfoId");
            preparingClassInfoId.setGradeId(gradeId);
            preparingClassInfoId.setDepartmentId(departmentId);
            request.getSession().setAttribute("preparingClassInfoId",preparingClassInfoId);
        }
        if (statusId.equals("4")){
            ClassInfoId holdingClassInfoId = (ClassInfoId) request.getSession().getAttribute("holdingClassInfoId");
            holdingClassInfoId.setGradeId(gradeId);
            holdingClassInfoId.setDepartmentId(departmentId);
            request.getSession().setAttribute("holdingClassInfoId",holdingClassInfoId);
        }
        if (statusId.equals("5")){
            ClassInfoId endClassInfoId = (ClassInfoId) request.getSession().getAttribute("endClassInfoId");
            endClassInfoId.setGradeId(gradeId);
            endClassInfoId.setDepartmentId(departmentId);
            request.getSession().setAttribute("endClassInfoId",endClassInfoId);
        }
        ExportStudentShow exportStudentShow = studentClassService.selectExcelStudentByDepartmentId(gradeId,departmentId,actiId);
        return exportStudentShow;
    }
    @PostMapping("/selectExcelStudentByClassId")
    @ResponseBody
    public ExportStudentShow selectExcelStudentByClassId(@PathParam("departmentId")String departmentId,@PathParam("classId")String classId, @PathParam("actiId")String actiId,@PathParam("statusId") String statusId,HttpServletRequest request ){
        //存入session为了在exportPresentStudentsToExcel方法中使用导出筛选的结果到excel
        //从session中取出在进行年级筛选时存入的classInfoId对象，将传入的筛选信息更新到这个对象中，然后在存进去，以便在exportPresentStudentsToExcel中使用
        if (statusId.equals("2")){
            ClassInfoId enrollingClassInfoId = (ClassInfoId) request.getSession().getAttribute("enrollingClassInfoId");
            enrollingClassInfoId.setGradeId(classId);
            enrollingClassInfoId.setDepartmentId(departmentId);
            request.getSession().setAttribute("enrollingClassInfoId",enrollingClassInfoId);
        }
        if (statusId.equals("3")){
            ClassInfoId preparingClassInfoId = (ClassInfoId)request.getSession().getAttribute("preparingClassInfoId");
            preparingClassInfoId.setGradeId(classId);
            preparingClassInfoId.setDepartmentId(departmentId);
            request.getSession().setAttribute("preparingClassInfoId",preparingClassInfoId);
        }
        if (statusId.equals("4")){
            ClassInfoId holdingClassInfoId = (ClassInfoId) request.getSession().getAttribute("holdingClassInfoId");
            holdingClassInfoId.setGradeId(classId);
            holdingClassInfoId.setDepartmentId(departmentId);
            request.getSession().setAttribute("holdingClassInfoId",holdingClassInfoId);
        }
        if (statusId.equals("5")){
            ClassInfoId endClassInfoId = (ClassInfoId) request.getSession().getAttribute("endClassInfoId");
            endClassInfoId.setGradeId(classId);
            endClassInfoId.setDepartmentId(departmentId);
            request.getSession().setAttribute("endClassInfoId",endClassInfoId);
        }
        ExportStudentShow exportStudentShow = studentClassService.selectExcelStudentByClassId(departmentId,classId,actiId);
        return exportStudentShow;
    }
    @PostMapping("/exportStudentPageShow")
    @ResponseBody
    public ExportStudentShow exportStudentPageShow(@PathParam("gradeId") String gradeId, @PathParam("departmentId") String departmentId, @PathParam("classId") String classId, @PathParam("actiId") String actiId, @PathParam("pageNum") int pageNum, HttpServletRequest request){
        log.info("来了"+gradeId+"===="+departmentId+"==="+classId+"==="+pageNum+"==="+actiId);
        ExportStudentShow exportStudentShow = studentClassService.selectExcelStudentByPage(gradeId, departmentId, classId, actiId ,pageNum);
        //分页数据
        //存入session为了在exportPresentStudentsToExcel方法中使用导出筛选的结果到excel
        request.getSession().setAttribute("gradeId",gradeId);
        request.getSession().setAttribute("departmentId",departmentId);
        request.getSession().setAttribute("classId",classId);
        log.info("分页数据"+exportStudentShow);
        return exportStudentShow;
    }

    @GetMapping("/exportPresentStudentsToExcel")
    //根据年级、院系、班级、筛选出来的结果，将数据导出excel表格
    public void  exportPresentStudentsToExcel(@PathParam("actiId") String actiId, @PathParam("actiStatusId") String statusId, HttpServletResponse response,HttpServletRequest request) throws Exception {
        log.info("接收的活动状态"+statusId);
        ClassInfoId classInfoId = new ClassInfoId();
        if (statusId.equals("2")){
            classInfoId = (ClassInfoId) request.getSession().getAttribute("enrollingClassInfoId");
        }
        if (statusId.equals("3")){
            classInfoId = (ClassInfoId)request.getSession().getAttribute("preparingClassInfoId");
        }
        if (statusId.equals("4")){
            classInfoId = (ClassInfoId) request.getSession().getAttribute("holdingClassInfoId");
        }
        if (statusId.equals("5")){
            classInfoId = (ClassInfoId) request.getSession().getAttribute("endClassInfoId");
        }
        //判断班级信息classInfoId是否为空，如果在导出学生信息时未进行任何年级、院系、班级条件筛选时，classInfoId为空，此时则根据活动id查询全部
        if(classInfoId!=null){
            String gradeId = classInfoId.getGradeId();
            String departmentId =classInfoId.getDepartmentId();
            String classId = classInfoId.getClassId();
            log.info("导出当前活动" + gradeId + "==" + departmentId+"=="+classId+"=="+actiId);
            //活动
            Activity activity = activityService.selectActivityById(actiId);
            List<ExcelExportStudent> exportStudentList=null;
            //如果用户没有点击任何筛选操作则session中没有任何班级id信息，此时根据活动id导出所有报名信息
            if(gradeId==null  && departmentId==null && classId==null){
                log.info("没班级信息");
                exportStudentList = studentService.exportStudentToExcel(actiId);
            }else {
                exportStudentList = studentService.exportPresentStudentsToExcel(gradeId,departmentId,classId,actiId);
                log.info("有班级信息");
            }
            log.info("导出的活动"+activity+"导出的数据"+exportStudentList);
            MyExportExcelUtil.myExportExcelMethod(exportStudentList,activity,request,response,4);
        }else {
            //活动
            Activity activity = activityService.selectActivityById(actiId);
            List<ExcelExportStudent> exportStudentList = studentService.exportStudentToExcel(actiId);
            //pageCount每张sheet表显示的数据条数（A4纸能容纳36条数据）
            MyExportExcelUtil.myExportExcelMethod(exportStudentList,activity,request,response,4);

        }
    }

    @PostMapping("/showActivityDetailByActiId")
    @ResponseBody
    public Activity showActivityDetailByActiId(@PathParam("actiId") String actiId){
        log.info("接收的id"+actiId);
        Activity activity = activityService.selectActivityById(actiId);
        log.info("返回的数据"+activity);
        return activity;
    }
    @PostMapping("/changeLevelByActivityId")
    @ResponseBody
    public Boolean changeLevelByActivityId(@PathParam("actiId") String actiId,@PathParam("changeLevel") String changeLevel){
        log.info("接收的id"+actiId+"=="+changeLevel);
        int i = activityService.changeLevelByActivityId(changeLevel, actiId);
        if (i==1){
            return true;
     }
     return false;

    }
    @PostMapping("/deleteActivityById")
    @ResponseBody
    public Boolean deleteActivityById(@PathParam("actiId") String actiId){
        //先根据id查询出活动，删除活动的主题图片
        Activity activity = activityService.selectActivityById(actiId);
        ImageUtil.myDeleteImage(activity.getActiThemeImage());
        //deletePublisghById是删除还在审核中的活动
        int i= activityService.deletePublishById(actiId);
        log.info("来了id"+actiId);
        if (i==1){
            return true;
        }
        return false;
    }



}


