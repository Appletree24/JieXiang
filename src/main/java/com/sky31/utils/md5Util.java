package com.sky31.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @AUTHOR Zzh
 * @DATE 2022/7/29
 * @TIME 15:34
 */
public class md5Util {
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static String md5(String originPassWord){
        if (StringUtils.isBlank(originPassWord)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(originPassWord.getBytes(StandardCharsets.UTF_8));
    }


}
