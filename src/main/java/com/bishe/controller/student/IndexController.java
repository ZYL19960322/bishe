package com.bishe.controller.student;

import com.bishe.dto.StudentClassInfo;
import com.bishe.dto.StudentInfoResponse;
import com.bishe.enums.ActivityStatusEnum;
import com.bishe.dto.IndexRequest;
import com.bishe.dto.IndexResponse;
import com.bishe.enums.EnrollStatusEnum;
import com.bishe.enums.StudentInfoEnum;
import com.bishe.pojo.*;
import com.bishe.service.*;
import com.bishe.utils.MyBeanUtil;
import com.bishe.utils.TimeUtils;
import com.bishe.utils.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Created by ZYL on 2019/2/15.
 */
@Controller
public class IndexController {
    Logger log = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    StudentService studentService;

    @Autowired
    ActivityService activityService;

    @Autowired
    ActivityStatusService activityStatusService;

    @Autowired
    ActivityCategoryService activityCategoryService;

    @Autowired
    DataInfoService dataInfoService;

    @Autowired
    StudentClassService studentClassService;


    @PostMapping("/indexRequest")
    @ResponseBody
    public IndexResponse indexSelect(IndexRequest indexRequest, IndexResponse indexResponse) {
        log.info("这是接受到的参数" + indexRequest);
        //处理接受到的参数:将空字符串（未设置值的数据）设为null
        if (indexRequest.getActiEndEnrollTime().isEmpty()) {
            indexRequest.setActiEndEnrollTime(null);
        }
        if (indexRequest.getActiStartEnrollTime().isEmpty()) {
            indexRequest.setActiStartEnrollTime(null);
        }
        if (indexRequest.getActiCategory().isEmpty()) {
            indexRequest.setActiCategory(null);
        }
        if (indexRequest.getActiStatus().isEmpty()) {
            indexRequest.setActiStatus(null);
        }

        log.info("这是修改后的参数" + indexRequest);
        List<Activity> activityList = activityService.indexSelect(indexRequest);
        indexResponse.setActivityList(activityList);

        //2、查询出所有的活动状态
        //List<ActivityStatus> activityStatusList =activityStatusService.findAllActivityStatus();
        //log.info("这是活动状态"+activityStatusList);
        //indexResponse.setActivityStatusList(activityStatusList);

        //3、查询出所有的活动类型
        //List<ActivityCategory> activityCategoryList =activityCategoryService.findAllActivityCategory();
        //indexResponse.setActivityCategoryList(activityCategoryList);


        System.out.print("这是查询出的数据" + activityList);
        return indexResponse;
    }


    //首页根据关键字搜索活动
    @PostMapping("/indexSearch")
    @ResponseBody
    public IndexResponse indexSearch(@PathParam("searchCondition") String searchCondition, IndexResponse indexResponse) {

        log.info("这是接收的搜索参数" + searchCondition);
        //将搜索条件的首尾、中间空格去掉
        searchCondition = searchCondition.replace(" ", "");
        log.info("这是处理后的搜索参数" + searchCondition);
        List<Activity> activityList = activityService.indexSearch(searchCondition);
        indexResponse.setActivityList(activityList);

        //2、查询出所有的活动状态
        List<ActivityStatus> activityStatusList = activityStatusService.findAllActivityStatus();
        //log.info("这是活动状态"+activityStatusList);
        //indexResponse.setActivityStatusList(activityStatusList);
        //modelMap.put("activityStatusList",activityStatusList);

        //3、查询出所有的活动类型
        List<ActivityCategory> activityCategoryList = activityCategoryService.findAllActivityCategory();
        //log.info("这是活动分类"+activityCategoryList);
        //indexResponse.setActivityCategoryList(activityCategoryList);
        //modelMap.put("activityCategoryList",activityCategoryList);


        // System.out.print("这是查询出的数据"+activityList);
        // modelMap.put("indexResponse",indexResponse);
        return indexResponse;
    }

    @GetMapping("/indexAll")
    @ResponseBody
    public IndexResponse indexAll(IndexResponse indexResponse) {
        log.info("准备查询首页所有活动了========");
        List<Activity> activityList = activityService.findAllActivity();
        log.info("首页全部活动" + activityList);
        indexResponse.setActivityList(activityList);
        return indexResponse;
    }

    @GetMapping("/indexRecommend")
    @ResponseBody
    public IndexResponse indexRecommend(IndexResponse indexResponse) {
        log.info("准备查询首页推荐活动了========");
        List<Activity> activityList = activityService.indexRecommend();
        log.info("首页推荐活动" + activityList);
        indexResponse.setActivityList(activityList);
        return indexResponse;
    }


    @GetMapping("/indexHot")
    @ResponseBody
    public IndexResponse indexHot(IndexResponse indexResponse) {
        log.info("准备查询首页热门活动了========");
        List<Activity> activityList = activityService.indexHot();
        log.info("首页热门活动" + activityList);
        indexResponse.setActivityList(activityList);
        return indexResponse;
    }

    @GetMapping("/actiMessage")
    //@ResponseBody
    //根据活动ID查询出活动，使其显示在活动列表页面
    public String actiMessage(@PathParam("actiId") String actiId, HttpServletRequest request, ModelMap modelMap) {
        //1、从数据库中查询出活动
        Activity activity = activityService.selectActivityById(actiId);
        //将活动存入session中，将在学生完善（修改）报名信息的方法studComfirmEnroll中使用，实现报名的功能
        request.getSession().setAttribute("activity",activity);
        modelMap.put("activity",activity);
        //2、从session中取出学生
        Student student = (Student) request.getSession().getAttribute("student");

        /*=======分析过程=======
        //活动状态=报名中
        //1、是否已经报名：    是——>（取消报名，更改报名信息）
        //                    否——>
        //                    否——>(我要报名)
        //2、报名人数是否已满：是——>(活动报名人数已满)

        //活动状态=筹办中/举办中
        // 1、判断是否自己发布的：是——>(这是您发布的活动) 否——>(活动在筹办中)(活动在举办中)

        //活动状态=已结束（活动已结束）*/


        //如果活动是报名中
        if (activity.getActiStatusId() == ActivityStatusEnum.ACTIVITY_ENROLLING.getStatusId()) {
            //判断报名人数是否已报名
            List<String> studIdList = dataInfoService.selectStudIdListByActiId(actiId);
            for (int i = 0; i < studIdList.size(); i++) {
                if (student.getStudId().equals(studIdList.get(i))) {
                    modelMap.put("msg", EnrollStatusEnum.STATUS1.getStatusId());
                    //log.info(EnrollStatusEnum.ENROLL_REPEAT.getStatusDesc());
                    return "front/activitydetail";
                }
            }
            //判断报名人数是否已满
            if (activity.getActiMaxEnroll() <= activity.getActiNowEnroll()) {
                modelMap.put("msg", EnrollStatusEnum.STATUS3.getStatusId());
                return "front/activitydetail";
                //否则返回"我要报名"
            } else {
                modelMap.put("msg", EnrollStatusEnum.STATUS2.getStatusId());
                return "front/activitydetail";
            }
        }
        //活动在筹办中/举办中
        if (activity.getActiStatusId() == ActivityStatusEnum.ACTIVITY_HOLDING.getStatusId() || activity.getActiStatusId() == ActivityStatusEnum.ACTIVITY_PREPARING.getStatusId()) {
            //判断是否是自己发布的
            if (activity.getActiBuilderId().equals(student.getStudId())) {
                modelMap.put("msg", EnrollStatusEnum.STATUS4.getStatusId());
                return "front/activitydetail";
                //判断是在筹办中还是举办中
            } else if (activity.getActiStatusId() == ActivityStatusEnum.ACTIVITY_PREPARING.getStatusId()) {
                modelMap.put("msg", EnrollStatusEnum.STATUS5.getStatusId());
                return "front/activitydetail";
            } else {
                modelMap.put("msg", EnrollStatusEnum.STATUS6.getStatusId());
                return "front/activitydetail";
            }
        }
        //以上条件都不成立则活动就是已结束了
        modelMap.put("msg", EnrollStatusEnum.STATUS7.getStatusId());
        return "front/activitydetail";
    }


    @GetMapping("/selectDepartment")
    @ResponseBody
    //根据级联查询报名信息页面的班级下拉框选择
    public List<StudentClass> selectDepartmentByParentId(@PathParam("parentId") String parentId, ModelMap modelMap) {
        log.info("接收到的父级id" + parentId);
        //根据父级id查询出所有的学院返回到报名信息页面
        List<StudentClass> departmentList = studentClassService.selectDepartmentByParentId(parentId);
        modelMap.put("departmentList", departmentList);
        log.info("查询出的学院" + departmentList);
        return departmentList;

    }

    @GetMapping("/studEnroll")
    @ResponseBody
    //根据活动ID查询出活动，使其显示在活动列表页面
    public StudentInfoResponse studEnroll(@PathParam("actiId") String actiId, DataInfo dataInfo, HttpServletRequest request, ModelMap modelMap) {
        //1、从session中取出学生
        Student student = (Student) request.getSession().getAttribute("student");
        //判断学生信息是否完全
        StudentInfoResponse studentInfoResponse = studentService.comfirmStudentInfo(student.getStudName(), student.getStudPassword());
        log.info("判断学生信息是否完全" + studentInfoResponse);
        //如果信息完全则可以报名
        if (studentInfoResponse.getStatus() == StudentInfoEnum.INFO_SUCCESS.getStatusId()) { //2、从数据库中查询出活动
            Activity activity = activityService.selectActivityById(actiId);
            log.info("这是活动" + activity);
            if (student.getStudId().equals(activity.getActiBuilderId())) {
                //学生报名自己发布的活动
                dataInfo.setDataRelationId(2);
            } else {
                //学生报名不是自己发布的活动
                dataInfo.setDataRelationId(1);
            }
            //4封装数据的其他数据
            dataInfo.setDataActiId(activity.getActiId());
            dataInfo.setDataStudId(student.getStudId());
            dataInfo.setDataCreateTime(TimeUtils.getStringDate());
            dataInfo.setDataId(UUIDUtils.getId());
            log.info("这是数据" + dataInfo);

            //5、将数据插入到数据库中的data表
            dataInfoService.addDataInfo(dataInfo);
            //6、将活动的报名人数增加1
            activityService.addEnrollStudent(actiId);
            log.info("报名成功");
        }
        return studentInfoResponse;

    }


    @GetMapping("/deleteEnroll")
    @ResponseBody
    //根据活动ID查询出活动，使其显示在活动列表页面
    public StudentInfoResponse deleteEnroll(@PathParam("actiId") String actiId, HttpServletRequest request, StudentInfoResponse studentInfoResponse) {
        //1、从session中取出学生
        Student student = (Student) request.getSession().getAttribute("student");
        //判断学生信息是否完全
        int i = dataInfoService.deleteEnroll(student.getStudId(), actiId);
        studentInfoResponse.setStatus(i);
        log.info("删除结果" + i);
        return studentInfoResponse;
    }


    @GetMapping("/toIndexPage")
    //@ResponseBody
    //报名成功回到首页，避免用户重复提交
    public String toIndexPage(ModelMap modelMap, IndexResponse indexResponse) {
        //回到首页，将需要的数据带回首页
        //1、活动列表
        List<Activity> activityList = activityService.findAllActivity();
        indexResponse.setActivityList(activityList);

        //2、查询出所有的活动状态
        List<ActivityStatus> activityStatusList = activityStatusService.findAllActivityStatus();
        indexResponse.setActivityStatusList(activityStatusList);

        //3、查询出所有的活动类型
        List<ActivityCategory> activityCategoryList = activityCategoryService.findAllActivityCategory();
        indexResponse.setActivityCategoryList(activityCategoryList);

        modelMap.put("indexResponse", indexResponse);
        return "front/index";
    }

    @GetMapping("/studentInfo")
    //当学生的班级id不为空时，前端页面显示的效果不太好，需完善
    //去完善个人信息
    public String studentInfo(@PathParam("actiId") String actiId, HttpServletRequest request, ModelMap modelMap) {

        log.info("接收到的id" + actiId);
        //将学生要报名参加的活动的活动id存入到session中，页面定向到输入学生报名信息页面
        request.getSession().setAttribute("EnrollActiId", actiId);
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

        return "front/enrollinfo";
    }

    /*
    原始方法，经测试成功
    @PostMapping("/studComfirmEnroll")
    public  String studComfirmEnroll(@PathParam("studNumber") String studNumber,@PathParam("studName") String studName,
                                     @PathParam("studPhone") String studPhone,@PathParam("classId") String classId){
        log.info("接收的学生参数"+studName+"=="+studNumber+"=="+studPhone+"=="+classId);
        //验证接收到学生数据的有效性
        //将学生信息插入到学生的数据库中
        //从数据库中验证学生信息的完整性
        //从session中取出学生，和活动
        //封装datainfo数据
        //将datainfo插入数据库

        return null;

    }*/

    //使用MyBeanUtil（亮点：两个相同类的不同对象的属性值合并到一个对象当中）的方法
    @PostMapping("/studComfirmEnroll")
    @ResponseBody
    public StudentInfoResponse studComfirmEnroll(Student student1, HttpServletRequest request, DataInfo dataInfo) {
        //log.info("接收的学生参数"+studName+"=="+studNumber+"=="+studPhone+"=="+classId);
        log.info("使用MyBeanUtil的方法" + student1);
        //从session中取出学生
        Student student2 = (Student) request.getSession().getAttribute("student");
        log.info("session中的对象" + student2);
        Student student = (Student) MyBeanUtil.combineSydwCore(student1, student2);
        log.info("合并对象的结果" + student);
        //1、验证接收到学生数据的有效性
        //如果学生的信息不合理，直接返回错误信息，合理则继续执行下列操作
        log.info("来这了");
        StudentInfoResponse studentInfoResponse = studentService.testStudentInfo(student);
        if (studentInfoResponse.getStatus() != StudentInfoEnum.INFO_SUCCESS.getStatusId()) {
            return studentInfoResponse;
        }

        //2、将学生报名信息插入(更新)到学生的数据库中
        studentService.changeStudentInfo(student);

        //从session中取出actiMessage方法存入session的活动,即当前活动
        Activity activity = (Activity) request.getSession().getAttribute("activity");

        //封装datainfo数据
        if (student.getStudId().equals(activity.getActiBuilderId())) {
            //学生报名自己发布的活动
            dataInfo.setDataRelationId(2);
        } else {
            //学生报名不是自己发布的活动
            dataInfo.setDataRelationId(1);
        }
        dataInfo.setDataActiId(activity.getActiId());
        dataInfo.setDataStudId(student.getStudId());
        dataInfo.setDataCreateTime(TimeUtils.getStringDate());
        dataInfo.setDataId(UUIDUtils.getId());
        log.info("这是数据" + dataInfo);

        //5、将datainfo插入数据库的data表
        dataInfoService.addDataInfo(dataInfo);
        //6、将活动的报名人数增加1
        activityService.addEnrollStudent(activity.getActiId());
        //log.info("修改信息时报名成功");
        return studentInfoResponse;
    }


}
