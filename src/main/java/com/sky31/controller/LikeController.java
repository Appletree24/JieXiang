package com.sky31.controller;

import com.sky31.domain.Event;
import com.sky31.domain.User;
import com.sky31.event.EventProducer;
import com.sky31.service.LikeService;
import com.sky31.utils.Constant;
import com.sky31.utils.HostHolder;
import com.sky31.utils.Md5AndJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/3
 * @TIME 11:29
 */
@RestController
@ResponseBody
@RequestMapping("/api")
public class LikeController implements Constant {
    @Autowired
    private LikeService likeService;

    @Autowired
    EventProducer eventProducer;
    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId, int postId) {
        User user = hostHolder.getUser();
        likeService.like(user.getId(), entityType, entityId, entityUserId);
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityId, entityType);
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);
        if (likeStatus == 1) {
            Event event = new Event();
            event.setTopic(TOPIC_LIKE)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId", postId);
            eventProducer.fireEvent(event);
        }
        return Md5AndJsonUtil.getJSONString(0, "点赞成功", map);
    }
}
