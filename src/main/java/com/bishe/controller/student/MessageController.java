package com.bishe.controller.student;

import com.bishe.dto.StudentMessageResult;
import com.bishe.pojo.Activity;
import com.bishe.pojo.Student;
import com.bishe.service.StudentMessageService;
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
 * Created by ZYL on 2019/2/21.
 */
@Controller
public class MessageController {
    Logger log=LoggerFactory.getLogger(MessageController.class);
    @Autowired
    StudentMessageService studentMessageService;

    @GetMapping("/toMessagePage")
    public String toMessagePage(HttpServletRequest request, ModelMap modelMap){
        //根据学生id查询出学生的所有消息
        Student student=(Student) request.getSession().getAttribute("student");
        List<StudentMessageResult> studentMessageResultList = studentMessageService.selectAllMessageByStudId(student.getStudId());
        log.info("消息的数量"+studentMessageResultList.size() +"===消息详情"+studentMessageResultList);
        modelMap.put("studentMessageResultList",studentMessageResultList);
        //将所有未读的消息状态置为已读
       studentMessageService.changeMessageStatusByStudId(student.getStudId());
        return "front/message";
    }

    @PostMapping("/deleteMessageByMessageId")
    @ResponseBody
    public Boolean deleteMessageByMessageId(@PathParam("studMessageId") String studMessageId){
        //根据学生的消息id删除学生
        int i = studentMessageService.deleteMessageByMessageId(studMessageId);
        if(i!=0){
            return true;
        }
        return false;
    }
    @GetMapping("/selectActivityByStudMessageId")
    public  String selectActivityByStudMessageId(@PathParam("studMessageId") String studMessageId,ModelMap modelMap){
        Activity activity = studentMessageService.selectActivityByStudMessageId(studMessageId);
        modelMap.put("activity",activity);
        log.info("活动"+activity);

        return "front/messagedetail";
    }


}
