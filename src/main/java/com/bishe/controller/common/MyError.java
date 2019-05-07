package com.bishe.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ZYL on 2019/1/27.
 */
//@Controller
public class MyError {
    @RequestMapping("/404")
    public String error404() {
        return "front/errors/404";
    }

    /**
     * 500 error
     * @return
     */
    @RequestMapping("/500")
    public String error500() {
        return "front/errors/500";
    }
}
