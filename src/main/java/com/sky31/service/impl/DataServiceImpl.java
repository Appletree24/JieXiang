package com.sky31.service.impl;

import com.sky31.service.DataService;
import com.sky31.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/6
 * @TIME 15:48
 */
@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private RedisTemplate redisTemplate;


    private SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");


    @Override
    public void recordUV(String ip) {
        String redisKey= RedisKeyUtil.getUVKey(df.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(redisKey,ip);
    }

    public long calculateUV(Date start,Date end){
        if (start==null||end==null){
            throw new IllegalArgumentException("参数不能为空!");
        }
        //整理日期范围中的key
        List<String> keyList=new ArrayList<>();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)){
            String key = RedisKeyUtil.getUVKey(df.format(calendar.getTime()));
            keyList.add(key);
            calendar.add(Calendar.DATE,1);
        }
        String redisKey=RedisKeyUtil.getUVKey(df.format(start),df.format(end));
        redisTemplate.opsForHyperLogLog().union(redisKey,keyList.toArray());
        return redisTemplate.opsForHyperLogLog().size(redisKey);
    }


    public void recordDAU(int userId){
        String redisKey=RedisKeyUtil.getDAUKey(df.format(new Date()));
        redisTemplate.opsForValue().setBit(redisKey,userId,true);
    }

    public long calculateDAU(Date start,Date end){
        if (start==null||end==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        List<byte[]> keyList=new ArrayList<>();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)){
            String key = RedisKeyUtil.getDAUKey(df.format(start), df.format(end));
            keyList.add(key.getBytes());
            calendar.add(Calendar.DATE,1);
        }
        return (long) redisTemplate.execute((RedisCallback) connection -> {
            String redisKey=RedisKeyUtil.getDAUKey(df.format(start),df.format(end));
            connection.bitOp(RedisStringCommands.BitOperation.OR,redisKey.getBytes(),keyList.toArray(new byte[0][0]));
            return connection.bitCount(redisKey.getBytes());
        });
    }
}
