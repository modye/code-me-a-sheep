package com.code.a.sheep.codeasheep;

import com.code.a.sheep.codeasheep.domain.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeASheepApplicationTests {

    @Autowired
    private DocumentSearcher documentSearcher;

    @Test
    public void simpleSearch() {
        SearchResult result = documentSearcher.searchDocuments("boas");

        assertEquals(5, result.getNbHits());
    }

}

