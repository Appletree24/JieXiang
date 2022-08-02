package com.sky31.utils;

import com.sky31.domain.User;
import org.springframework.stereotype.Component;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 16:30
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users=new ThreadLocal<>();


    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
