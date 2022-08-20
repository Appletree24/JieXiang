package com.sky31;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/8
 * @TIME 23:27
 */
public class CommunityServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Sky31WelcomeApplication.class);
    }
}
