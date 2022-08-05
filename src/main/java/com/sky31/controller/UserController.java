package com.sky31.controller;

import com.sky31.domain.ResponseResult;
import com.sky31.domain.User;
import com.sky31.mapper.UserMapper;
import com.sky31.service.UserService;
import com.sky31.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
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

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user) throws IllegalAccessException {
        Map<String, Object> registerUser = userService.register(user);
        if (registerUser.get("usernameMsg") == "账号不能为空") {
            return new ResponseResult(300, "注册失败", registerUser);
        }
        if (registerUser.get("usernameMsg") == "该用户已存在") {
            return new ResponseResult(300, "注册失败", registerUser);
        }
        if (registerUser.get("passwordMsg") == "密码不能为空") {
            return new ResponseResult(300, "注册失败", registerUser);
        }
        if (registerUser.get("usernameMsg") == "账号含有违禁词") {
            return new ResponseResult(300, "注册失败", registerUser);
        }
        return new ResponseResult(200, "注册成功", registerUser);
    }

    @GetMapping("/getAll")
    public List<User> getAll() {
        return userService.getAll();
    }
}
