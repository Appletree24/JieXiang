package com.sky31.interceptor;

import com.sky31.domain.User;
import com.sky31.service.DataService;
import com.sky31.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/6
 * @TIME 16:05
 */
@Component
public class DataInterceptor implements HandlerInterceptor {

    public DataInterceptor() {
    }

    @Autowired
    private DataService dataService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //统计UV
        String ip = request.getRemoteHost();
        dataService.recordUV(ip);

        //统计DAU
        User user = hostHolder.getUser();
        if (user!=null){
            dataService.recordDAU(user.getId());
        }


        return true;
    }
}
