package com.bishe;

import com.bishe.controller.WebSocket.AdminWebSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootApplication
public class BisheApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(BisheApplication.class, args);
		AdminWebSocket.setApplicationContext(applicationContext);
	}



	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setForceEncoding(true);
		characterEncodingFilter.setEncoding("UTF-8");
		registrationBean.setFilter(characterEncodingFilter);
		return registrationBean;
	}
}
