package com.code.a.sheep.codeasheep.configuration;

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
    public CustomAnalyzer create(ResourceLoader resourceLoader) throws IOException {
        return CustomAnalyzer.builder(resourceLoader)
                .withTokenizer(StandardTokenizerFactory.class)
                .addTokenFilter(LowerCaseFilterFactory.class)
                .addTokenFilter(ASCIIFoldingFilterFactory.class)
                .build();
    }
}
