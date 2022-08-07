package com.sky31.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sky31.domain.DiscussPost;
import com.sky31.domain.RestHighLevelClient;
import com.sky31.mapper.DiscussPostRepository;
import com.sky31.service.ElasticsearchService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/4
 * @TIME 20:07
 */
@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Override
    public void saveDiscussPost(DiscussPost discussPost) {
        discussPostRepository.save(discussPost);
    }

    @Override
    public void deleteDiscussPost(int id) {
        discussPostRepository.deleteById(id);
    }

    @Override
    public Object searchDiscussPost(String keyword, int current, int limit) throws IOException {
        org.elasticsearch.client.RestHighLevelClient highLevelClient = restHighLevelClient.create();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyword, "title", "content");
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("title").field("content").preTags("<span style='color:red'>").postTags("</span>");
        SearchRequest request = new SearchRequest("discusspost");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().highlighter(highlightBuilder)
                .sort("type", SortOrder.DESC).sort("score", SortOrder.DESC).sort("createTime", SortOrder.DESC)
                .query(multiMatchQueryBuilder)
                .timeout(new TimeValue(60, TimeUnit.SECONDS))
                .from(0)
                .size(10);
        request.source(searchSourceBuilder);
        SearchResponse response = highLevelClient.search(request, RequestOptions.DEFAULT);
        List<DiscussPost> list = new LinkedList<>();
        for (SearchHit hit:response.getHits().getHits()){
            DiscussPost discussPost = JSONObject.parseObject(hit.getSourceAsString(), DiscussPost.class);
            // 处理高亮显示的结果
            HighlightField titleField = hit.getHighlightFields().get("title");
            if (titleField != null) {
                discussPost.setTitle(titleField.getFragments()[0].toString());
            }
            HighlightField contentField = hit.getHighlightFields().get("content");
            if (contentField != null) {
                discussPost.setContent(contentField.getFragments()[0].toString());
            }
            list.add(discussPost);
        }
        return JSON.toJSON(list);
    }

    @Override
    public List<DiscussPost> searchDiscussPost_list(String keyword, int current, int limit) throws IOException {
        org.elasticsearch.client.RestHighLevelClient highLevelClient = restHighLevelClient.create();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyword, "title", "content");
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("title").field("content").preTags("<span style='color:red'>").postTags("</span>");
        SearchRequest request = new SearchRequest("discusspost");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().highlighter(highlightBuilder)
                .sort("type", SortOrder.DESC).sort("score", SortOrder.DESC).sort("createTime", SortOrder.DESC)
                .query(multiMatchQueryBuilder)
                .timeout(new TimeValue(60, TimeUnit.SECONDS))
                .from(0)
                .size(10);
        request.source(searchSourceBuilder);
        SearchResponse response = highLevelClient.search(request, RequestOptions.DEFAULT);
        List<DiscussPost> list = new LinkedList<>();
        for (SearchHit hit:response.getHits().getHits()){
            DiscussPost discussPost = JSONObject.parseObject(hit.getSourceAsString(), DiscussPost.class);
            // 处理高亮显示的结果
            HighlightField titleField = hit.getHighlightFields().get("title");
            if (titleField != null) {
                discussPost.setTitle(titleField.getFragments()[0].toString());
            }
            HighlightField contentField = hit.getHighlightFields().get("content");
            if (contentField != null) {
                discussPost.setContent(contentField.getFragments()[0].toString());
            }
            list.add(discussPost);
        }
        return list;
    }
}
