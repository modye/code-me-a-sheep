package com.code.a.sheep.codeasheep.reader;

import com.code.a.sheep.codeasheep.domain.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
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

    private Resource book;
    private String currentChapter;

    /**
     * Inject the book from the resources.
     *
     * @param book the whole little prince book in French
     */
    public LittlePrinceReader(@Value("classpath:le-petit-prince.txt") Resource book) {
        this.book = book;
    }

    /**
     * Read the file and produces a list of document representation.
     * In real life this should be streamed in order to control memory usage
     *
     * @return the list of domain {@link Document} ready to be indexed
     */
    public List<Document> read() {
        try {
            try (Stream<String> lines = Files.lines(Paths.get(book.getFile().getPath()), Charset.defaultCharset())) {
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
    private Document createDocumentFromLine(String readLine) {

        //TODO-02-a Build a document from the read line and return it
        Document document = new Document();

        // TODO-02-a.1 manage text field
        document.put(TEXT.getName(), readLine);

        // If we are on a chapter line
        if (readLine.startsWith("Chapitre")) {
            currentChapter = readLine;
        } else {

            // TODO-02-a.2 Dialog mark
            if (readLine.startsWith("-")) {
                // manage isDialog field
                document.put(IS_DIALOG.getName(), true);
            }
            // Question mark
            if (readLine.endsWith("?")) {
                // TODO-02-a.3 manage isQuestion field
                document.put(IS_QUESTION.getName(), true);
            }
        }

        document.put(CHAPTER.getName(), currentChapter);
        return document;
    }
}
