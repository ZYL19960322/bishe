package com.bishe.controller.student;


import com.bishe.dto.EnrollStudent;
import com.bishe.dto.IndexResponse;
import com.bishe.dto.PublishResponse;
import com.bishe.dto.UserResponse;
import com.bishe.enums.ActivityStatusEnum;
import com.bishe.pojo.*;
import com.bishe.service.*;
import com.bishe.utils.*;;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * Created by ZYL on 2019/1/15.
 */
@Controller
public class StudPublishController {
    Logger log = LoggerFactory.getLogger(StudPublishController.class);

    @Autowired
    ActivityService activityService;
    @Autowired
    ActivityCategoryService activityCategoryService;
    @Autowired
    PreviewActivityService previewActivityService;
    @Autowired
    MessageService messageService;
    @Autowired
    RemindService remindService;
    @Autowired
    StudentService studentService;

    @GetMapping("/toPublishPage")
    public String toPublishPage(HttpServletRequest request, ModelMap modelMap) {
        Student student = (Student) request.getSession().getAttribute("student");
        UserResponse userResponse = activityService.toPublishPage(student.getStudId());
        modelMap.put("userResponse", userResponse);
        return "front/publish";
    }

    //去发布页面，需要携带的参数的是活动类型选择列表
    @RequestMapping("/toReleaseActivity")
    public String toReleasePage(ModelMap modelMap) {
        List<ActivityCategory> activityCategoryList = activityCategoryService.findAllActivityCategory();
        modelMap.put("activityCategoryList", activityCategoryList);
        return "front/release";
    }


    @GetMapping("/selectPublishActivityListBySign")
    public String selectPublishActivityListBySign(@PathParam("sign") int sign, ModelMap modelMap, HttpServletRequest request, IndexResponse indexResponse) {
        log.info("接收的sign参数" + sign);
        Student student = (Student) request.getSession().getAttribute("student");
        indexResponse.setActivityList(activityService.selectActivityListBySign(sign, student.getStudId()));
        modelMap.put("indexResponse", indexResponse);
        return "front/publishactivitylist";
    }


    @GetMapping("/publishActivityMessage")
    //@ResponseBody
    //根据活动ID查询出活动，使其显示在活动列表页面
    public String publishActivityMessage(@PathParam("actiId") String actiId, HttpServletRequest request, ModelMap modelMap) {
        log.info("actiMessageactiMessage" + actiId);
        //2、从数据库中查询出活动
        Activity activity = activityService.selectActivityById(actiId);
        log.info("活动详情" + activity);
        //前端取出活动状态的id根据活动的状态id来在活动详情页面提供学生不同的活动操作功能
        modelMap.put("activity", activity);
        //modelMap.put("msg",activity.getActiStatusId());
        log.info("这是活动状态ID" + activity.getActiStatusId());
        //return activity.getActiStatusId();
        return "front/publishactivitydetail";
    }


   // @PostMapping("/studentPublish")
    @ResponseBody
    public PublishResponse studPublish(@RequestParam("file") MultipartFile upFile, Activity activity,
                                       HttpServletRequest request, PublishResponse publishResponse) {

        if (activity.getActiTheme().isEmpty() || activity.getActiDesc().isEmpty() || activity.getActiAddress().isEmpty() || activity.getActiComment().isEmpty() || activity.getActiCategoryId() == null || activity.getActiPhone().isEmpty() || activity.getActiHolder().isEmpty() || activity.getActiStartTime().isEmpty() || activity.getActiEndrollTime().isEmpty() || activity.getActiMaxEnroll() == null) {
            //如果必填的信息填写不完全则返回并且带上失败信息
            publishResponse.setSuccess(false);
            publishResponse.setMsg("请检查必填参数是否填写完全");
            log.info("这是接收的数据" + activity + "======");
            return publishResponse;
        }
        //2、如果参数没有问题则存储图片
        String virtualPath = ImageUtil.myStoreActivityThemeImage(upFile, FileUtil.getThemeImageRelativePath());
        //将前端能取出图片的图片路径赋值到activity属性中
        activity.setActiThemeImage(virtualPath);
        //3、将其它值赋值到activity中
        //活动ID:使用UUID工具类生成
        activity.setActiId(UUIDUtils.getId());
        //从session中获取学生对象
        Student student = (Student) request.getSession().getAttribute("student");
        //学生即发布者
        activity.setActiBuilderId(student.getStudId());
        //活动建立时间
        activity.setActiBuildTime(TimeUtils.getStringDate());
        activity.setActiNowEnroll(0);

        //活动所有需要存储的值已完成，
        //4、将数据存入数据库
        log.info("这是申请发布的活动" + activity + "======");
        Integer id = activityService.studPublishActivity(activity);

        log.info("这是学生" + student);
        //4、返回前端
        //数据存储成功
        publishResponse.setSuccess(true);
        publishResponse.setMsg("申请发布成功");
        return publishResponse;
    }

    @PostMapping("/studentPublish")
    @ResponseBody
    public PublishResponse studentPublish(@RequestParam("file") MultipartFile upFile, Activity activity,
                                     HttpServletRequest request) {
        //1、验证提交的数据是否合理
        PublishResponse publishResponse = activityService.checkActivityInfo(activity);
        if (publishResponse.getSuccess() == false) {
            return publishResponse;
        }
        try {
            //2、如果参数没有问题则存储图片
            String virtualPath = ImageUtil.myStoreActivityThemeImage(upFile, FileUtil.getThemeImageRelativePath());
            //将前端能取出图片的图片路径赋值到activity属性中
            activity.setActiThemeImage(virtualPath);
            //3、将其它值赋值到activity中
            //活动ID:使用UUID工具类生成
            activity.setActiId(UUIDUtils.getId());
            //从session中获取学生对象
            Student student = (Student) request.getSession().getAttribute("student");
            //学生即发布者
            activity.setActiBuilderId(student.getStudId());
            //活动建立时间
            String actiBuildTime=TimeUtils.getStringDate();
            activity.setActiBuildTime(actiBuildTime);

            activity.setActiNowEnroll(0);
            //活动所有需要存储的值已完成，
            //4、将数据存入数据库
            log.info("这是申请发布的活动" + activity + "======");
            activityService.studPublishActivity(activity);
            //向数据的message表中添加一条message数据，和根据这条申请审核信息向admin_message表中给所有管理员添加一条adminMessage数据，这个在service层中实现
            Message message = new Message();
            message.setMessageId(UUIDUtils.getId());
            message.setMessageActiId(activity.getActiId());
            message.setMessageBuilderId(student.getStudId());
            message.setMessageCreateTime(actiBuildTime);
            //初次申请发布活动，message的messageActiStatus与活动状态一致为审核中值为1(从枚举型取值)
            message.setMessageActiStatus(ActivityStatusEnum.ACTIVITY_CHECKING.getStatusId());
            messageService.addAMessage(message);
        } catch (Exception e) {
            publishResponse.setSuccess(false);
            return publishResponse;
        }
        publishResponse.setMsg("申请发布成功");
        return publishResponse;
    }

    @PostMapping("/preview")
    @ResponseBody
    public PublishResponse preview(@RequestParam("file") MultipartFile upFile, Activity activity,
                                   HttpServletRequest request,ModelMap modelMap) {
        //1、验证提交的数据是否合理
        PublishResponse publishResponse = activityService.checkActivityInfo(activity);
        if (publishResponse.getSuccess() == false) {
            return publishResponse;
        }
        try {
            //能存储但不能从从预览活动图片文件夹下移动活动文件夹下：故将预览活动图片存储到活动图片中
            //2、如果参数没有问题则存储图片
            String virtualPath = ImageUtil.myStoreActivityThemeImage(upFile,FileUtil.getThemeImageRelativePath());
            //将前端能取出图片的图片路径赋值到activity属性中
            activity.setActiThemeImage(virtualPath);
            //3、将其它值赋值到activity中
            //活动ID:使用UUID工具类生成
            activity.setActiId(UUIDUtils.getId());
            //从session中获取学生对象
            Student student = (Student) request.getSession().getAttribute("student");
            //学生即发布者
            activity.setActiBuilderId(student.getStudId());
            //活动建立时间
            activity.setActiBuildTime(TimeUtils.getStringDate());
            activity.setActiNowEnroll(0);
            //活动所有需要存储的值已完成，
            //4*、将数据存入数据库前先判断数据库中是否有存储有自己的之前的预览活动，如果有则先删除(原则是预览表一个学生只能存在一条数据)
            Activity activity1= previewActivityService.selectPreviewActivityByStudId(student.getStudId());
            if(activity1!=null){

                //1、删除文件夹下对应的活动主题图片
                ImageUtil.myDeleteImage(activity1.getActiThemeImage());
                //2、删除activity_preview表中的活动数据
                previewActivityService.deletePreviewActivityByStudId(student.getStudId());
            }
            //没有则直接将这次的预览图片直接添加到表中
            log.info("这是申请预览的活动" + activity + "======");
            previewActivityService.storePreviewActivity(activity);
        } catch (Exception e) {
            publishResponse.setSuccess(false);
            return publishResponse;
        }
        modelMap.put("activity", activity);
        return publishResponse;
    }


    @GetMapping("/toPreview")
    //@ResponseBody
    public String toPreview(HttpServletRequest request,ModelMap modelMap) {
        //根据用户id查询出用户存储在数据库的预览活动带到前端页面显示
        Student student = (Student) request.getSession().getAttribute("student");
        Activity activity = previewActivityService.selectPreviewActivityByStudId(student.getStudId());
        modelMap.put("activity",activity);
        return "front/preview";
    }

    @GetMapping("/previewToPublish")
    @ResponseBody
    //确定要发布活动：
    public Boolean previewToPublish(HttpServletRequest request) {
        //1、根据学生id查询出预览表中的活动，将其添加到activity活动表,并且将该预览活动的主题图片移动到发布活动的文件下，返回的路径更新到活动中
        Student student = (Student) request.getSession().getAttribute("student");
        try { Activity activity=previewActivityService.selectPreviewActivityByStudId(student.getStudId());
            log.info("图片的先前路径"+activity.getActiThemeImage());
            //String newPath = ImageUtil.mytext(activity.getActiThemeImage(), FileUtil.getThemeImageRelativePath());
            //log.info("移动之后的路径"+newPath);
            //更新到活动中
            //activity.setActiThemeImage(newPath);
            log.info("最终存储的活动"+activity);
            activityService.studPublishActivity(activity);

            //向数据的message表中添加一条message数据，和根据这条申请审核信息向admin_message表中给所有管理员添加一条adminMessage数据，这个在service层中实现
            Message message = new Message();
            message.setMessageId(UUIDUtils.getId());
            message.setMessageActiId(activity.getActiId());
            message.setMessageBuilderId(student.getStudId());
            message.setMessageCreateTime(activity.getActiBuildTime());
            //初次申请发布活动，message的messageActiStatus与活动状态一致为审核中值为1(从枚举型取值)
            message.setMessageActiStatus(ActivityStatusEnum.ACTIVITY_CHECKING.getStatusId());
            messageService.addAMessage(message);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @PostMapping("/deletePublish")
    @ResponseBody
    public PublishResponse deletePublish(@PathParam("actiId") String actiId, PublishResponse publishResponse) {
        //取消发布活动将删除活动，即要删除数据库中的活动数据也要删除文件夹下的活动图片
        Activity activity = activityService.selectActivityById(actiId);
        try {
            //删除图片
            ImageUtil.myDeleteImage(activity.getActiThemeImage());
            //删除数据中的数据
            int i = activityService.deletePublishById(actiId);
            if (i != 0) {
                publishResponse.setSuccess(true);
                log.info("删除成功");
            }
        } catch (Exception e) {
            publishResponse.setSuccess(false);
            log.info("删除失败");
        }
        return publishResponse;
    }


    @PostMapping("/endEnroll")
    @ResponseBody
    //截止报名:即报名中———>筹办中====statusId:2———>3,设置活动的报名截止时间
    public int endEnroll(@PathParam("actiId") String actiId) {
        //1、查询出活动
        //2、改变活动的状态id和活动的截止时间
        Activity activity = activityService.selectActivityById(actiId);
        activity.setActiEndrollTime(TimeUtils.getStringDate());
        activity.setActiStatusId(ActivityStatusEnum.ACTIVITY_PREPARING.getStatusId());
        //3更新到数据库中
        return activityService.changeActivityInfo(activity);
    }

    @PostMapping("/holdActivity")
    @ResponseBody
    //举办活动，即筹办中——>举办中的功能===statusId:2———>3设置活动的开始时间
    public int holdActivity(@PathParam("actiId") String actiId) {
        //1、查询出活动
        //2、改变活动的状态id和活动的截止时间
        Activity activity = activityService.selectActivityById(actiId);
        activity.setActiStartTime(TimeUtils.getStringDate());
        activity.setActiStatusId(ActivityStatusEnum.ACTIVITY_HOLDING.getStatusId());
        //3更新到数据库中
        return activityService.changeActivityInfo(activity);
    }

    @PostMapping("/endActivity")
    @ResponseBody
    //结束活动:即举办中———>已结束====statusId:4———>5，设置活动结束时间
    public int endActivity(@PathParam("actiId") String actiId) {
        //1、查询出活动
        //2、改变活动的状态id和活动的截止时间
        Activity activity = activityService.selectActivityById(actiId);
        activity.setActiEndTime(TimeUtils.getStringDate());
        activity.setActiStatusId(ActivityStatusEnum.ACTIVITY_HASENDED.getStatusId());
        //3更新到数据库中
        return activityService.changeActivityInfo(activity);
    }


    @GetMapping("/toChangePublishActivity")
    //@ResponseBody
    public String toChangePublishActivity(@PathParam("actiId") String actiId, ModelMap modelMap) {
        //修改活动：带上要修改的活动和活动类型选择列表
        Activity activity = activityService.selectActivityById(actiId);
        modelMap.put("activity", activity);
        List<ActivityCategory> activityCategoryList = activityCategoryService.findAllActivityCategory();
        modelMap.put("activityCategoryList", activityCategoryList);
        log.info("这是活动" + activity);
        return "front/changepublish";
    }


    //修改发布的活动信息之修改活动主题图片
    @PostMapping("/changeActivityThemeImage")
    @ResponseBody
    //如果成功则返回sql语句受影响的行数，失败/发生异常则返回0
    public int changeActivityThemeImage(@RequestParam("file") MultipartFile
                                                actiThemeImageFile, @PathParam("actiId") String actiId, HttpServletRequest request) {
        log.info("接收的文件名" + FileUtil.getFileExtension(actiThemeImageFile) + "接收的活动id" + actiId);
        //根据活动id查询出活动数据
        //根据活动的主题图片地址将活动的主题图片删除
        //重新存储活动主题图片
        try {
            Activity activity = activityService.selectActivityById(actiId);
            ImageUtil.myDeleteImage(activity.getActiThemeImage());
            String actiThemeImage = ImageUtil.myStoreActivityThemeImage(actiThemeImageFile, FileUtil.getThemeImageRelativePath());
            return activityService.changeActivityThemeImage(actiId, actiThemeImage);
        } catch (Exception e) {
            return 0;
        }

    }

    //修改发布的活动信息之修改文字信息
    @PostMapping("/changeActivityInfo")
    @ResponseBody
    public int changeActivityInfo(Activity activity1) {
        Activity activity2 = activityService.selectActivityById(activity1.getActiId());
        log.info("修改前的活动" + activity2);
        //设置上修改的时间
        activity1.setActiChangeTime(TimeUtils.getStringDate());
        log.info("接收的活动" + activity1);
        Activity activity = (Activity) MyBeanUtil.combineSydwCore(activity1, activity2);
        log.info("合并后的活动" + activity);
        //判断此次修改的活动是初审核还是再审核活动，如果无审核通过记录则为初审核，有审核通过记录则为再审核，删除管理员对应这个活动的消息，再给管理员添加新的消息，
        // 如果活动为再审核且有报名参加者则给所有的报名者一条消息，开启在线的管理线程和开启与活动有关的并且在线的学生线程
        //原则：一条活动对应一条message消息N个管理员的N条admin_message消息和N个student_message(学生的消息可以有多条)
        messageService.addMessagesByChangeActivity(activity);
        //将活动的状态修改为审核中，更新到数据库中，审核通过之后再从消息表中取出原状态
        activity.setActiStatusId(ActivityStatusEnum.ACTIVITY_CHECKING.getStatusId());
        int i = activityService.changeActivityInfo(activity);
        return i;
    }

    @PostMapping("/sendremindMessage")
    @ResponseBody
    public Boolean sendremindMessage(@PathParam("actiId") String actiId,@PathParam("remindComment") String remindComment){
     log.info("活动id"+actiId+"消息内容"+remindComment);
      return remindService.addARemindMessage(actiId,remindComment) ;
}

    @GetMapping("/showEnrollStudent")
    //@ResponseBody
    public String showEnrollStudent(@PathParam("actiId") String actiId, ModelMap modelMap) {
        //根据活动id查询出所有的活动报名参加人数
        List<EnrollStudent> enrollStudentList = studentService.selectEnrollStudentByActiId(actiId);
        modelMap.put("enrollStudentList", enrollStudentList);
        modelMap.put("total",enrollStudentList.size());
        log.info("这是活动的报名者共有"+enrollStudentList.size()+"详情" + enrollStudentList);
        return "front/enrollstudent";
    }


}
