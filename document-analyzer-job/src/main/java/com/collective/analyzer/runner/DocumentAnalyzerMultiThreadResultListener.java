package com.collective.analyzer.runner;

import com.collective.concepts.core.Concept;
import com.collective.concepts.core.lookup.ConceptLookup;
import com.collective.concepts.core.lookup.result.ResultListener;
import com.collective.concepts.core.lookup.result.ResultListnerException;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DocumentAnalyzerMultiThreadResultListener
        implements ResultListener {

    private Set<ConceptLookup> conceptLookups =
            new HashSet<ConceptLookup>();

    public synchronized void submitResult(ConceptLookup conceptLookup)
            throws ResultListnerException {
        conceptLookups.add(conceptLookup);
    }

    public Set<Concept> getConcepts() throws ResultListnerException {
        Set<Concept> concepts = new HashSet<Concept>();
        for(ConceptLookup conceptLookup : conceptLookups) {
            concepts.add(conceptLookup.getConcept());
        }
        return concepts;
    }
}
