package com.sky31.service;

import com.sky31.domain.DiscussPost;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 13:29
 */
@Service
public interface DiscussPostService {
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    int selectDiscussPostRows(@Param("userId") int userId);

    int addDiscussPost(DiscussPost post);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id,int commentCount);
}
