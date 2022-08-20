package com.sky31.service.impl;

import com.sky31.domain.DiscussPost;
import com.sky31.mapper.DiscussPostMapper;
import com.sky31.service.DiscussPostService;
import com.sky31.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 14:19
 */
@Service
public class DiscussPostServiceImpl implements DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;


    @Override
    public List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int type) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit,type);
    }

    @Override
    public int selectDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }


    @Override
    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));
        post.setMilsTime(System.currentTimeMillis());
        return discussPostMapper.insert(post);
    }

    @Override
    public DiscussPost selectDiscussPostById(int id) {
        return discussPostMapper.selectById(id);
    }

    public int updateCommentCount(int id, int commentCount) {
        return discussPostMapper.updateCommentCountInt(id, commentCount);
    }

    @Override
    public int updateType(int id, int type) {
        return discussPostMapper.updateType(id,type);
    }

    @Override
    public int updateStatus(int id, int status) {
        return discussPostMapper.updateStatus(id, status);
    }
}
