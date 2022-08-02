package com.sky31.interceptor;

import com.sky31.domain.User;
import com.sky31.service.UserService;
import com.sky31.utils.HostHolder;
import com.sky31.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 10:19
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        try {
            Claims claims = JwtUtil.parseJWT(token);
            User user = userService.findUserByToken(token);
            hostHolder.setUser(user);
            String subject = claims.getSubject();
            System.out.println(subject);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("请登录后重试");
        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
