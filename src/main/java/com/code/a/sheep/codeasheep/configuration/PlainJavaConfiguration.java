package com.code.a.sheep.codeasheep.configuration;

import com.code.a.sheep.codeasheep.plain.schema.PlainJavaSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure the application with a simple plain Java implementation
 */
@Configuration
public class PlainJavaConfiguration {

  @Bean
  public PlainJavaSchema createLuceneSchema() {
    return new PlainJavaSchema();
  }
}
