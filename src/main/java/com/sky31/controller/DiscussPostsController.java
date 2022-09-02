package com.sky31.controller;

import com.alibaba.fastjson2.JSON;
import com.sky31.domain.*;
import com.sky31.event.EventProducer;
import com.sky31.mapper.DiscussPostRepository;
import com.sky31.service.*;
import com.sky31.utils.Constant;
import com.sky31.utils.HostHolder;
import com.sky31.utils.Md5AndJsonUtil;
import com.sky31.utils.RedisKeyUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 15:19
 */
@RestController
@ResponseBody
@RequestMapping("/api/discuss")
@Getter
public class DiscussPostsController implements Constant {
    @Autowired
    DiscussPostService discussPostService;

    @Autowired
    CommentService commentService;

    @Autowired
    ElasticsearchService elasticsearchService;

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    UserService userService;

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    RedisTemplate redisTemplate;

    private static Integer sum = 0;

    @PostMapping("/add")
    public String addDiscussPost(String title, String content) {
        sum += 1;
        User user = hostHolder.getUser();
        if (user == null) {
            return Md5AndJsonUtil.getJSONString(403, "请先登录");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPost.setMilsTime(System.currentTimeMillis());
        discussPostService.addDiscussPost(discussPost);
        Event event = new Event();
        event.setTopic(TOPIC_PUBLISH).setUserId(user.getId()).setEntityType(ENTITY_TYPE_POST).setEntityId(discussPost.getId());
        eventProducer.fireEvent(event);
        //计算帖子分数
        String redisKey = RedisKeyUtil.getPostScore();
        redisTemplate.opsForSet().add(redisKey, discussPost.getId());
        discussPostRepository.saveAll(discussPostService.findByContent(content));
        return Md5AndJsonUtil.getJSONString(0, "发布问题成功");
    }

    public Integer getSum(){
        return sum;
    }
    @GetMapping("/questionCount")
    public Object questionCount() {
        return JSON.toJSON(sum);
    }

    @GetMapping("/increase")
    public void increase() {
        sum++;
    }

    @RequestMapping(path = "/top", method = RequestMethod.POST)
    public Object setTop(int id) {
        discussPostService.updateType(id, 1);
//        User user = hostHolder.getUser();
//        Event event = new Event();
//        event.setTopic(TOPIC_PUBLISH).setUserId(user.getId()).setEntityType(ENTITY_TYPE_POST).setEntityId(id);
//        eventProducer.fireEvent(event);
        return JSON.toJSON("置顶成功");
    }

    @RequestMapping(path = "/info", method = RequestMethod.GET)
    public Object getDiscussPost(int id) {
        DiscussPost discussPost = discussPostService.getDiscussPost(id);
        return JSON.toJSON(discussPost);
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Object setDelete(int id) {
        discussPostService.updateType(id, 2);
//        User user = hostHolder.getUser();
//        Event event = new Event();
//        event.setTopic(TOPIC_DELETE).setUserId(user.getId()).setEntityType(ENTITY_TYPE_POST).setEntityId(id);
//        eventProducer.fireEvent(event);
        return JSON.toJSON("删除成功");
    }

    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public Object deletePostByUser(int id) {
        int userIdSQL = discussPostService.deletePostByUser(id);
        Integer userId = hostHolder.getUser().getId();
        if (userIdSQL == userId) {
            discussPostService.updateType(id, 2);
            User user = hostHolder.getUser();
            Event event = new Event();
            event.setTopic(TOPIC_DELETE).setUserId(user.getId()).setEntityType(ENTITY_TYPE_POST).setEntityId(id);
            eventProducer.fireEvent(event);
            return JSON.toJSON("删除成功");
        } else {
            return Md5AndJsonUtil.getJSONString(1, "当前问题并不是此用户所发送");
        }
    }


    @RequestMapping(value = "/detail/{discussPostId}", method = RequestMethod.GET)
    public Object getDiscussPost(@PathVariable("discussPostId") int discussPostId, Page page) {
        DiscussPost discussPost = discussPostService.selectDiscussPostById(discussPostId);
        User user = userService.findUserById(discussPost.getUserId());
        int likeCount = (int) likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        int likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), discussPostId, ENTITY_TYPE_POST);
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("discussPost", discussPost);
//        page.setLimit(30);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(discussPost.getCommentCount());
        List<Comment> comments = commentService.selectCommentsByEntity(ENTITY_TYPE_POST, discussPostId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (comments != null) {
            for (Comment comment : comments) {
                Map<String, Object> commentVo = new HashMap<>();
                commentVo.put("comment", comment);
                commentVo.put("user", userService.findUserById(comment.getUserId()));
                likeCount = (int) likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeCount", likeCount);
                likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), comment.getId(), ENTITY_TYPE_COMMENT);
                commentVo.put("likeStatus", likeStatus);
                List<Comment> replyList = commentService.selectCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList.size() != 0) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        replyVo.put("reply", reply);
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        likeCount = (int) likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeCount", likeCount);
                        likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), reply.getId(), ENTITY_TYPE_COMMENT);
                        replyVo.put("likeStatus", likeStatus);
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);
                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);
                int replyCount = commentService.selectCountByEntity(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);
                int commentCount = commentService.getCommentCount(discussPostId);
                commentVo.put("commentCount", commentCount);
                commentVoList.add(commentVo);
            }
        }
        commentVoList.add(map);
        commentVoList.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                if (o1.get("likeCount") != null && o2.get("likeCount") != null) {
                    return (Integer) o2.get("likeCount") - (Integer) o1.get("likeCount");
                } else {
                    return 0;
                }
            }
        });
        return JSON.toJSON(commentVoList);
    }
}
