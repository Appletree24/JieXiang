package com.sky31.service;

import com.sky31.domain.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 18:28
 */
@Service
public interface CommentService {
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int addComment(Comment comment);

    Comment selectCommentById(int id);

    int deleteCommentByUser(int id);

    int updateComment(int id);

    int getCommentCount(int id);
}
