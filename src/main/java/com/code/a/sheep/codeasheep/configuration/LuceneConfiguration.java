package com.code.a.sheep.codeasheep.configuration;

import com.code.a.sheep.codeasheep.lucene.schema.LuceneBooleanField;
import com.code.a.sheep.codeasheep.lucene.schema.LuceneField;
import com.code.a.sheep.codeasheep.lucene.schema.LuceneSchema;
import com.code.a.sheep.codeasheep.lucene.schema.LuceneTextField;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.analysis.util.ClasspathResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Configure application.
 */
@Configuration
public class LuceneConfiguration {

  @Bean
  public ResourceLoader resourceLoader() {
    return new ClasspathResourceLoader(LuceneConfiguration.class);
  }

  @Bean
  public CustomAnalyzer createLuceneAnalyzer(ResourceLoader resourceLoader) throws IOException {
    return CustomAnalyzer.builder(resourceLoader)
            .withTokenizer(StandardTokenizerFactory.class)
            .addTokenFilter(LowerCaseFilterFactory.class)
            .addTokenFilter(ASCIIFoldingFilterFactory.class)
            .build();
  }

  @Bean
  public LuceneSchema createLuceneSchema() {
    return new LuceneSchema()
            .addField(LuceneTextField.builder().name("text").isStored(true).build())
            .addField(LuceneTextField.builder().name("chapter").isStored(true).withRawField(true).build())
            .addField(LuceneBooleanField.builder().name("isQuestion").isStored(true).withRawField(true).build())
            .addField(LuceneBooleanField.builder().name("isDialog").isStored(true).withRawField(true).build());
  }
}
