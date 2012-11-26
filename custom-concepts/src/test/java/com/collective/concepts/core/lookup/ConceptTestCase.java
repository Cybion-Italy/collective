package com.collective.concepts.core.lookup;

import com.collective.concepts.core.Concept;
import org.testng.Assert;
import org.testng.annotations.Test;

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
        Assert.assertEquals(concept.getMaximumNgramSize(), 1);
        concept.addKeyword("bi gram");
        Assert.assertEquals(concept.getMaximumNgramSize(), 2);
        concept.addKeyword("tri gr am");
        Assert.assertEquals(concept.getMaximumNgramSize(), 3);
    }
}
