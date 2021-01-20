package com.kebo.es.service;

import com.alibaba.fastjson.JSON;
import com.kebo.es.dto.ResultData;
import com.kebo.es.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @description:
 * @Author: kb
 * @Date: 2021-01-20 14:51
 */
@Service
@Slf4j
public class MatchQueryService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 匹配查询符合条件的所有数据，并设置分页
     */
    public ResultData matchAllQuery(Integer from, Integer size) {
        ResultData rd = new ResultData();
        try {
            // 构建查询条件
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            // 创建查询源构造器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(matchAllQueryBuilder);
            //System.out.println(searchSourceBuilder.size());
            // 设置分页
            searchSourceBuilder.from(from);
            searchSourceBuilder.size(size);
            // 设置排序
            searchSourceBuilder.sort("salary", SortOrder.ASC);
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("users");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            ArrayList<User> al = new ArrayList<>();
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getHits().length > 0) {
                SearchHits hits = searchResponse.getHits();

                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    User user = JSON.parseObject(hit.getSourceAsString(), User.class);
                    al.add(user);
                    // 输出查询信息
                    log.info(user.toString());
                }
            }
            rd.setData(al);
            return rd;
        } catch (IOException e) {
            log.error("", e);
        }
        return rd;
    }


    /**
     * 匹配查询数据
     * 模糊 匹配查询地址含有  “通”   “州”    “区” 这3个字的数据：  有点分词的感觉
     */
    public ResultData matchQuery(String queryCondition, Object condition, Integer from, Integer size) {
        ResultData rd = new ResultData();
        if (from == null) {
            from = 0;
        }
        if (size == null) {
            size = 10;
        }
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery(queryCondition, condition));
            searchSourceBuilder.from(from);
            searchSourceBuilder.size(size);
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("users");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            ArrayList<User> al = new ArrayList<User>();
            int resLength = searchResponse.getHits().getHits().length;
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && resLength > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    User user = JSON.parseObject(hit.getSourceAsString(), User.class);
                    al.add(user);
                    // 输出查询信息
                    log.info(user.toString());
                }
            }
            rd.setData(al);
            rd.setDataSize(resLength);
            return rd;
        } catch (IOException e) {
            log.error("", e);
        }
        return rd;
    }

    /**
     * 词语匹配查询
     * 模糊匹配
     * matchPhraseQuery
     */
    public void matchPhraseQuery() {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("address", "北京"));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("users");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getHits().length > 0) {
                SearchHits hits = searchResponse.getHits();
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
     * 内容在多字段中进行查询
     * multiMatchQuery
     */
    public void matchMultiQuery() {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery("北京市", "address", "remark"));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("users");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getHits().length > 0) {
                SearchHits hits = searchResponse.getHits();
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