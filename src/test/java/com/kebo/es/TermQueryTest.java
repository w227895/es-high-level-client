package com.kebo.es;

import com.kebo.es.service.IndexService;
import com.kebo.es.service.TermQueryService;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @Author: kb
 * @Date: 2021-01-20 14:29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TermQueryTest {

    @Autowired
    private TermQueryService termQueryService;

    @Test
    public void testTermQuery(){
        termQueryService.termQuery();
    }

    @Test
    public void testTermsQuery(){
        termQueryService.termsQuery();
    }
}
