package com.sky31.service;

import com.sky31.domain.User;
import com.sky31.mapper.UserMapper;
import com.sky31.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @AUTHOR Zzh
 * @DATE 2022/7/29
 * @TIME 15:41
 */
@Service
public interface UserService {
    User login(User user);

    Map<String, Object> register(User user) throws IllegalAccessException;

    List<User> getAll();

    User findUserById(int id);

    int saveToken(String username, String token);

    User findUserByToken(String token);

    User getCache(int userId);

    User initCache(int userId);

    void clearCache(int userId);


}
