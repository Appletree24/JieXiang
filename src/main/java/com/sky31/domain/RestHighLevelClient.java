package com.sky31.domain;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/4
 * @TIME 18:50
 */
@Component
public class RestHighLevelClient {

    public org.elasticsearch.client.RestHighLevelClient create() throws IOException {
        return new org.elasticsearch.client.RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
    }

}
