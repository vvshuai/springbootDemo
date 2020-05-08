package com.miaoshaproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 23:41 2020/5/7
 * @Modified By:
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController( "/" ).setViewName( "forward:/login.html" );
        registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
        WebMvcConfigurer.super.addViewControllers( registry );
    }
}
