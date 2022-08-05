package com.sky31.utils;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/3
 * @TIME 11:12
 */
public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    private static final String PREFIX_USER_LIKE = "like:user";

    private static final String PREFIX_TOKEN = "token:entity";

    private static final String PREFIX_KAPTCHA = "kaptcha";

    private static final String PREFIX_USER = "user";

    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }


    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    public static String getTokenKey(String token) {
        return PREFIX_TOKEN + SPLIT + token;
    }

    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    public static String getUserLikeKey(int userId){
        return PREFIX_USER_LIKE+SPLIT+userId;
    }
}
