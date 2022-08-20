package com.sky31.controller;

import com.alibaba.fastjson2.JSON;
import com.sky31.domain.DiscussPost;
import com.sky31.domain.Page;
import com.sky31.domain.User;
import com.sky31.service.DiscussPostService;
import com.sky31.service.LikeService;
import com.sky31.service.UserService;
import com.sky31.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 14:34
 */
@RestController
@RequestMapping("/api")
public class IndexController implements Constant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;


//    @GetMapping("/index")
//    public String getIndexDiscuss(int userId,int offset,int limit){
//        List<DiscussPost> discussPosts = discussPostService.selectDiscussPosts(userId, offset, limit);
//        return JSON.toJSON(discussPosts).toString();
//    }
    @GetMapping("/")
    public String root(){
        return "forward:/index";
    }



    @GetMapping("/index")
    public String getIndexPage(Page page,int type){
        page.setRows(discussPostService.selectDiscussPostRows(0));
        page.setPath("/index");
        List<DiscussPost> list = discussPostService.selectDiscussPosts(0, page.getOffset(), page.getLimit(),type);
        List<Map<String,Object>> discussPosts=new ArrayList<>();
        if (list!=null){
            for (DiscussPost post:list){
                Map<String,Object> map=new HashMap<>();
                map.put("post",post);
                User userById = userService.findUserById(post.getUserId());
                map.put("user",userById);
                long likeCount=likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId());
                map.put("likeCount",likeCount);
                discussPosts.add(map);
            }
        }
        return JSON.toJSON(discussPosts).toString();
    }

/*    @RequestMapping(value = "/error",method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }*/

}
