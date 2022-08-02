package com.sky31.controller;

import com.sky31.domain.DiscussPost;
import com.sky31.domain.User;
import com.sky31.service.DiscussPostService;
import com.sky31.utils.HostHolder;
import com.sky31.utils.md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 15:19
 */
@RestController
@ResponseBody
@RequestMapping("/discuss")
public class DiscussPostsController {
    @Autowired
    DiscussPostService discussPostService;

    @Autowired
    HostHolder hostHolder;

    @PostMapping("/add")
    public String addDiscussPost(String title,String content){
        User user = hostHolder.getUser();
        if (user==null){
            return md5Util.getJSONString(403,"请先登录");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPost.setMilsTime(System.currentTimeMillis());
        discussPostService.addDiscussPost(discussPost);
        return md5Util.getJSONString(0,"发布问题成功");
    }
}
