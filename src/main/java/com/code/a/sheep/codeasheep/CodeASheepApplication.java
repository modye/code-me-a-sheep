package com.code.a.sheep.codeasheep;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.interfaces.DocumentSearcher;
import com.code.a.sheep.codeasheep.reader.LittlePrinceIndexer;
import com.code.a.sheep.codeasheep.reader.LittlePrinceReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

/**
 * This application exposes a REST API used to search on the Little Prince book
 * TODO-09 You can run the app and play for the API as you did in first lab
 * For example: http://localhost:8080/search?text=mouton%20caisse&facet.field=isDialog&facet.field=chapter
 */
@SpringBootApplication
public class CodeASheepApplication {

    /**
     * Bootstrap the Spring context and index the Little prince book
     *
     * @param args
     */
    public static void main(String[] args) {

        // Run the application
        ConfigurableApplicationContext run = SpringApplication.run(CodeASheepApplication.class, args);

        // Retrieve the reader to read the little prince book
        LittlePrinceReader reader = run.getBean(LittlePrinceReader.class);
        List<Document> documents = reader.read();

        // Retrieve the indexer to index the documents
        LittlePrinceIndexer indexer = run.getBean(LittlePrinceIndexer.class);
        indexer.indexAndCommit(documents);

        // Initializes the document searcher
        DocumentSearcher searcher = run.getBean(DocumentSearcher.class);
        searcher.initializeIndexSearcher();
    }
}
