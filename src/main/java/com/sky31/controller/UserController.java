package com.sky31.controller;

import com.sky31.domain.ResponseResult;
import com.sky31.domain.User;
import com.sky31.mapper.UserMapper;
import com.sky31.service.UserService;
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
    UserService userService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        Map<String,Object> map=new HashMap<>();
        User loginUser = userService.login(user);
        if (loginUser!=null){
            String token = JwtUtil.createJWT(UUID.randomUUID().toString(), String.valueOf(loginUser.getId()), null);
            map.put("token",token);
        }else{
            return new ResponseResult(300,"用户名或密码错误，请重新登录");
        }
        return new ResponseResult(200,"登陆成功",map);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user) throws IllegalAccessException {
        Map<String, Object> registerUser = userService.register(user);
        if (registerUser.get("usernameMsg")=="账号不能为空"){
            return new ResponseResult(300,"注册失败",registerUser);
        }
        if (registerUser.get("usernameMsg")=="该用户已存在"){
            return new ResponseResult(300,"注册失败",registerUser);
        }
        if (registerUser.get("passwordMsg")=="密码不能为空"){
            return new ResponseResult(300,"注册失败",registerUser);
        }
        return new ResponseResult(200,"注册成功",registerUser);
    }
}
