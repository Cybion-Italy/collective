package com.collective.concepts.core.lookup.result;

import com.collective.concepts.core.Concept;
import com.collective.concepts.core.lookup.ConceptLookup;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class InMemoryResultListener implements ResultListener {

    private Set<ConceptLookup> conceptLookups = new HashSet<ConceptLookup>();

    public synchronized void submitResult(ConceptLookup conceptLookup)
            throws ResultListnerException {
        conceptLookups.add(conceptLookup);
    }

    public Set<Concept> getConcepts() throws ResultListnerException {
        Set concepts = new HashSet<Concept>();
        for(ConceptLookup conceptLookup : conceptLookups) {
            concepts.add(conceptLookup.getConcept());
        }
        return concepts;
    }

}
