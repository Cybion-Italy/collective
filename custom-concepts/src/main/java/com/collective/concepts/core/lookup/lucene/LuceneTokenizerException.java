package com.collective.concepts.core.lookup.lucene;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LuceneTokenizerException extends Exception {

    public LuceneTokenizerException(String message, Exception e) {
        super(message, e);
    }
}
