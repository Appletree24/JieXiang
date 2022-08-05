package com.sky31.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/3
 * @TIME 11:21
 */
@Service
public interface LikeService {

    void like(int userId,int entityType,int entityId,int entityUserId);

    long findEntityLikeCount(int entityType,int entityId);

    int findEntityLikeStatus(int userId,int entityId,int entityType);

    int findUserLikeCount(int userId);
}
