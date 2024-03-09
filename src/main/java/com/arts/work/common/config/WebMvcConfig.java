package com.arts.work.common.config;


import com.arts.work.common.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//自定义配置类
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //设置拦截路径，排除路径，优先级等
        registry.addInterceptor(new TokenInterceptor())
                .excludePathPatterns("/user/login").order(11)
                // 拦截url
                .addPathPatterns("/**");
    }
}
