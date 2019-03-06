package com.code.a.sheep.codeasheep.reader;

import com.code.a.sheep.codeasheep.interfaces.DocumentIndexer;
import org.apache.lucene.document.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

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

import static com.code.a.sheep.codeasheep.domain.DocumentFields.*;

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

    @Value("classpath:le-petit-prince.txt")
    private Resource resourceFile;
    private String currentChapter;

    public LittlePrinceReader(DocumentIndexer documentIndexer) throws IOException {
        documentIndexer.indexDocuments(read());
    }

    /**
     * Read the file and produces a list of {@link Document}
     * In real life this should be streamed in order to control memory usage
     *
     * @return
     */
    private List<Map<String, Object>> read() {
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

    /**
     * Convert a line into a Map<String, Object> ready to be indexed in the search engine.
     *
     * @param readLine line to convert
     * @return the converted line as a Map<String, Object>
     */
    private Map<String, Object> createDocumentFromLine(String readLine) {
        Map<String, Object> document = new HashMap<>();

        document.put(TEXT.getName(), readLine);

        // If we are on a chapter line
        if (readLine.startsWith("Chapitre")) {
            currentChapter = readLine;
        } else {

            // Dialog mark
            if (readLine.startsWith("-")) {
                document.put(IS_DIALOG.getName(), true);
            }
            // Question mark
            if (readLine.endsWith("?")) {
                document.put(IS_QUESTION.getName(), true);
            }
        }

        document.put(CHAPTER.getName(), currentChapter);

        return document;
    }
}
