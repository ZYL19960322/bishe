package com.bishe.config;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * Created by ZYL on 2018/12/10.
 */
@org.springframework.context.annotation.Configuration
public class MybatisConfig{
        @Bean
        public ConfigurationCustomizer configurationCustomizer(){
            return new ConfigurationCustomizer(){

                @Override
                public void customize(Configuration configuration) {
                    configuration.setMapUnderscoreToCamelCase(true);
                }
            };
        }
}
