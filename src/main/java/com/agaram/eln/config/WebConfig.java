package com.agaram.eln.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/**").allowedMethods("GET", "POST");
//    	registry.addMapping("/api/**")
//        .allowedOrigins("http://domain2.com")
//        .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS")
//        .allowedHeaders("X-TenantID", "header2", "header3")
//        .exposedHeaders("X-TenantID", "header2")
//        .allowCredentials(false).maxAge(3600);
    }
    
    
}
