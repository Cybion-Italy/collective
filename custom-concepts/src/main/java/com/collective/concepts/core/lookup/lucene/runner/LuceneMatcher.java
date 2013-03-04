package com.collective.concepts.core.lookup.lucene.runner;

import com.collective.concepts.core.Concept;
import com.collective.concepts.core.lookup.ConceptLookup;
import com.collective.concepts.core.lookup.result.ResultListener;

import java.util.concurrent.Callable;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LuceneMatcher implements Callable<LookupReport> {

    private static final String MATCHER = "MATCHER-";

    private int index;

    private Concept concept;

    private String[] tokens;

    private ResultListener resultListener;

    public LuceneMatcher(int index, Concept concept, String[] tokens, ResultListener resultListener) {
        this.index = index;
        this.concept = concept;
        this.tokens = tokens;
        this.resultListener = resultListener;
    }

    public LookupReport call() throws Exception {
        for(String token : tokens) {
            if(concept.getKeywords().contains(token)) {
                ConceptLookup conceptLookup = new ConceptLookup(token, concept);
                resultListener.submitResult(conceptLookup);
                return new LookupReport(getIdentifier(), true);
            }
        }
        return new LookupReport(getIdentifier(), false);
    }

    public String getIdentifier() {
        return MATCHER + index;
    }

}
