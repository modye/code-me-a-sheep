package com.code.a.sheep.codeasheep.reader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Component used to read the Little Prince book and to produce from each line a well formed {@link Document} having several properties:
 * <ul>
 * <li>text: actual text from the book</li>
 * <li>isDialog: is the phrase a dialog ?</li>
 * <li>isQuestion: is the phrase a question ?</li>
 * <li>chapter: chapter of the phrase</li>
 * </ul>
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LittlePrinceReader {

    @Value("classpath:le-petit-prince.txt")
    private Resource resourceFile;
    private String currentChapter;

    /**
     * Read the file and produces a list of {@link Document}
     *
     * @return
     */
    public List<Document> read() {
        try {
            File resource = new ClassPathResource("le-petit-prince.txt").getFile();
            try (Stream<String> lines = Files.lines(Paths.get(resource.getPath()), Charset.defaultCharset())) {
                return lines
                        .filter(l -> !l.isEmpty())
                        .map(this::createDocumentFromLine)
                        .collect(Collectors.toList());
            }
        } catch (IOException ex) {
            throw new RuntimeException("Oooops, something went bad when reading the Little Prince", ex);
        }
    }

    private Document createDocumentFromLine(String readLine) {
        Document document = new Document();
        document.add(new TextField("text", readLine, Field.Store.YES));

        // If we are on a chapter line
        if (readLine.startsWith("Chapitre")) {
            currentChapter = readLine;
        } else {
            // Dialog mark
            if (readLine.startsWith("-")) {
                document.add(new TextField("isDialog", "true", Field.Store.YES));
            }
            // Question mark
            if (readLine.endsWith("?")) {
                document.add(new TextField("isQuestion", "true", Field.Store.YES));
            }
        }

        document.add(new TextField("chapter", currentChapter, Field.Store.YES));
        return document;
    }
}
