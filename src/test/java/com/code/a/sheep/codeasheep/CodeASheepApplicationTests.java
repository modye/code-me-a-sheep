package com.code.a.sheep.codeasheep;

import com.code.a.sheep.codeasheep.domain.SearchResult;
import com.code.a.sheep.codeasheep.interfaces.DocumentIndexer;
import com.code.a.sheep.codeasheep.interfaces.DocumentSearcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("plain-java")
public class CodeASheepApplicationTests {

    @Autowired
    private DocumentSearcher documentSearcher;

    @Autowired
    private DocumentIndexer documentIndexer;

    @Test
    public void simpleSearch() {
        SearchResult result = documentSearcher.searchDocuments("text:boas", Collections.singletonList("chapter"));

        assertEquals(5, result.getNbHits());
        System.out.println(result);
        //      result.getFacets().get(0).getValues().get(0));
    }
}

