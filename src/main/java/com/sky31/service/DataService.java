package com.sky31.service;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/6
 * @TIME 15:48
 */
@Service
public interface DataService {

    void recordUV(String ip);

    long calculateUV(Date start, Date end);

    void recordDAU(int userId);

    long calculateDAU(Date start,Date end);
}
