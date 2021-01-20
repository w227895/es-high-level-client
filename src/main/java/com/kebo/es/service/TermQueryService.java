package com.kebo.es.service;

/**
 * @description:
 * @Author: kb
 * @Date: 2021-01-20 14:22
 */

import com.alibaba.fastjson.JSON;
import com.kebo.es.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class TermQueryService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 精确查询（查询条件不会进行分词，但是查询内容可能会分词，导致查询不到）
     */
    public void termQuery() {
        try {
            // 构建查询条件（注意：termQuery 支持多种格式查询，如 boolean、int、double、string 等，这里使用的是 string 的查询）
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.termQuery("address.keyword", "北京"));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("users");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            SearchHits hits = searchResponse.getHits();
            if (RestStatus.OK.equals(searchResponse.status()) && hits.getHits().length > 0) {
                System.out.println(hits.getTotalHits());
                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    User user = JSON.parseObject(hit.getSourceAsString(), User.class);
                    // 输出查询信息
                    log.info(user.toString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }

    /**
     * 多个内容在一个字段中进行查询
     */
    public void termsQuery() {
        try {
            // 构建查询条件（注意：termsQuery 支持多种格式查询，如 boolean、int、double、string 等，这里使用的是 string 的查询）
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.termsQuery("name.keyword", "李四","张三"));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("users");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status())) {
                SearchHits hits = searchResponse.getHits();
                System.out.println(hits.getTotalHits());
                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    User user = JSON.parseObject(hit.getSourceAsString(), User.class);
                    // 输出查询信息
                    log.info(user.toString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }

}
