package com.sky31.utils;

/**
 * @AUTHOR Zzh
 * @DATE 2022/7/29
 * @TIME 19:14
 */

import org.apache.commons.codec.digest.DigestUtils;

public class SaltUtil {
    public static String salt = "springboot";//这里要和前端的盐值一样,拿来做第一次加密

    public static String md5(String str){
        return DigestUtils.md5Hex(str);
    }

    //因为前端做了一次加盐加密，后端也做了一次加盐加密，所以在登录的时候需要做两次加盐加密进行匹配
    //两次加密后的密文如果和数据库中的密文一样的话，证明输入的是正确的密码

    //第一次加密，前端传过来的加密
    public static String inputToBack(String str){
        return md5(str+salt);
    }

    //第二次加密。第二次加密会用前端传过来的密文再加密一次
    public static String backToDb(String str,String dbsalt){
        return md5(str + dbsalt);
    }

    //两次加密以后和数据库里面的作对比，如果一样就证明输入的密码正确
    public static String inputToDb(String str,String dbsalt){
        return backToDb(inputToBack(str),dbsalt);
    }
}