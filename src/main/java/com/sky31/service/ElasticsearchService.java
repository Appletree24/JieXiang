package com.sky31.service;

import com.sky31.domain.DiscussPost;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/4
 * @TIME 20:06
 */
@Service
public interface ElasticsearchService {

    void saveDiscussPost(DiscussPost discussPost);

    void deleteDiscussPost(int id);

    Object searchDiscussPost(String keyword, int current, int limit) throws IOException;

    List<DiscussPost> searchDiscussPost_list(String keyword, int current, int limit) throws IOException;

}
