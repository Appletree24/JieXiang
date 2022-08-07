package com.sky31;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sky31.domain.DiscussPost;
import com.sky31.domain.RestHighLevelClient;
import com.sky31.mapper.DiscussPostRepository;
import com.sky31.service.DiscussPostService;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @AUTHOR Zzh
 * @DATE 2022/8/4
 * @TIME 11:11
 */
@SpringBootTest
public class ElasticsearchTests {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private DiscussPostRepository discussPostRepository;


    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void testInsert() {
        discussPostRepository.saveAll(discussPostService.selectDiscussPosts(101, 0, 100));
        discussPostRepository.saveAll(discussPostService.selectDiscussPosts(102, 0, 100));
        discussPostRepository.saveAll(discussPostService.selectDiscussPosts(103, 0, 100));
        discussPostRepository.saveAll(discussPostService.selectDiscussPosts(111, 0, 100));
        discussPostRepository.saveAll(discussPostService.selectDiscussPosts(112, 0, 100));
        discussPostRepository.saveAll(discussPostService.selectDiscussPosts(131, 0, 100));
        discussPostRepository.saveAll(discussPostService.selectDiscussPosts(132, 0, 100));
        discussPostRepository.saveAll(discussPostService.selectDiscussPosts(133, 0, 100));
    }

    @Test
    public void testUpdate() {
        DiscussPost post = discussPostService.selectDiscussPostById(231);
        post.setContent("我是新人，使劲灌水");
        discussPostRepository.save(post);
    }

    @Test
    public void testSearch() throws IOException {
//        NativeSearchQuery build = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
//                .withSorts(SortBuilders.fieldSort("type").order(SortOrder.DESC))
//                .withSorts(SortBuilders.fieldSort("score").order(SortOrder.DESC))
//                .withSorts(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
//                .withPageable(PageRequest.of(0, 10))
//                .withHighlightFields(
//                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
//                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
//                ).build();

        org.elasticsearch.client.RestHighLevelClient highLevelClient = restHighLevelClient.create();
        SearchRequest request = new SearchRequest("discusspost");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content");
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title").field("content").preTags("<em>").postTags("</em>");
        builder.highlighter(highlightBuilder);
        builder.sort("type", SortOrder.DESC).sort("score", SortOrder.DESC).sort("createTime", SortOrder.DESC);
        builder.query(multiMatchQueryBuilder);
        builder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        builder.from(0);
        builder.size(10);
        request.source(builder);
        SearchResponse response = highLevelClient.search(request, RequestOptions.DEFAULT);
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            list.add(sourceAsMap);
        }
        System.out.println(list);

        System.out.println(JSONObject.toJSONString(Arrays.toString(response.getHits().getHits())));
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
            //输出高亮情况
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlightField = highlightFields.get("title");
            if (highlightField == null) continue;
            Text[] fragments = highlightField.getFragments();
            StringBuilder title = new StringBuilder();
            for (Text fragment : fragments) {
                title.append(fragment);
            }
            //使用有高亮的结果替换原本的结果，
            sourceAsMap.put("title", title.toString());
            System.out.println(sourceAsMap);
        }

    }
}


//    @Test
//    public void test() throws IOException {
//        RestClient restClient = RestClient.builder(new HttpHost("120.24.64.221", 9200)).build();
//        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
//        ElasticsearchClient client = new ElasticsearchClient(transport);
////        DiscussPost post1 = discussPostService.selectDiscussPostById(241);
////        DiscussPost post2 = discussPostService.selectDiscussPostById(242);
////        DiscussPost post3 = discussPostService.selectDiscussPostById(243);
////        CreateResponse createResponse = client.create(e -> e.index("discusspost").id("1").document(post1));
////        client.create(e->e.index("discusspost").id("2").document(post2));
////        client.create(e->e.index("discusspost").id("3").document(post3));
////        System.out.println(createResponse);
//        List<DiscussPost> post1 = discussPostService.selectDiscussPosts(101, 0, 100);
//        List<DiscussPost> post2 = discussPostService.selectDiscussPosts(102, 0, 100);
//        List<DiscussPost> discussPosts = discussPostService.selectDiscussPosts(103, 0, 100);
//        List<DiscussPost> discussPosts1 = discussPostService.selectDiscussPosts(111, 0, 100);
//        List<DiscussPost> discussPosts2 = discussPostService.selectDiscussPosts(112, 0, 100);
//        List<DiscussPost> discussPosts3 = discussPostService.selectDiscussPosts(131, 0, 100);
//        List<DiscussPost> discussPosts4 = discussPostService.selectDiscussPosts(132, 0, 100);
//        List<DiscussPost> discussPosts5 = discussPostService.selectDiscussPosts(133, 0, 100);
//        List<DiscussPost> discussPosts6 = discussPostService.selectDiscussPosts(134, 0, 100);
//        Map<String,Object> jsonMap=new HashMap<>();
//        jsonMap.put("posts3",discussPosts6);
//        client.create(e->e.index("discusspost").document(jsonMap).id("9"));
//        transport.close();
//        restClient.close();
//    }

