package com.sky31.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky31.domain.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 13:29
 */
@Mapper
@Repository
public interface DiscussPostMapper extends BaseMapper<DiscussPost> {
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit,int type);

    int selectDiscussPostRows(@Param("userId") int userId);

//    int insertDiscussPost(DiscussPost discussPost);

    int updateCommentCountInt(int id,int commentCount);

    int updateType(int id,int type);

    int updateStatus(int id,int status);

    int deletePostsByUser(int id);

    int getPostCount(int type);

    List<DiscussPost> findByContent(String content);

    DiscussPost getDisscussPost(int id);
}
