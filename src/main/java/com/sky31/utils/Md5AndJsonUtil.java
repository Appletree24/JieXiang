package com.sky31.utils;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

/**
 * @AUTHOR Zzh
 * @DATE 2022/7/29
 * @TIME 15:34
 */
public class Md5AndJsonUtil {
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String md5(String originPassWord) {
        if (StringUtils.isBlank(originPassWord)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(originPassWord.getBytes(StandardCharsets.UTF_8));
    }

    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                jsonObject.put(key, map.get(key));
            }
        }
        return jsonObject.toJSONString();
    }


    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

//    public static void main(String[] args){
//        Map<String,Object> map=new HashMap<>();
//        map.put("name","zzh");
//        map.put("age",24);
//        System.out.println(getJSONString(0, "ok", map));
//    }

}
