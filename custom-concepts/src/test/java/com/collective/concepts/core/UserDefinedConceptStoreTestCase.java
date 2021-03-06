package com.collective.concepts.core;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.configuration.ConfigurationManager;
import tv.notube.commons.storage.kvs.mybatis.MyBatisKVStore;
import tv.notube.commons.storage.model.fields.serialization.SerializationManager;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class UserDefinedConceptStoreTestCase {

    private static final String CONFIGURATION = "kvs-configuration.xml";

    private UserDefinedConceptStore store;
    private static final Logger LOGGER =
            Logger.getLogger(UserDefinedConceptStoreTestCase.class);

    @BeforeTest
    public void setUp() {

        ConfigurationManager kvsStoreConfiguration =
                ConfigurationManager.getInstance(CONFIGURATION);

        KVStore kvs = new MyBatisKVStore(
                kvsStoreConfiguration.getKVStoreConfiguration().getProperties(),
                new SerializationManager());

        store = new DefaultUserDefinedConceptStoreImpl(kvs);
    }

    @Test
    public void test() throws UserDefinedConceptStoreException {
        final String company = "acme";
        final String owner = "mox";
        final String name = "Book";
        final String label = "Book";
        final long userId = 37262L;

        Concept concept = new Concept(company, owner, name, label);
        concept.setVisibility(Concept.Visibility.PUBLIC);

        List<String> keywords = new ArrayList<String>();
        keywords.add("book");
        keywords.add("article");
        keywords.add("publication");

        concept.setKeywords(keywords);

        final String description = "this concept describes a book";
        concept.setDescription(description);

        //store it
        store.storeConcept(userId, concept);

        //get ALL user concepts
        List<Concept> actual = store.getUserConcepts(userId);
        //check
        assertNotNull(actual);
        assertEquals(actual.size(), 1);
        Concept actualConcept = actual.get(0);

        LOGGER.debug("got concept from db: " + actualConcept.toString());
        assertEquals(actualConcept, concept);
        assertEquals(actualConcept.getKeywords(), keywords);

        store.deleteConcept(userId, concept.getURL());
        actual = store.getUserConcepts(userId);
        assertNotNull(actual);
        assertEquals(actual.size(), 0);

        store.storeConcept(userId, concept);
        actual = store.getUserConcepts(userId);
        assertNotNull(actual);
        assertEquals(actual.size(), 1);
        assertEquals(actual.get(0), concept);

        store.deleteAllConcepts(userId);
        actual = store.getUserConcepts(userId);
        assertNull(actual);
    }

}
