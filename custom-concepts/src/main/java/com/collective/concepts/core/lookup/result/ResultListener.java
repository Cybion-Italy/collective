package com.collective.concepts.core.lookup.result;

import com.collective.concepts.core.Concept;
import com.collective.concepts.core.lookup.ConceptLookup;
import com.collective.concepts.core.lookup.result.ResultListnerException;

import java.util.Set;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface ResultListener {

    public void submitResult(ConceptLookup conceptLookup)
            throws ResultListnerException;

    public Set<Concept> getConcepts() throws ResultListnerException;

}
