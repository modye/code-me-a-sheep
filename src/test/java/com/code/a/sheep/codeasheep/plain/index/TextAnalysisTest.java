package com.code.a.sheep.codeasheep.plain.index;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TextAnalysisTest {

    @Test
    public void defaultAnalysis() {
        List<String> analysis = TextAnalysis.defaultAnalysis("élémentAire Pr1nce");

        assertEquals(Arrays.asList("elementaire","pr1nce"), analysis);
    }
}