package com.sky31.controller;


import com.alibaba.fastjson2.JSON;
import com.google.code.kaptcha.Producer;
import com.sky31.domain.ResponseResult;
import com.sky31.domain.User;
import com.sky31.service.UserService;
import com.sky31.utils.JwtUtil;
import com.sky31.utils.Md5AndJsonUtil;
import com.sky31.utils.RedisKeyUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 0:04
 */
@RestController
@ResponseBody
@RequestMapping("/api")
@Getter
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    private static Integer sum = 0;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private Producer kaptchaProducer;


    public Integer getSum(){
        return sum;
    }

    @PostMapping("/adminLogin")
    public ResponseResult loginAdmin(@RequestBody User user) {
        if (!user.getUsername().equals("superAdmin") && !user.getPassword().equals("sky31666")) {
            return new ResponseResult(1, "账号密码错误");
        } else {
            return new ResponseResult(0, "登陆成功");
        }
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user, @CookieValue("kaptchaOwner") String kaptchaOwner, String code) throws IOException {
//        if (user.getType()==2){
//            return new ResponseResult(300,"该用户已被拉黑");
//        }
        sum += 1;
        User tempUser = userService.findUserByName(user.getUsername());
        if (tempUser.getType() == 2) {
            return new ResponseResult(300, "该用户已被拉黑");
        }
        String kaptcha = null;
        if (StringUtils.isNotBlank(kaptchaOwner)) {
            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha = (String) redisTemplate.opsForValue().get(redisKey);
        }
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            return new ResponseResult(300, "验证码不正确");
        }
        Map<String, Object> map = new HashMap<>();
        User loginUser = userService.login(user);
        if (loginUser != null) {
            String token = JwtUtil.createJWT(UUID.randomUUID().toString(), String.valueOf(loginUser.getId()), null);
            userService.saveToken(user.getUsername(), token);
            map.put("token", token);
        } else {
            return new ResponseResult(300, "用户名或密码错误，请重新登录");
        }
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), userService.getAuthorities(user.getId()));
//        SecurityContextHolder.setContext(new SecurityContextImpl(authenticationToken));
        return new ResponseResult(200, "登陆成功", map);
    }

    @GetMapping("/loginCount")
    public Object loginCount(){
        return JSON.toJSON(sum);
    }

    @GetMapping("/login/increase")
    public void increase(){
        sum++;
    }


    @RequestMapping(value = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response /*HttpSession session*/, HttpServletRequest request) {
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
//        session.setAttribute("kaptcha", text);
        //验证码归属
        String kaptchaOwner = Md5AndJsonUtil.generateUUID();
        Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath(request.getContextPath());
        response.addCookie(cookie);
        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        redisTemplate.opsForValue().set(redisKey, text, 60, TimeUnit.SECONDS);
        response.setContentType("image/png");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("响应验证码失败 " + e.getMessage());
        }
    }
}
