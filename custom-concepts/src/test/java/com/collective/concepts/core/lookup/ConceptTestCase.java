package com.collective.concepts.core.lookup;

import com.collective.concepts.core.Concept;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class ConceptTestCase
{
    @Test
    public void testingMaxNgramSize() {

        String company = "comp";
        String owner = "me";
        String name = "justaname";
        String label = "label";

        Concept concept = new  Concept(company, owner, name, label);
        concept.addKeyword("unigram");
        assertEquals(concept.getMaximumNgramSize(), 1);
        concept.addKeyword("bi gram");
        assertEquals(concept.getMaximumNgramSize(), 2);
        concept.addKeyword("tri gr am");
        assertEquals(concept.getMaximumNgramSize(), 3);
    }
}
