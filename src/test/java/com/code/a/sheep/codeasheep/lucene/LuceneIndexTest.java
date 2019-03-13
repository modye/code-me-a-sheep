package com.code.a.sheep.codeasheep.lucene;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.reader.LittlePrinceIndexer;
import com.code.a.sheep.codeasheep.reader.LittlePrinceReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.containsString;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("lucene")
public class LuceneIndexTest {

    @Autowired
    private LittlePrinceReader littlePrinceReader;

    @Autowired
    private LittlePrinceIndexer littlePrinceIndexer;

    @Rule
    public final OutputCapture outputCapture = new OutputCapture();

    @Test
    public void indexAndCommit() {
        // Given
        List<Document> documents = littlePrinceReader.read();

        // When
        littlePrinceIndexer.indexAndCommit(documents);

        // Then
        outputCapture.expect(containsString("Successfully indexed 761 documents"));
    }
}
