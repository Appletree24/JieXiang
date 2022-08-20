package com.sky31.controller;

import com.alibaba.fastjson2.JSON;
import com.sky31.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/6
 * @TIME 16:09
 */
@RestController
@ResponseBody
@RequestMapping("/api")
public class DataController {

    @Autowired
    private DataService dataService;


    @RequestMapping(value = "/data/uv",method = RequestMethod.POST)
    public Object getUV(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                        @DateTimeFormat(pattern = "yyyy-MM-dd")Date end){
        long uv = dataService.calculateUV(start, end);
        Map<String,Object> map=new HashMap<>();
        map.put("uv",uv);
        map.put("uvStartDate",start);
        map.put("uvEndDate",end);
        return JSON.toJSON(map);
    }

    @RequestMapping(value = "/data/dau",method = RequestMethod.POST)
    public Object getDAU(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                         @DateTimeFormat(pattern = "yyyy-MM-dd")Date end){
        long dau = dataService.calculateDAU(start, end);
        Map<String,Object> map=new HashMap<>();
        map.put("dau",dau);
        map.put("dauStartDate",start);
        map.put("dauEndDate",end);
        return JSON.toJSON(map);
    }


}
