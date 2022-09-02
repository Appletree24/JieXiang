package com.sky31.controller;

import com.alibaba.fastjson2.JSON;
import com.sky31.domain.ResponseResult;
import com.sky31.domain.User;
import com.sky31.service.UserService;
import com.sky31.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @AUTHOR Zzh
 * @DATE 2022/7/29
 * @TIME 16:49
 */
@RestController
@RequestMapping("/api/user")
@ResponseBody
@Getter
public class UserController {

    @Autowired
    UserService userService;

    private static Integer sum = 0;

    static final String REGEX_USERNAME = "^[a-zA-Z0-9]{6,20}$";

    static final String REGEX_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$";

    static final String REGEX_CHINESE = "^[\\u4e00-\\u9fa5]{0,20}$";

    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }


    public Integer getSum(){
        return sum;
    }

    @GetMapping("/tokenExpire")
    public Object tokenExpire(String token) {
        try {
            Claims claims = JwtUtil.parseJWT(token);
            User user = userService.findUserByToken(token);
            return JSON.toJSON(user);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult<>(1, "token失效");
        }
    }

    @PostMapping("/ban")
    public Object ban(String username) {
        if (StringUtils.isBlank(username)) {
            try {
                userService.banUser(username);
            } catch (Exception e) {
                e.printStackTrace();
                return JSON.toJSON("该用户不存在");
            }
            return JSON.toJSON("拉黑成功");
        } else {
            return JSON.toJSON("该用户不存在");
        }
    }

    @GetMapping("/regCount")
    public Object regCount(){
        return JSON.toJSON(sum);
    }

    @GetMapping("/increase")
    public void increase(){
        sum++;
    }


    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user) throws IllegalAccessException {
        sum += 1;
        if (isUsername(user.getUsername()) && isPassword(user.getPassword()) && !isChinese(user.getUsername())) {
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
        } else {
            return new ResponseResult(300, "用户名或密码不符合要求");
        }
    }

    @GetMapping("/getAll")
    public List<User> getAll() {
        return userService.getAll();
    }
}
