package com.code.a.sheep.codeasheep;

import com.code.a.sheep.codeasheep.reader.LittlePrinceReader;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class CodeASheepApplication {

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext run = SpringApplication.run(CodeASheepApplication.class, args);

        // Retrieve Analyzer
        CustomAnalyzer customAnalyzer = run.getBean(CustomAnalyzer.class);
        // Read the file and get its documents
        LittlePrinceReader littlePrinceReader = run.getBean(LittlePrinceReader.class);
        List<Document> documents = littlePrinceReader.read();

        // Index the document in Lucene
        try (RAMDirectory directory = new RAMDirectory(); IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(customAnalyzer))) {
            indexWriter.addDocuments(documents);
            // flush to make the document searchable
            indexWriter.flush();

            // create an index reader
            try (DirectoryReader reader = DirectoryReader.open(indexWriter)) {
                IndexSearcher searcher = new IndexSearcher(reader);

                int count = searcher.count(new TermQuery(new Term("text", "boas")));
                System.out.println("The little prince has " + count + " occurences of word boas :)");
            }
        }
    }

}

