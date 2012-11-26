package com.collective.concepts.core.lookup.lucene;

import com.collective.concepts.core.Concept;
import com.collective.concepts.core.DefaultUserDefinedConceptStoreImpl;
import com.collective.concepts.core.UserDefinedConceptStore;
import com.collective.concepts.core.UserDefinedConceptStoreException;
import com.collective.concepts.core.lookup.UserDefinedConceptLookupEngine;
import com.collective.concepts.core.lookup.UserDefinedConceptLookupEngineException;
import com.collective.concepts.core.lookup.result.InMemoryResultListener;
import com.collective.concepts.core.lookup.result.ResultListener;
import com.collective.concepts.core.lookup.result.ResultListnerException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.configuration.ConfigurationManager;
import tv.notube.commons.storage.kvs.mybatis.MyBatisKVStore;
import tv.notube.commons.storage.model.fields.serialization.SerializationManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LuceneLookupEngineTestCase
{

    private static String CONFIGURATION = "kvs-configuration.xml";

    private static final String file = "src/test/resources/test.txt";

    private UserDefinedConceptStore conceptStore;

    private long userId = 746736452L;

    protected UserDefinedConceptLookupEngine engine;

    @BeforeTest
    public void setUp() throws UserDefinedConceptStoreException
    {
        conceptStore = initConceptStore();
        conceptStore.storeConcept(userId, getConcept());
        engine = new LuceneLookupEngine(conceptStore, 50);
    }

    @Test
    public void testLookUp() throws IOException,
            UserDefinedConceptLookupEngineException, ResultListnerException, UserDefinedConceptStoreException
    {
        String text = getText(file);
        ResultListener resultListener = new InMemoryResultListener();
        try {
            engine.lookup(text, userId, resultListener);
        } finally {
            conceptStore.deleteAllConcepts(userId);
        }
        Assert.assertTrue(resultListener.getConcepts().size() > 0);
    }

    private String getText(String file) throws IOException
    {
        InputStream is = new FileInputStream(file);
        Reader reader = new InputStreamReader(is);
        BufferedReader buf = new BufferedReader(reader);
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        String line;
        while ((line = buf.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        return stringBuilder.toString();
    }

    private UserDefinedConceptStore initConceptStore()
    {
        ConfigurationManager kvsStoreConfiguration =
                ConfigurationManager.getInstance(CONFIGURATION);

        KVStore kvs = new MyBatisKVStore(
                kvsStoreConfiguration.getKVStoreConfiguration().getProperties(),
                new SerializationManager());

        return new DefaultUserDefinedConceptStoreImpl(kvs);
    }

    public Concept getConcept()
    {
        Concept concept = new Concept("cybion", "mmoci", "broadband", "broadband");
        concept.setVisibility(Concept.Visibility.PUBLIC);
        concept.addKeyword("mobile broadband");
        concept.addKeyword("broadband communications");
        concept.addKeyword("broadband");
        concept.setDescription("this concept describes a broadband communication");
        return concept;
    }
}
