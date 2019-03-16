package com.code.a.sheep.codeasheep.configuration;


import com.code.a.sheep.codeasheep.lucene.schema.LuceneBooleanField;
import com.code.a.sheep.codeasheep.lucene.schema.LuceneSchema;
import com.code.a.sheep.codeasheep.lucene.schema.LuceneTextField;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.analysis.util.ClasspathResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

import static com.code.a.sheep.codeasheep.domain.DocumentFields.*;

/**
 * Configure application.
 */
@Configuration
@Profile("lucene")
public class LuceneConfiguration {

    @Bean
    public ResourceLoader resourceLoader() {
        return new ClasspathResourceLoader(LuceneConfiguration.class);
    }

    /**
     * Creates a {@link RAMDirectory}, deprecated but this is for testing purpose
     *
     * @return
     */
    @Bean
    public Directory directory() {
        return new RAMDirectory();
    }

    /**
     * Creates the bean that can write documents in Lucene index.
     *
     * @return
     */
    @Bean
    public IndexWriter indexWriter() {
        try {
            return new IndexWriter(directory(), new IndexWriterConfig(customAnalyzer(resourceLoader())));
        } catch (IOException ex) {
            throw new RuntimeException("Oooops, something went bad when creating index writer", ex);
        }
    }

    /**
     * Creates a {@link CustomAnalyzer} used while indexing/searching the document.
     * This analyzer uses:
     * <ul>
     * <li>{@link StandardTokenizerFactory}: standard tokenizer, cuts on whitespace</li>
     * <li>{@link LowerCaseFilterFactory}: lowercase token filter, lowercases tokens, removes case</li>
     * <li>{@link ASCIIFoldingFilterFactory}: asciifolding token filter, removes "special characters" é,à,...</li>
     * </ul>
     *
     * @param resourceLoader
     * @return
     * @throws IOException
     */
    @Bean
    public CustomAnalyzer customAnalyzer(ResourceLoader resourceLoader) {
        try {
            return CustomAnalyzer.builder(resourceLoader)
                    .withTokenizer(StandardTokenizerFactory.class)
                    .addTokenFilter(LowerCaseFilterFactory.class)
                    .addTokenFilter(ASCIIFoldingFilterFactory.class)
                    .build();
        } catch (IOException ex) {
            throw new RuntimeException("Oooops, something went bad when creating the analyzer", ex);
        }
    }

    /**
     * Creates the schema :)
     * Structure of our documents.
     * <ul>
     * <li>text: this field is used for full text search</li>
     * <li>chapter: this field is used for full text search and also for filtering/faceting purpose</li>
     * <li>isQuestion: this field is used for filtering/faceting purpose</li>
     * <li>isDialog: this field is used for  filtering/faceting purpose</li>
     * </ul>
     *
     * @return
     */
    @Bean
    public LuceneSchema createLuceneSchema() {
        return new LuceneSchema()
                // Text is stored
                .addField(LuceneTextField.builder()
                        .name(TEXT.getName())
                        .isStored(true)
                        .build())
                // Chapter is stored and has a raw field
                .addField(LuceneTextField.builder()
                        .name(CHAPTER.getName())
                        .isStored(true)
                        .withRawField(true)
                        .build())
                // isQuestion is not stored and has a raw field
                .addField(LuceneBooleanField.builder()
                        .name(IS_QUESTION.getName())
                        .isStored(false)
                        .withRawField(true)
                        .build())
                // If the field is not stored, it can be searched but can't be retrieved in the result
                // isDialog is stored and has a raw field
                .addField(LuceneBooleanField.builder()
                        .name(IS_DIALOG.getName())
                        .isStored(true)
                        .withRawField(true)
                        .build());
    }
}
