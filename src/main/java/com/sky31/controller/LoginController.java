package com.sky31.controller;


import com.google.code.kaptcha.Producer;
import com.sky31.domain.ResponseResult;
import com.sky31.domain.User;
import com.sky31.service.UserService;
import com.sky31.utils.JwtUtil;
import com.sky31.utils.RedisKeyUtil;
import com.sky31.utils.md5Util;
import io.netty.util.internal.StringUtil;
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
import javax.servlet.http.HttpSession;
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
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private Producer kaptchaProducer;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user,@CookieValue("kaptchaOwner") String kaptchaOwner,String code) throws IOException {
        String kaptcha=null;
        if (StringUtils.isNotBlank(kaptchaOwner)){
            String redisKey=RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha= (String) redisTemplate.opsForValue().get(redisKey);
        }
        if (StringUtils.isBlank(kaptcha)||StringUtils.isBlank(code)||!kaptcha.equalsIgnoreCase(code)){
            return new ResponseResult(300,"验证码不正确");
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
        return new ResponseResult(200, "登陆成功", map);
    }

    @RequestMapping(value = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response /*HttpSession session*/, HttpServletRequest request) {
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
//        session.setAttribute("kaptcha", text);
        //验证码归属
        String kaptchaOwner = md5Util.generateUUID();
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
