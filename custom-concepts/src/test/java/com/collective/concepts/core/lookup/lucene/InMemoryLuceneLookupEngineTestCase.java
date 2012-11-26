package com.collective.concepts.core.lookup.lucene;

import com.collective.concepts.core.Concept;
import com.collective.concepts.core.UserDefinedConceptStore;
import com.collective.concepts.core.UserDefinedConceptStoreException;
import com.collective.concepts.core.lookup.UserDefinedConceptLookupEngineException;
import com.collective.concepts.core.lookup.result.InMemoryResultListener;
import com.collective.concepts.core.lookup.result.ResultListener;
import com.collective.concepts.core.lookup.result.ResultListnerException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class InMemoryLuceneLookupEngineTestCase
{
    private UserDefinedConceptStore inMemoryConceptStore;

    private long otherUserId = 525225662L;

    private LuceneLookupEngine engine;

    @BeforeTest
    public void setUp() throws UserDefinedConceptStoreException
    {
        inMemoryConceptStore = new InMemoryConceptStore();
        inMemoryConceptStore.storeConcept(this.otherUserId, getOneKeywordConcept());
        engine = new LuceneLookupEngine(inMemoryConceptStore, 50);
    }

    @Test
    public void testLookUp() throws IOException,
            UserDefinedConceptLookupEngineException, ResultListnerException, UserDefinedConceptStoreException
    {
        String text = "just a text with a unigram keyword";
        ResultListener resultListener = new InMemoryResultListener();
        try {
            engine.lookup(text, otherUserId, resultListener);
        } finally {
            this.inMemoryConceptStore.deleteAllConcepts(otherUserId);
        }
        Assert.assertTrue(resultListener.getConcepts().size() > 0);
    }

    private Concept getOneKeywordConcept()
    {
        String company = "comp";
        String owner = "me";
        String name = "justaname";
        String label = "label";

        Concept concept = new  Concept(company, owner, name, label);
        concept.addKeyword("unigram");
        return concept;
    }
}
