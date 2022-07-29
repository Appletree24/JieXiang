package com.sky31.controller;

import com.sky31.domain.ResponseResult;
import com.sky31.domain.User;
import com.sky31.mapper.UserMapper;
import com.sky31.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @AUTHOR Zzh
 * @DATE 2022/7/29
 * @TIME 16:49
 */
@RestController
@RequestMapping("/user")
@ResponseBody
public class UserController {
    @Autowired
    UserMapper userMapper;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        Map<String,Object> map=new HashMap<>();
        User loginUser = userMapper.login(user);
        if (loginUser!=null){
            String token = JwtUtil.createJWT(UUID.randomUUID().toString(), String.valueOf(loginUser.getId()), null);
            map.put("token",token);
        }else{
            return new ResponseResult(300,"用户名或密码错误，请重新登录");
        }
        return new ResponseResult(200,"登陆成功",map);
    }
}
