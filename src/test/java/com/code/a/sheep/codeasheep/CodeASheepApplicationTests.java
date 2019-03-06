package com.code.a.sheep.codeasheep;

import com.code.a.sheep.codeasheep.domain.FacetValue;
import com.code.a.sheep.codeasheep.domain.SearchResult;
import com.code.a.sheep.codeasheep.interfaces.DocumentSearcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeASheepApplicationTests {

    @Autowired
    private DocumentSearcher documentSearcher;

    @Test
    public void simpleSearch() {
        SearchResult result = documentSearcher.searchDocuments("boas", Collections.singletonList("chapter"));

        assertEquals(5, result.getNbHits());
        assertEquals(FacetValue.builder().key("Chapitre 1").count(3).build(),
                result.getFacets().get(0).getValues().get(0));
    }
}

