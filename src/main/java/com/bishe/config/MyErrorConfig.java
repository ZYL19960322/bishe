package com.bishe.config;

import com.bishe.component.MyErrorPageRegistrar;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ZYL on 2019/1/27.
 */
//使自定义错误生效
@Configuration
public class MyErrorConfig {
    @Bean
    public ErrorPageRegistrar errorPageRegistrar(){
        return new MyErrorPageRegistrar();
    }
}
