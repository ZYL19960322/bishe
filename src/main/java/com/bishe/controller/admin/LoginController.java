package com.bishe.controller.admin;

import com.bishe.dto.StudentInfoResponse;
import com.bishe.enums.StudentInfoEnum;
import com.bishe.pojo.Admin;
import com.bishe.service.AdminService;
import com.bishe.utils.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Created by ZYL on 2019/3/15.
 */
@Controller
public class LoginController {
   Logger log= LoggerFactory.getLogger(LoginController.class);
    @Autowired
    AdminService adminService;

    @GetMapping("/admin/come")
    public String isAdminComing(){
        return "admin/login";
    }


    @PostMapping("/admin/register")
    @ResponseBody
    public StudentInfoResponse toRegister(@PathParam("adminName") String adminName,@PathParam("adminPassword") String adminPassword){
        log.info("接收的参数==名字"+adminName+"密码"+adminPassword);
        //管理员是否已被注册
        List<Admin> adminList = adminService.selectAllAdmin();
        if(adminList!=null){
            for(Admin admin:adminList){
                if (admin.getAdminName().equals(adminName) && admin.getAdminPassword().equals(adminPassword)){
                    return new StudentInfoResponse(StudentInfoEnum.HasRegistered.getStatusId(),StudentInfoEnum.HasRegistered.getStatusDesc());
                }
            }
        }
        //没有被注册则注册管理员
         adminService.addAdmin(UUIDUtils.getId(),adminName, adminPassword);
            return new StudentInfoResponse(StudentInfoEnum.RegisterSuccess.getStatusId(),StudentInfoEnum.RegisterSuccess.getStatusDesc());
    }

    @PostMapping("/admin/login")
    @ResponseBody
    public StudentInfoResponse toLogin(@PathParam("adminName") String adminName, @PathParam("adminPassword") String adminPassword, HttpServletRequest request){
        log.info("接收的参数==名字"+adminName+"密码"+adminPassword);
        //判断账号和密码是否为空
        if(adminName!="" && adminPassword!=""){
           Admin admin = adminService.selectAdminByNameAndPassword(adminName, adminPassword);
           if(admin==null){
               //账号或密码不正确
               return new StudentInfoResponse(StudentInfoEnum.AccountNameOrPasswordError.getStatusId(),StudentInfoEnum.AccountNameOrPasswordError.getStatusDesc());
           }
           //用户存在，则登录成功
           if(admin!=null){
               //将用户存入session中
               request.getSession().setAttribute("admin",admin);
               request.getSession().setMaxInactiveInterval(1000000);
               log.info("controller中的用户"+request.getSession().getAttribute("admin"));
               return new StudentInfoResponse(StudentInfoEnum.LoginSuccess.getStatusId(),StudentInfoEnum.LoginSuccess.getStatusDesc());
           }
        }
        //密码或账号为空
        return new StudentInfoResponse(StudentInfoEnum.NameOrPasswordEmpty.getStatusId(),StudentInfoEnum.NameOrPasswordEmpty.getStatusDesc());
    }

    @GetMapping("/toAdminIndexPage")
    public String toAdminIndexPage(){
        log.info("去系统首页");
        return "admin/index";
    }

    @GetMapping("/adminLoginOut")
    public String adminLoginOut(HttpServletRequest request){
        //情况session数据
        request.getSession().invalidate();
        //回到登录页面
        return "admin/loginandregister";
    }





}
