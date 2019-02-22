package com.code.a.sheep.codeasheep.reader;

import com.code.a.sheep.codeasheep.DocumentIndexer;
import org.apache.lucene.document.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class LittlePrinceReader {

    private final DocumentIndexer documentIndexer;

    @Value("classpath:le-petit-prince.txt")
    private Resource resourceFile;
    private String currentChapter;

    public LittlePrinceReader(DocumentIndexer documentIndexer) {
        this.documentIndexer = documentIndexer;
    }

    @PostConstruct
    private void indexText() throws IOException {
        documentIndexer.indexDocuments(read());
    }

    /**
     * Read the file and produces a list of {@link Document}
     * In real life this should be streamed in order to control memory usage
     *
     * @return
     */
    public List<Map<String, Object>> read() {
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

    private Map<String, Object> createDocumentFromLine(String readLine) {
        Map<String, Object> document = new HashMap<>();


        document.put("text", readLine);

        // If we are on a chapter line
        if (readLine.startsWith("Chapitre")) {
            currentChapter = readLine;
        } else {
            // Dialog mark
            if (readLine.startsWith("-")) {
                document.put("isDialog", true);
            }
            // Question mark
            if (readLine.endsWith("?")) {
                document.put("isQuestion", true);
            }
        }

        document.put("chapter", currentChapter);

        return document;
    }
}
