package com.bishe.component;

import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ErrorPageRegistry;
import org.springframework.http.HttpStatus;

/**
 * Created by ZYL on 2019/1/27.
 */
//自定义错误页面
public class MyErrorPageRegistrar implements ErrorPageRegistrar {
    @Override
    public void registerErrorPages(ErrorPageRegistry errorPageRegistry) {
        ErrorPage page404 = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
        ErrorPage page500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");

        errorPageRegistry.addErrorPages(page404, page500);

    }
}
