package com.bishe.interceptor;



import com.bishe.pojo.Admin;
import com.bishe.pojo.Student;
import com.bishe.utils.MobileUtil;
import com.sun.istack.internal.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ZYL on 2018/12/12.
 */
public class UserLoginInterceptor implements HandlerInterceptor{
    Logger log = LoggerFactory.getLogger(UserLoginInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean moblieFlag = MobileUtil.JudgeIsMoblie(request);
        if (moblieFlag==true){
            //手机访问
            Student student = (Student) request.getSession().getAttribute("student");
            if (student==null){
                response.sendRedirect("student/come");
                return false ;
            }
            if (student!=null){
                return true;
            }

        }
        if (moblieFlag==false){
            //电脑访问
            Admin admin = (Admin) request.getSession().getAttribute("admin");
            if (admin==null){
                response.sendRedirect("admin/come");
                return false ;
            }
            if(admin!=null){
                return true;
            }


        }
       // log.info("拦截器通行");
         return true ;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
