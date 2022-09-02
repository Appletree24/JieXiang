package com.sky31.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky31.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 18:25
 */
@Mapper
@Repository
public interface CommentMapper extends BaseMapper<Comment> {
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType,int entityId);

    int insertComment(Comment comment);

    int deleteCommentByUser(int id);

    int updateComment(int id);

    int getCommentCount(int id);

}
