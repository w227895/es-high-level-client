package com.kebo.es.service;

import com.alibaba.fastjson.JSON;
import com.kebo.es.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @description:
 * @Author: kb
 * @Date: 2021-01-20 10:36
 */
@Slf4j
@Service
public class IndexService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public void createIndex() {

        // 创建 Mapping
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("dynamic", true)
                    .startObject("properties")
                    .startObject("name")
                    .field("type", "text")
                    .startObject("fields")
                    .startObject("keyword")
                    .field("type", "keyword")
                    .endObject()
                    .endObject()
                    .endObject()
                    .startObject("address")
                    .field("type", "text")
                    .startObject("fields")
                    .startObject("keyword")
                    .field("type", "keyword")
                    .endObject()
                    .endObject()
                    .endObject()
                    .startObject("title")
                    .field("type", "text")
                    .startObject("fields")
                    .startObject("keyword")
                    .field("type", "keyword")
                    .endObject()
                    .endObject()
                    .endObject()
                    .startObject("age")
                    .field("type", "integer")
                    .endObject()
                    .startObject("salary")
                    .field("type", "float")
                    .endObject()
                    .startObject("createTime")
                    .field("type", "date")
                    .endObject()
                    .endObject()
                    .endObject();
            // 创建索引配置信息，配置
            Settings settings = Settings.builder()
                    .put("index.number_of_shards", 1)
                    .put("index.number_of_replicas", 0)
                    .build();
            // 新建创建索引请求对象，然后设置索引类型（ES 7.0 将不存在索引类型）和 mapping 与 index 配置
            CreateIndexRequest request = new CreateIndexRequest("users", settings);
            request.mapping("doc", mapping);
            // RestHighLevelClient 执行创建索引
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            // 判断是否创建成功
            boolean isCreated = createIndexResponse.isAcknowledged();
            log.info("是否创建成功：{}", isCreated);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除索引
     */
    public void deleteIndex() {
        try {
            // 新建删除索引请求对象
            DeleteIndexRequest request = new DeleteIndexRequest("users");
            // 执行删除索引
            AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
            // 判断是否删除成功
            boolean siDeleted = acknowledgedResponse.isAcknowledged();
            log.info("是否删除成功：{}", siDeleted);
        } catch (IOException e) {
            log.error("", e);
        }
    }

    /**
     * 增加文档信息
     */
    public void addDocument() {
        try {
            // 创建索引请求对象
            IndexRequest indexRequest = new IndexRequest("users");
            //
            indexRequest.id(UUID.randomUUID().toString());
            // 创建员工信息
            User user = new User();
            user.setName("张三");
            user.setAge(30);
            user.setSalary(8000.00f);
            user.setAddress("上海");
            user.setTitle("初级开发工程师");
            user.setCreateTime(new Date());
            // 将对象转换为 byte 数组
            byte[] json = JSON.toJSONBytes(user);
            // 设置文档内容
            indexRequest.source(json, XContentType.JSON);
            // 执行增加文档
            IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            log.info("创建状态：{}", response.status());
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 获取文档信息
     */
    public void getDocument(String id) {
        try {
            // 获取请求对象
            GetRequest getRequest = new GetRequest("users");
            getRequest.id(id);
            // 获取文档信息
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            // 将 JSON 转换成对象
            if (getResponse.isExists()) {
                User user = JSON.parseObject(getResponse.getSourceAsBytes(), User.class);
                log.info("员工信息：{}", user);
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }



    /**
     * 更新文档信息
     */
    public void updateDocument(String id) {
        try {
            // 创建索引请求对象
            UpdateRequest updateRequest = new UpdateRequest("users", id);
            // 设置员工更新信息
            User user = new User();
            user.setSalary(9000.00f);
            user.setAddress("北京");
            // 将对象转换为 byte 数组
            byte[] json = JSON.toJSONBytes(user);
            // 设置更新文档内容
            updateRequest.doc(json, XContentType.JSON);
            // 执行更新文档
            UpdateResponse response = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            log.info("创建状态：{}", response.status());
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 删除文档信息
     */
    public void deleteDocument(String id) {
        try {
            // 创建删除请求对象
            DeleteRequest deleteRequest = new DeleteRequest("users", id);
            // 执行删除文档
            DeleteResponse response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            log.info("删除状态：{}", response.status());
        } catch (IOException e) {
            log.error("", e);
        }
    }

    public void addDocumentTest() {
        try {
            // 创建索引请求对象
            IndexRequest indexRequest = new IndexRequest("users");
            for (int i = 0; i < 20000; i++) {
                indexRequest.id(UUID.randomUUID().toString());
                // 创建员工信息
                User user = new User();
                if (i % 2 == 0) {
                    user.setName("张三");
                    user.setAge(30);
                    user.setSalary(8000.00f);
                } else {
                    user.setName("李四");
                    user.setAge(29);
                    user.setSalary(10000.00f);
                }
                user.setAddress("北京");
                user.setTitle("初级开发工程师");
                user.setCreateTime(new Date());
                // 将对象转换为 byte 数组
                byte[] json = JSON.toJSONBytes(user);
                // 设置文档内容
                indexRequest.source(json, XContentType.JSON);
                // 执行增加文档
                IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
                log.info("创建状态：{}", response.status());
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
