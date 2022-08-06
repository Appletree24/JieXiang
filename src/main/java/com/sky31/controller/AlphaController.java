package com.sky31.controller;

import com.sky31.utils.Md5AndJsonUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 12:41
 */
@RestController
@ResponseBody
public class AlphaController {

    @PostMapping("/ajax")
    @ResponseBody
    public String testAjax(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return Md5AndJsonUtil.getJSONString(0, "操作成功!");
    }
}
