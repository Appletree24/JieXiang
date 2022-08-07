package com.sky31.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sky31.domain.Message;
import com.sky31.domain.Page;
import com.sky31.domain.User;
import com.sky31.service.MessageService;
import com.sky31.service.UserService;
import com.sky31.utils.Constant;
import com.sky31.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/3
 * @TIME 23:35
 */
@Controller
public class MessageController implements Constant {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    HostHolder hostHolder;


    private List<Integer> getLetterIds(List<Message> letterList) {
        List<Integer> ids = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                if (hostHolder.getUser().getId() == message.getToId() && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }

    @RequestMapping(value = "/notice/list", method = RequestMethod.GET)
    @ResponseBody
    public Object getNoticeList() {
        User user = hostHolder.getUser();
        Map<String, Map<String, Object>> map = new HashMap<>();
        Message latestNotice = messageService.findLatestNotice(user.getId(), TOPIC_COMMENT);
        if (latestNotice != null) {
            Map<String, Object> messageVo = new HashMap<>();
            messageVo.put("message", latestNotice);
            String content = HtmlUtils.htmlUnescape(latestNotice.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
            messageVo.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVo.put("entityType", data.get("entityType"));
            messageVo.put("entityId", data.get("entityId"));
            messageVo.put("postId", data.get("postId"));
            int count = messageService.findNoticeCount(user.getId(), TOPIC_COMMENT);
            messageVo.put("count", count);
            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_COMMENT);
            messageVo.put("unread", unread);
            map.put("comment", messageVo);
        }
        latestNotice = messageService.findLatestNotice(user.getId(), TOPIC_LIKE);
        if (latestNotice != null) {
            Map<String, Object> messageVo = new HashMap<>();
            messageVo.put("message", latestNotice);
            String content = HtmlUtils.htmlUnescape(latestNotice.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
            messageVo.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVo.put("entityType", data.get("entityType"));
            messageVo.put("entityId", data.get("entityId"));
            messageVo.put("postId", data.get("postId"));
            int count = messageService.findNoticeCount(user.getId(), TOPIC_LIKE);
            messageVo.put("count", count);
            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_LIKE);
            messageVo.put("unread", unread);
            int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
            messageVo.put("unreadNotice", noticeUnreadCount);
            map.put("like", messageVo);
        }
        return JSON.toJSON(map);
    }

    @ResponseBody
    @RequestMapping(value = "/notice/detail/{topic}", method = RequestMethod.GET)
    public Object getNoticeDetail(@PathVariable("topic") String topic, Page page) {
        User user = hostHolder.getUser();
        page.setLimit(5);
        page.setPath("/notice/detail/" + topic);
        page.setRows(messageService.findNoticeCount(user.getId(), topic));
        List<Message> noticeList = messageService.selectNotices(user.getId(), topic, page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeVoList = new ArrayList<>();
        if (noticeList != null) {
            for (Message notice : noticeList) {
                Map<String, Object> map = new HashMap<>();
                map.put("notice", notice);
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
                map.put("user", userService.findUserById((Integer) data.get("userId")));
                map.put("entityType", data.get("entityType"));
                map.put("entityId", data.get("entityId"));
                map.put("postId", data.get("postId"));
                map.put("fromUser", userService.findUserById(notice.getFromId()));
                noticeVoList.add(map);
            }
        }
        List<Integer> ids=getLetterIds(noticeList);
        if (!ids.isEmpty()){
            messageService.readMessage(ids);
        }
        return JSON.toJSON(noticeVoList);
    }
}
