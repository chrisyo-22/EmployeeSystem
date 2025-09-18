package com.chrisyo;

import com.chrisyo.interceptor.InterceptorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //Declaration that this is a config, basically @Component
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private InterceptorHandler interceptorHandler;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptorHandler).addPathPatterns("/**").excludePathPatterns("/login");
    }
}
