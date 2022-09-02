package com.sky31.controller;

import com.alibaba.fastjson2.JSON;
import com.sky31.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

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

    CommentController commentController;

    SearchController searchController;

    DiscussPostsController discussPostsController;

    LoginController loginController;

    UserController userController;

    @GetMapping("/countList")
    public Object countList(){
        Integer commentSum = commentController.getSum();
        Integer searchControllerSum = searchController.getSum();
        Integer postsControllerSum = discussPostsController.getSum();
        Integer loginControllerSum = loginController.getSum();
        Integer regSum = userController.getSum();
        Map<String,Integer> sums=new HashMap<>();
        sums.put("回答次数",commentSum);
        sums.put("搜索次数",searchControllerSum);
        sums.put("提问次数",postsControllerSum);
        sums.put("登录次数",loginControllerSum);
        sums.put("注册次数",regSum);
        return JSON.toJSON(sums);
    }


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
