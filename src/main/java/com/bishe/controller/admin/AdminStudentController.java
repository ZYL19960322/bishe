package com.bishe.controller.admin;

import com.bishe.dto.AdminStudentAndClassList;
import com.bishe.dto.StudentClassInfo;
import com.bishe.dto.StudentInfoResponse;
import com.bishe.pojo.Student;
import com.bishe.pojo.StudentClass;
import com.bishe.service.StudentClassService;
import com.bishe.service.StudentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
 * Created by ZYL on 2019/3/2.
 */
@Controller
public class AdminStudentController {
    Logger log=LoggerFactory.getLogger(AdminStudentController.class);

    @Autowired
    StudentService studentService;
    @Autowired
    StudentClassService studentClassService;
    @GetMapping("/adStudentInfo")
    public String adStudentInfo(ModelMap modelMap,HttpServletRequest request){
        PageInfo<Student> studentPageInfo = studentService.selectAllStudentPageShow();
        //将原始数据存在session中,以便在pageBeforeAndAfter方法中使用
        request.getSession().setAttribute("studentPageInfo",studentPageInfo);
        //获取分页数据
        List<Student> studentList = studentPageInfo.getList();
        //获取所有数据的总数
        long total = studentPageInfo.getTotal();
        //查询出所有的年级返回到报名信息页面
        List<StudentClass> gradeList = studentClassService.selectGrades();
        modelMap.put("gradeList", gradeList);
        modelMap.put("studentList",studentList);
        modelMap.put("count",total);
        return "admin/studentinfo";
    }

    @GetMapping("/selectDepartmentByGradeId")
    @ResponseBody
    //根据级联查询报名信息页面的班级下拉框选择
    public AdminStudentAndClassList selectDepartmentByGradeId(@PathParam("gradeId") String gradeId,HttpServletRequest request) {
        log.info("接收到的父级id" + gradeId);
        //如果年级id为空则查询全部
        //根据年级id查询出所有的该年级的学院返回前端下拉框显示
        List<StudentClass> studentClassList = studentClassService.selectDepartmentByParentId(gradeId);
        //根据年级id查询出属于该年级的所有学生在页面上展示
        PageInfo<Student> studentPageInfo = studentClassService.selectStudentListByGradeId(gradeId);

        //将原始数据存在session中,以便在pageBeforeAndAfter方法中使用
        request.getSession().setAttribute("studentPageInfo",studentPageInfo);
        //学生数据分页显示
        List<Student>studentList = studentPageInfo.getList();
        for(Student student : studentList){
            //遍历学生，根据学生的班级id查询出学生的班级信息
            if(student.getStudClassID()!=null){
                StudentClassInfo studentClassInfo = studentClassService.selectStudentClassInfo(student.getStudClassID());
                student.setStudentClassInfo(studentClassInfo);
            }
            //如果学生的id为空，则将学生的班级信息置为空字符串(null在页面上显示不会看，使用空字符串即空白代替显示)
            else student.setStudentClassInfo(new StudentClassInfo("",""));
        }
        //将学生列表、学院列表、学生数量封装到实体类中在前端页面显示
        AdminStudentAndClassList adminStudentAndClassList = new AdminStudentAndClassList(studentList, studentClassList,studentPageInfo.getTotal());
        //modelMap.put("departmentList", departmentList);
        log.info("查询出的学院" + studentClassList);
        return adminStudentAndClassList;
    }


    @GetMapping("/selectClassByDepartmentId")
    @ResponseBody
    //根据级联查询报名信息页面的班级下拉框选择
    public AdminStudentAndClassList  selectClassByDepartmentId(@PathParam("departmentId") String departmentId,HttpServletRequest request) {
        log.info("传来的参数"+"==="+departmentId);
        //根据院系id查询出所有的该学院的班级返回前端下拉框显示
        List<StudentClass> studentClassList = studentClassService.selectDepartmentByParentId(departmentId);
        //根据年级id和院系id查询出属于年级的该院系的所有学生
        PageInfo<Student> studentPageInfo = studentClassService.selectStudentListByDepartmentId(departmentId);
        //将原始数据存在session中,以便在pageBeforeAndAfter方法中使用
        request.getSession().setAttribute("studentPageInfo",studentPageInfo);
        List<Student>studentList = studentPageInfo.getList();

        for(Student student : studentList){
                 StudentClassInfo studentClassInfo = studentClassService.selectStudentClassInfo(student.getStudClassID());
                 student.setStudentClassInfo(studentClassInfo);
            }
        //将学生列表、专业班级列表、学生数量封装到实体类中在前端页面显示
        AdminStudentAndClassList adminStudentAndClassList = new AdminStudentAndClassList(studentList, studentClassList,studentPageInfo.getTotal());
        return adminStudentAndClassList;
    }


    @GetMapping("/selectStudentListByClassId")
    @ResponseBody
    //根据班级id查询学生（与之前根据年级院系不同，此时不用级联查询班级表，因为学生表中存储的是班级id，直接查询学生表即可）
    public AdminStudentAndClassList  selectStudentListByClassId(@PathParam("classId") String classId,HttpServletRequest request) {
        PageInfo<Student> studentPageInfo = studentService.selectStudentListByClassId(classId);
        //将原始数据存在session中,以便在pageBeforeAndAfter方法中使用
        request.getSession().setAttribute("studentPageInfo",studentPageInfo);
        List<Student>studentList = studentPageInfo.getList();
        for(Student student : studentList){
            StudentClassInfo studentClassInfo = studentClassService.selectStudentClassInfo(student.getStudClassID());
            student.setStudentClassInfo(studentClassInfo);
        }
        AdminStudentAndClassList adminStudentAndClassList = new AdminStudentAndClassList(studentList, studentPageInfo.getTotal());
        return  adminStudentAndClassList;
    }

    @PostMapping("/pageShow")
    @ResponseBody
    public  List<Student> pageShow(@PathParam("gradeId") String gradeId, @PathParam("departmentId") String departmentId, @PathParam("classId") String classId, @RequestParam(value ="pageNum",defaultValue="1") int pageNum, HttpServletRequest request){

        log.info("接收的数据"+"=="+pageNum+"=="+gradeId+"=="+departmentId+"=="+classId);
        PageInfo<Student> studentPageInfo = studentService.selectStudentListPageShow(gradeId, departmentId, classId, pageNum);
        //分页查询返回结果，获取结果中的数据
        List<Student> studentList = studentPageInfo.getList();
        //将原始数据存在session中,以便在pageBeforeAndAfter方法中使用
        request.getSession().setAttribute("studentPageInfo",studentPageInfo);
        //返回最后的想要的分页结果
        log.info("原始数据"+studentPageInfo);
        log.info("学生结果数据"+studentList.get(0).toString()+"班级信息"+studentList.get(0).getStudentClassInfo().toString());
       return studentList;
    }

    @PostMapping("/pageBeforeAndAfter")
    @ResponseBody
    public AdminStudentAndClassList pageBeforeAndAfter(@PathParam("gradeId") String gradeId, @PathParam("departmentId") String departmentId, @PathParam("classId") String classId,@PathParam("pageNum") int pageNum,HttpServletRequest request ){
        log.info("接收的参数"+pageNum);
        PageInfo<Student> studentPageInfo = (PageInfo<Student>) request.getSession().getAttribute("studentPageInfo");
        //上一页传来的标志参数为0，如何接收的参数为0则表示显示上一页数据，则从session取出上一次访问此方法的数据，得到跟上一次访问相比的上一页的真实数据，下一页也是此原理
        if(pageNum==0){
            pageNum=studentPageInfo.getPrePage();
            log.info("上一页"+pageNum);
        }
        if(pageNum==5){
            pageNum=studentPageInfo.getNextPage();
            log.info("下一页"+pageNum);
        }
        PageInfo<Student> studentPageInfo1 = studentService.selectStudentListPageShow(gradeId, departmentId, classId, pageNum);
        //分页查询返回结果，获取结果中的数据
        List<Student> studentList = studentPageInfo1.getList();
        //将原始数据存在session中,以便在下一次使用
        request.getSession().setAttribute("studentPageInfo",studentPageInfo1);
        //返回最后的想要的分页结果
        log.info("原始数据"+studentPageInfo);
//        log.info("学生结果数据"+studentList.get(0).toString()+"班级信息"+studentList.get(0).getStudentClassInfo().toString());
        return new AdminStudentAndClassList(studentList,Long.valueOf(pageNum));
    }

    //注销学生
    @PostMapping("/cancelStudentByStudId")
    @ResponseBody
    public Boolean cancelStudentByStudId(@PathParam("studId") String studId){
      return  studentService.cancelStudentByStudId(studId);
    }




    @GetMapping("/cancelStudent")
    public String cancelStudent(ModelMap modelMap) {
        PageInfo<Student> pageInfo = studentService.selectCancelStudentList();
        modelMap.put("pageInfo",pageInfo);
        return "admin/cancelstudent";
    }
    @PostMapping("/pageShowCancelStudent")
    @ResponseBody
    public PageInfo<Student> pageShowCancelStudent(@PathParam("pageNum") int pageNum){
        PageInfo<Student> pageInfo = studentService.pageShowCancelStudent(pageNum);
        return pageInfo;
    }

    @PostMapping("/deleteStudentByStudId")
    @ResponseBody
    public Boolean deleteStudentByStudId(@PathParam("studId")String studId){
        return studentService.deleteStudentBystudId(studId);
    }

    @PostMapping("/selectStudentDetailBystudId")
    @ResponseBody
    public StudentInfoResponse selectStudentDetailBystudId(@PathParam("studId")String studId ){
        log.info("活动id"+studId);
        return studentService.selectStudentDetailBystudId(studId);
    }

}



