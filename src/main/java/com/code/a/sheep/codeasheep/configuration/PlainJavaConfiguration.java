package com.code.a.sheep.codeasheep.configuration;

import com.code.a.sheep.codeasheep.plain.schema.PlainJavaSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configure the application with a simple plain Java implementation
 */
@Configuration
@Profile("plain-java")
public class PlainJavaConfiguration {

  @Bean
  public PlainJavaSchema createPlainJavaSchema() {
    return new PlainJavaSchema();
  }
}
