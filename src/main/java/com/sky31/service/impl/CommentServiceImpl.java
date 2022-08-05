package com.sky31.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky31.domain.Comment;
import com.sky31.mapper.CommentMapper;
import com.sky31.service.CommentService;
import com.sky31.service.DiscussPostService;
import com.sky31.utils.Constant;
import com.sky31.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 18:28
 */
@Service
public class CommentServiceImpl implements CommentService, Constant {
    @Autowired
    CommentMapper commentMapper;

    @Autowired
    SensitiveFilter sensitiveFilter;

    @Autowired
    DiscussPostService discussPostService;

    @Override
    public List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType,entityId,offset,limit);
    }

    @Override
    public int selectCountByEntity(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType,entityId);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows=commentMapper.insertComment(comment);
        if (comment.getEntityType()==ENTITY_TYPE_POST){
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(),count);
        }
        return rows;
    }

    @Override
    public Comment selectCommentById(int id) {
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Comment> wrapper = commentQueryWrapper.eq("id", id);
        return commentMapper.selectOne(wrapper);
    }
}
