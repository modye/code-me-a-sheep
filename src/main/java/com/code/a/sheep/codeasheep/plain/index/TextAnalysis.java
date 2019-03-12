package com.code.a.sheep.codeasheep.plain.index;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class TextAnalysis {
    public static List<String> defaultAnalysis(String content) {
        return Arrays.asList(StringUtils.stripAccents(content)
                .toLowerCase()
                .split("[^a-zA-Z0-9]"));
    }
}
