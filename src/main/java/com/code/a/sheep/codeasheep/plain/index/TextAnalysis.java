package com.code.a.sheep.codeasheep.plain.index;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Simple text analysis that just split on non alphanum character and apply lowercase
 */
public class TextAnalysis {

    /**
     * Applies analysis on a text
     *
     * @param content
     * @return list of produced tokens
     */
    public static List<String> defaultAnalysis(String content) {
        return Arrays.asList(StringUtils.stripAccents(content)
                .split("[^a-zA-Z0-9]"));
    }

    // TODO-04-b Create a new analysis method that applies lowercase filtering logic to produced tokens
}
