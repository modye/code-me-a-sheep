package com.code.a.sheep.codeasheep;

<<<<<<< HEAD
import com.code.a.sheep.codeasheep.domain.SearchResult;
=======
import com.code.a.sheep.codeasheep.lucene.LuceneDocumentSearcher;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.TopDocs;
>>>>>>> fix(search): add a unit test for simple searching
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

<<<<<<< HEAD
=======
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

>>>>>>> fix(search): add a unit test for simple searching
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeASheepApplicationTests {

<<<<<<< HEAD
    @Autowired
    private DocumentSearcher documentSearcher;

    @Test
    public void simpleSearch() {
        SearchResult result = documentSearcher.searchDocuments("boas");

        assertEquals(5, result.getNbHits());
    }
=======
	@Autowired
	private DocumentSearcher documentSearcher;

	@Test
	public void simpleSearch() throws IOException, QueryNodeException {
		// TODO should be generic (Not Lucene only)
		TopDocs result = documentSearcher.searchDocuments("text:boas");

		assertEquals(5, result.totalHits);
	}
>>>>>>> fix(search): add a unit test for simple searching

}

