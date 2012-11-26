package com.collective.concepts.core.lookup.lucene;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author DAvide Palmisano ( dpalmisano@gmail.com )
 */
public class LuceneTokenizerTestCase {

    private final String text = "just a simple text for research and development";

    private LuceneTokenizer tokenizer;

    @BeforeTest
    public void setUp() {
        tokenizer = new LuceneTokenizer(text, 2);
    }

    @AfterTest
    public void tearDown() {}

    @Test
    public void test() throws LuceneTokenizerException {
        String[] strings = tokenizer.tokenize();
        for(String string : strings) {
            System.out.println(string);
        }
    }
}
