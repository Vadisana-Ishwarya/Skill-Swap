package com.skillswap.config;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
@Configuration @EnableWebMvc @ComponentScan("com.skillswap.controller")
public class WebConfig {
 @Bean public InternalResourceViewResolver viewResolver(){
  InternalResourceViewResolver r=new InternalResourceViewResolver();
  r.setPrefix("/WEB-INF/views/"); r.setSuffix(".jsp"); return r;
 }
}