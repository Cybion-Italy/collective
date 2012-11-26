package com.collective.concepts.core.lookup.lucene;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public final class LuceneTokenizer {

    private String text;

    private int maximumNgramSize;

    public LuceneTokenizer(String text, int maximumNgramSize) {
        this.text = text;
        this.maximumNgramSize = maximumNgramSize;
    }

    public String[] tokenize() throws LuceneTokenizerException {
        ShingleAnalyzerWrapper saw = new ShingleAnalyzerWrapper(
                new SimpleAnalyzer(Version.LUCENE_34),
                2,
                maximumNgramSize
        );
        saw.setOutputUnigrams(true);
        saw.setTokenSeparator(" ");
        Reader reader = new StringReader(text);
        TokenStream ts = saw.tokenStream(null, reader);
        Set<String> strings = new HashSet<String>();
        try {
            while (ts.incrementToken()) {
                CharTermAttribute attribute = ts.getAttribute(CharTermAttribute.class);
                strings.add(attribute.toString().replace("_", "").trim());
            }
        } catch (IOException e) {
            throw new LuceneTokenizerException("", e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException("", e);
            }
        }
        return strings.toArray(new String[strings.size()]);
    }

}
