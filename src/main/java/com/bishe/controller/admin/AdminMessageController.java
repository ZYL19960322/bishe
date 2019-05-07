package com.bishe.controller.admin;

import com.bishe.dto.CheckAgainActivity;
import com.bishe.dto.RemindMessage;
import com.bishe.pojo.Activity;
import com.bishe.pojo.Admin;
import com.bishe.service.AdminMessageService;
import com.bishe.service.RemindService;
import com.bishe.service.StudentMessageService;
import com.github.pagehelper.PageInfo;
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
 * Created by ZYL on 2019/3/13.
 */
@Controller
public class AdminMessageController {
  Logger log=  LoggerFactory.getLogger(AdminActivityController.class);

    @Autowired
    AdminMessageService adminMessageService;
    @Autowired
    StudentMessageService studentMessageService;
    @Autowired
    RemindService remindService;

    @GetMapping("/selectCheckingActivityByAdminId")
    public String selectCheckingActivityByAdminId(HttpServletRequest request,ModelMap modelMap){
        //从session中取出管理员
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        //根据管理员的id查询出所有的未读、为审核的消息(审核活动)，然后管理员将自己的所有初审核消息的状态值为已读
        List<CheckAgainActivity> checkAgainActivityList = adminMessageService.selectCheckingActivityByAdminId(admin.getAdminId());
        modelMap.put("checkAgainActivityList",checkAgainActivityList);
        log.info("审核的活动"+checkAgainActivityList+"数量"+checkAgainActivityList.size());
        return "admin/message";
    }

    @PostMapping("/adminCheckActivityPass")
    @ResponseBody
    public Boolean adminCheckActivityPass(@PathParam("adminMessageId")String adminMessageId){
        Boolean result = studentMessageService.adminCheckActivityPass(adminMessageId);
        //根据活动id将这个活动的状态改为报名中
        //根据活动id和管理的id查询出申请审核活动时产生的消息进而查询出管理员对应的这个消息
        ////管理员将这个消息的状态值为已读并且添加消息处理信息
        //给对应的学生插入一条消息
        //判断这个学生是否在线，如果在线则执行这个学生的线程返回一个消息
        log.info("最终结果"+result);
        return result;
    }

    @PostMapping("/backMessage")
    @ResponseBody
    public Boolean backMessage(@PathParam("adminMessageId")String adminMessageId,@PathParam("messageContent")String messageContent){
        //将所有管理员下标记这条消息为已读
        //给在线的管理员发送消息
        //将消息内容插入到学生的消息表中
        //根据活动id查询出学生，给学生发送一条消息通知
        Boolean result = studentMessageService.backMessage(adminMessageId, messageContent);

        return result;
    }

    @PostMapping("/deleteMessageByActiId")
    @ResponseBody
    public  Boolean deleteMessageByActiId(@PathParam("actiId") String actiId,HttpServletRequest request){
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        int i = adminMessageService.deleteMessageByActiId(actiId, admin.getAdminId());
        if(i!=0){
            return true;
        }
        return false;
    }

    @GetMapping("/selectCheckAgainActivityByAdminId")
    public String selectCheckAgainActivityByAdminId(HttpServletRequest request,ModelMap modelMap){
        //从session中取出管理员
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        //根据管理员的id查询出所有的未处理的、为再审核的消息(再审核活动)
        List<CheckAgainActivity> checkAgainActivityList = adminMessageService.selectCheckAgainActivityByAdminId(admin.getAdminId());
        modelMap.put("checkAgainActivityList",checkAgainActivityList);
        log.info("再审核的活动"+checkAgainActivityList+"数量"+checkAgainActivityList.size());
        return "admin/checkagainmessage";
    }



    @PostMapping("/deleteMessageByAdminMessageId")
    @ResponseBody
    public  Boolean deleteMessageByAdminMessageId(@PathParam("adminMessageId") String adminMessageId){
        int i = adminMessageService.deleteMessageByAdminMessageId(adminMessageId);
        if(i!=0){
            return true;
        }
        return false;
    }


    @PostMapping("/adminCheckAgainActivityPass")
    @ResponseBody
    public Boolean adminCheckAgainActivityPass(@PathParam("adminMessageId") String adminMessageId,HttpServletRequest request){
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        Boolean result = studentMessageService.adminCheckAgainActivityPass(adminMessageId);
        //根据管理员消息id连接message表查询出活动id和活动的原始状态，将这个活动的状态改为原始状态
        //将当前管理员的消息状态置为已读，更新其他管理员这条消息的内容
        //给对应的学生插入一条消息
        //判断这个学生是否在线，如果在线则执行这个学生的线程返回一个消息


        log.info("最终结果"+result);
        return result;
    }

    @PostMapping("/backMessageByadminMessageId")
    @ResponseBody
    public Boolean backMessageByadminMessageId(@PathParam("adminMessageId")String adminMessageId,@PathParam("messageContent")String messageContent){
        //将所有管理员下标记这条消息为已读
        //给在线的管理员发送消息
        //将消息内容插入到学生的消息表中
        //根据adminMessageId查询出message，给学生发送一条消息通知
        Boolean result = studentMessageService.backMessageByadminMessageId(adminMessageId,messageContent);
        return result;
    }


    @GetMapping("/selectUndealMessageByAdminId")
    public String selectUndealMessageByAdminId(HttpServletRequest request,ModelMap modelMap){
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        List<CheckAgainActivity> checkAgainActivityList = adminMessageService.selectUndealMessageByAdminId(admin.getAdminId());
        modelMap.put("checkAgainActivityList",checkAgainActivityList);
        log.info("再审核的活动"+checkAgainActivityList+"数量"+checkAgainActivityList.size());
        return "admin/undealmessage";
    }


    @GetMapping("/selectFinishedMessageByAdminId")
    public String selectUnreadMessageByAdminId(HttpServletRequest request,ModelMap modelMap){
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        List<CheckAgainActivity> checkAgainActivityList = adminMessageService.selectFinishedMessageByAdminId(admin.getAdminId());
        modelMap.put("checkAgainActivityList",checkAgainActivityList);
        log.info("再审核的活动"+checkAgainActivityList+"数量"+checkAgainActivityList.size());
        return "admin/finishedmessage";
    }

    @PostMapping("/setMessageHasReaded")
    @ResponseBody
    public  Boolean setMessageHasReaded(@PathParam("adminMessageId") String adminMessageId){
        int i = adminMessageService.setMessageHasReaded(adminMessageId);
        if(i!=0){
            return true;
        }
        return false;
    }

    @GetMapping("/selectAllRemindMessage")
    public String selectAllRemindMessage(ModelMap modelMap,HttpServletRequest request){
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        PageInfo<RemindMessage> pageInfo = remindService.selectAllRemindMessageByAdminId(admin.getAdminId());
        modelMap.put("pageInfo",pageInfo);
        log.info("分页信息"+pageInfo);
        return "admin/remindmessage";
    }


    @PostMapping("/deleteRemindByRemindId")
    @ResponseBody
    public Boolean deleteRemindByRemindId(@PathParam("remindId") String remindId){
            return remindService.deleteRemindByRemindId(remindId);
    }

    @PostMapping("/checkPassByActiId")
    @ResponseBody
    public  Boolean checkPassByActiId(@PathParam("actiId") String actiId,HttpServletRequest request){
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        return studentMessageService.checkPassByActiIdAndAdminId(actiId,admin.getAdminId());

    }

    @PostMapping("/backMessageByActidId")
    @ResponseBody
    public Boolean backMessageByActidId(@PathParam("actiId") String actiId,@PathParam("messageComment") String messageComment,HttpServletRequest request){
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        return studentMessageService.backMessageByActidId(actiId,messageComment,admin.getAdminId());

    }
}
