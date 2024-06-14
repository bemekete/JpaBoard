package com.example.jpaboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 설정 클래스
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    // 13번줄 경로를 12번줄 경로로 접근을 하겠다
    private String resourcePath = "/upload/**"; // view 에서 접근 할 경로
    private String savePath = "file:///C:/ProjectHwang/uploadFiles/"; // 실제 파일 저장 경로

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath).addResourceLocations(savePath);
    }
}
