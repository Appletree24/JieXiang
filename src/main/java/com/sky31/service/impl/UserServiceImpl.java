package com.sky31.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sky31.domain.User;
import com.sky31.mapper.UserMapper;
import com.sky31.service.UserService;
import com.sky31.utils.SaltUtil;
import com.sky31.utils.md5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        QueryWrapper<User> wrapper1 = userQueryWrapper.eq("username", user.getUsername());
        User user1 = userMapper.selectOne(wrapper1);
        String truePassWord=SaltUtil.backToDb(user.getPassword(),user1.getSalt());
        QueryWrapper<User> userQueryWrapper1 = new QueryWrapper<>();
        QueryWrapper<User> wrapper = userQueryWrapper1.eq("password", truePassWord)
                .eq("username", user.getUsername());
        return userMapper.selectOne(wrapper);
    }

    @Override
    public Map<String,Object> register(User user) throws IllegalAccessException {
        Map<String,Object> map=new HashMap<>();
        if (user==null){
            throw new IllegalAccessException("参数为空");
        }
        if (StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        QueryWrapper<User> wrapper = userQueryWrapper.eq("username", user.getUsername());
        if (userMapper.selectOne(wrapper)!=null){
            map.put("usernameMsg","该用户已存在");
            return map;
        }
        String salt=md5Util.generateUUID().substring(0,5);
        user.setSalt(salt);
        String newPassword = SaltUtil.backToDb(user.getPassword(), salt);
        user.setPassword(newPassword);
        user.setIsAdmin(0);
        userMapper.insert(user);
        return map;
    }
}
