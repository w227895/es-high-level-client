package com.kebo.es;

import com.kebo.es.service.IndexService;
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
public class IndexTest {
    @Autowired
    private IndexService indexService;


    @Test
    public void testCreateIndex() {
        indexService.createIndex();
    }

    @Test
    public void testDeleteIndex() {
        indexService.deleteIndex();
    }

    @Test
    public void testAddDocument() {
        indexService.addDocument();
    }

    @Test
    public void testUpdateDocument() {
        indexService.updateDocument("58a7ec08-bdfa-4c03-92cb-33d887437f5c");
    }

    @Test
    public void testGetDocument() {
        String id = "58a7ec08-bdfa-4c03-92cb-33d887437f5c";
        indexService.getDocument(id);
    }

    @Test
    public void testDeleteDocument() {
        String id = "58a7ec08-bdfa-4c03-92cb-33d887437f5c";
        indexService.deleteDocument(id);
    }


    @Test
    public void testAddDocumentTest() {
        indexService.addDocumentTest();
    }

}
