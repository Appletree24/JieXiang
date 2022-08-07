package com.sky31.mapper;

import com.sky31.domain.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/4
 * @TIME 15:12
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost,Integer> {

}
