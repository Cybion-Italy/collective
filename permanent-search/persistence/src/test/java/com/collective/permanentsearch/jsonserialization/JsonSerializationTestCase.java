package com.collective.permanentsearch.jsonserialization;

import com.collective.permanentsearch.model.LabelledURI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class JsonSerializationTestCase {

    private Logger logger = Logger.getLogger(JsonSerializationTestCase.class);

    @BeforeClass
    public void setUp() {
        logger.debug("starting json test");
    }

    @AfterClass
    public void tearDown() {
        logger.debug("ending json test");
    }

    @Test(enabled = true)
    public void testGsonDeserialization() {

        Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .create();


        String jsonConcepts = "[{\"label\":\"fake label\",\"url\":\"http://fakeURI.com\"}]";
        logger.debug("deserializing json object: " + jsonConcepts);

        Type listType = new TypeToken<LinkedList<LabelledURI>>() {}.getType();
        logger.debug("type : " + listType.toString());

        LinkedList<LabelledURI> labelledURIs =  gson.fromJson(jsonConcepts, listType);

        logger.debug("built arrayList: " + labelledURIs.toString());
        logger.debug("built a class: " + labelledURIs.getClass());

    }
}
