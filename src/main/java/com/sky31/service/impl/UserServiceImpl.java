package com.sky31.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sky31.domain.User;
import com.sky31.mapper.UserMapper;
import com.sky31.service.UserService;
import com.sky31.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @AUTHOR Zzh
 * @DATE 2022/7/29
 * @TIME 15:48
 */
@Service
public class UserServiceImpl implements UserService, Constant {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private RedisTemplate redisTemplate;


    public User login(User user) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        QueryWrapper<User> wrapper1 = userQueryWrapper.eq("username", user.getUsername());
        User user1 = userMapper.selectOne(wrapper1);
        String truePassWord = SaltUtil.backToDb(user.getPassword(), user1.getSalt());
        QueryWrapper<User> userQueryWrapper1 = new QueryWrapper<>();
        QueryWrapper<User> wrapper = userQueryWrapper1.eq("password", truePassWord)
                .eq("username", user.getUsername());
        return userMapper.selectOne(wrapper);
    }


    public Map<String, Object> register(User user) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        if (user == null) {
            throw new IllegalAccessException("参数为空");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        String filter = sensitiveFilter.filter(user.getUsername());
        if (filter.contains("*")) {
            map.put("usernameMsg", "账号含有违禁词");
            return map;
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        QueryWrapper<User> wrapper = userQueryWrapper.eq("username", user.getUsername());
        if (userMapper.selectOne(wrapper) != null) {
            map.put("usernameMsg", "该用户已存在");
            return map;
        }
        String salt = Md5AndJsonUtil.generateUUID().substring(0, 5);
        user.setSalt(salt);
        String newPassword = SaltUtil.backToDb(user.getPassword(), salt);
        user.setPassword(newPassword);
        user.setType(0);
        user.setCreateTime(new Date());
        userMapper.insert(user);
        return map;
    }


    public List<User> getAll() {
        return userMapper.selectList(null);
    }

    @Override
    public User findUserById(int id) {
//        return userMapper.selectById(id);
        User user = getCache(id);
        if (user == null) {
            user = initCache(id);
        }
        return user;
    }

    @Override
    public User findUserByIdString(String id) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id",id);
        return userMapper.selectOne(userQueryWrapper);
    }

    @Override
    public User findUserExceptToken(int id) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.select("username","password","salt","type","create_time")
                .eq("id",id);
        return userMapper.selectOne(userQueryWrapper);
    }

    @Override
    public int saveToken(String username, String token) {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        UpdateWrapper<User> updateWrapper = userUpdateWrapper.eq("username", username)
                .set("token", token);
        return userMapper.update(null, updateWrapper);
    }

    @Override
    public User findUserByToken(String token) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        QueryWrapper<User> wrapper = userQueryWrapper.eq("token", token);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public User initCache(int userId) {
        User userExceptToken = this.findUserExceptToken(userId);
//        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, userExceptToken, 3600, TimeUnit.SECONDS);
        return userExceptToken;
    }

    @Override
    public void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

    @Override
    public User findUserByName(String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username",username);
        return userMapper.selectOne(userQueryWrapper);
    }

    @Override
    public int banUser(String username) {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("username",username)
                .set("type",2);
        return userMapper.update(null,userUpdateWrapper);
    }


}
