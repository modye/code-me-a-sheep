package com.code.a.sheep.codeasheep.configuration;

import com.code.a.sheep.codeasheep.plain.schema.PlainJavaField;
import com.code.a.sheep.codeasheep.plain.schema.PlainJavaSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.code.a.sheep.codeasheep.domain.DocumentFields.*;

/**
 * Configure the application with a simple plain Java implementation
 */
@Configuration
@Profile("plain-java")
public class PlainJavaConfiguration {

    @Bean
    public PlainJavaSchema createPlainJavaSchema() {
        return new PlainJavaSchema()
                // Text is stored
                .addField(PlainJavaField.builder()
                        .name(TEXT.getName())
                        .build())
                // Chapter is stored and has a raw field
                .addField(PlainJavaField.builder()
                        .name(CHAPTER.getName())
                        //TODO-08-f Pass it to false
                        //TODO-08-h Set it back to true :)
                        .withColumnarStorage(true)
                        .build())
                // isQuestion is not stored and has a raw field
                .addField(PlainJavaField.builder()
                        .name(IS_QUESTION.getName())
                        .withColumnarStorage(true)
                        .build())
                // isDialog is not stored and has a raw field
                .addField(PlainJavaField.builder()
                        .name(IS_DIALOG.getName())
                        .withColumnarStorage(true)
                        .build());
    }
}
