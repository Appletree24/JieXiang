package com.sky31.controller;

import com.alibaba.fastjson2.JSON;
import com.sky31.domain.DiscussPost;
import com.sky31.domain.Page;
import com.sky31.service.ElasticsearchService;
import com.sky31.service.LikeService;
import com.sky31.service.UserService;
import com.sky31.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/5
 * @TIME 16:46
 */
@RestController
@ResponseBody
public class SearchController implements Constant {
    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Object search(String keyword, Page page) throws IOException {
        List<DiscussPost> list = elasticsearchService.searchDiscussPost_list(keyword, page.getCurrent() - 1, page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        for (DiscussPost post : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("post", post);
            map.put("user", userService.findUserById(post.getUserId()));
            map.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));
            discussPosts.add(map);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword);
        discussPosts.add(map);
        page.setPath("/search?keyword=" + keyword);
        page.setRows(list.size());
        return JSON.toJSON(discussPosts);
    }
}
