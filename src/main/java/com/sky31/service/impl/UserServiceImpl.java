package com.sky31.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky31.domain.User;
import com.sky31.mapper.UserMapper;
import com.sky31.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @AUTHOR Zzh
 * @DATE 2022/7/29
 * @TIME 15:48
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(User user) {
        User login = userMapper.login(user);
        return login;
    }
}
