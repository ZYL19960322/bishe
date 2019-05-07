package com.bishe.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ZYL on 2018/12/14.
 */
public class CheckCodeUtil {
    public static boolean checkVerifyCode(HttpServletRequest request,String checkCode){
        String verifyCodeExpected = (String)request.getSession().getAttribute(
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        //这里相当于 request.getParameter("verifyCodeActual");
       // String verifyCodeActual = HttpServletRequestUtil.getString(request, "checkCode");
        String verifyCodeActual = checkCode ;
        if(verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)){
            return false;
        }
        return true;
    }
}
