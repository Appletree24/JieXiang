package com.sky31.controller;

import com.alibaba.fastjson2.JSON;
import com.sky31.domain.DiscussPost;
import com.sky31.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 14:34
 */
@RestController
@ResponseBody
public class IndexController {
    @Autowired
    DiscussPostService discussPostService;

    @GetMapping("/index")
    public String getIndexDiscuss(int userId,int offset,int limit){
        List<DiscussPost> discussPosts = discussPostService.selectDiscussPosts(userId, offset, limit);
        return JSON.toJSON(discussPosts).toString();
    }
}
