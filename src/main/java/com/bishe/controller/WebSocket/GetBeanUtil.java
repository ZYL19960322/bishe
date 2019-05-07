package com.bishe.controller.WebSocket;

import com.bishe.service.AdminMessageService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by ZYL on 2019/3/19.
 */
@Component
public class GetBeanUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        GetBeanUtil.applicationContext=applicationContext;

    }
    public  static Object getBean( Class c){
        return applicationContext.getBean(c);
    }
}
