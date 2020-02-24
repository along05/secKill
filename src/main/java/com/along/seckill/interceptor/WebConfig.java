package com.along.seckill.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 设置拦截器起作用的url
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AntiRefreshInterceptor antiRefreshInterceptor ;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(antiRefreshInterceptor).addPathPatterns(new String[]{"/lb"}) ;
    }
}
