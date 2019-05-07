package com.bishe.controller.WebSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by ZYL on 2019/3/18.
 */
@WebListener
@Component
public class RequestLister implements ServletRequestListener {
    Logger log = LoggerFactory.getLogger(RequestLister.class);

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        //将所有request请求都携带上httpSession
        HttpSession session = ((HttpServletRequest) sre.getServletRequest()).getSession();
        if(session==null){
            log.info("session为空");
        }
    }
    public RequestLister(){}
}
