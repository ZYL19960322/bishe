package com.bishe.config;


import com.bishe.component.FileUploadProperties;
import com.bishe.interceptor.UserLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//使用WebMvcConfigurerAdapter可以来扩展SpringMVC的功能
//@EnableWebMvc   不要接管SpringMVC
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {







    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
         super.addViewControllers(registry);
        //浏览器发送 /atguigu 请求来到 success
        registry.addViewController("/atguigu").setViewName("success");
    }

    //所有的WebMvcConfigurerAdapter组件都会一起起作用
    @Bean //将组件注册在容器
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(){
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
               // registry.addViewController("/").setViewName("login");
               // registry.addViewController("/index.html").setViewName("login");
                //registry.addViewController("commonLogin").setViewName("index");
            }

            //注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                super.addInterceptors(registry);
                //静态资源；  *.css , *.js
                //SpringBoot已经做好了静态资源映射
                registry.addInterceptor(new UserLoginInterceptor()).addPathPatterns("/**")
                        .excludePathPatterns("/getKaptchaImage","/student/register","/student/login","/student/come","/admin/come","/admin/login","/admin/register");
            }

        };
        return adapter;

    }

    @Autowired
    private FileUploadProperties fileUploadProperteis;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
        * 说明：增加虚拟路径(在此处配置的虚拟路径，用springboot内置的tomcat时有效，用外部的tomcat也有效)
        */
        String osName = System.getProperties().getProperty("os.name");
        if (osName.toLowerCase().startsWith("windows")) {
            //Windows下
            registry.addResourceHandler(fileUploadProperteis.getStaticAccessPath()).addResourceLocations("file:" + fileUploadProperteis.getUploadFolder() + "/");
        } else {
            //Linux或Unix下
            registry.addResourceHandler(fileUploadProperteis.getStaticAccessPath()).addResourceLocations("file:/home/bishe/");
            super.addResourceHandlers(registry);
        }
        }
    }




