package com.sky31.config;

import com.sky31.interceptor.DataInterceptor;
import com.sky31.interceptor.LoginInterceptor;
import com.sky31.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 11:16
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Autowired
    private DataInterceptor dataInterceptor;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/api/login", "/api/user/register", "/api/kaptcha","/api/index"
                ,"/api/user/tokenExpire","/api/adminLogin","/api/discuss/top","/api/user/ban","/api/discuss/delete","/api/discuss/info","/api/*Count","/api/*/increase","/api/countList"
                ,"/api/discuss/*Count","/api/comment/*Count","/api/user/regCount");
        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/api/login", "/api/user/register", "/api/kaptcha","/api/index"
                ,"/api/user/tokenExpire","/api/adminLogin","/api/discuss/top","/api/user/ban","/api/discuss/delete","/api/discuss/info","/api/*Count","/api/*/increase","/api/countList"
                ,"/api/discuss/*Count","/api/comment/*Count","/api/user/regCount");
        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
    }
}
