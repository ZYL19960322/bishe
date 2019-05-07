package com.bishe.controller.student;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Created by ZYL on 2018/12/13.
 */
@Controller
public class CommonController {

    @GetMapping("/student/register" )
    public String studentRegister(){
        return "front/register";
    }

    @GetMapping("/student/login" )
    public String studentLogin(){
        return "front/login";
    }




    @GetMapping("/student/come")
    public String isStudentComing(){
        return "front/login";
    }


}
