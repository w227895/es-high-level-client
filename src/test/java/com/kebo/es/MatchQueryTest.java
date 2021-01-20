package com.kebo.es;

import com.kebo.es.service.IndexService;
import com.kebo.es.service.MatchQueryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @Author: kb
 * @Date: 2021-01-20 10:34
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MatchQueryTest {
    @Autowired
    private MatchQueryService matchQueryService;


    @Test
    public void testMatchAllQuery() {
        matchQueryService.matchAllQuery(1,100);
    }

    @Test
    public void testMatchQuery() {
        matchQueryService.matchQuery("address","北京",1,100);
    }

    @Test
    public void testMatchPhraseQuery() {
        matchQueryService.matchPhraseQuery();
    }
}
