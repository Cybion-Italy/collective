package com.collective.resources.utils;

import com.collective.concepts.core.Concept;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class GsonSingletonSerializationTestCase {

    private Gson simpleGsonSerializer;
    private Gson onlyExposedFieldsGsonSerializer;
    private static final Logger logger = Logger.getLogger(GsonSerializationTestCase.class);

    private Gson excludeFieldsGson;
    private Gson allFieldsGson;

    @BeforeTest
    public void initTwoDeserializers() {

        GsonBuilder exposedFieldsBuilder = new GsonBuilder();
        GsonBuilder plainBuilder = new GsonBuilder();

        this.onlyExposedFieldsGsonSerializer = exposedFieldsBuilder
                               .excludeFieldsWithoutExposeAnnotation().create();
        this.simpleGsonSerializer = plainBuilder.create();

        this.excludeFieldsGson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        this.allFieldsGson = new GsonBuilder().create();
    }

    @AfterTest
    public void tearDown() {
        this.onlyExposedFieldsGsonSerializer = null;
        this.simpleGsonSerializer = null;
    }

    @Test
    public void shouldSerializeWithDifferentGsonObjects() {

        logger.debug("serialization test");
        Concept concept = DomainFixtures.getConcept(45L);

        String onlyExposedJson = onlyExposedFieldsGsonSerializer.toJson(concept);
        String plainJson = simpleGsonSerializer.toJson(concept);
        String excludedGson = excludeFieldsGson.toJson(concept);
        String allFieldsGsonString = allFieldsGson.toJson(concept);

        logger.debug("only exposed: ");
        logger.debug(onlyExposedJson);
        logger.debug("plain: ");
        logger.debug(plainJson);

        logger.debug("exlcudedGson: ");
        logger.debug(excludedGson);
        logger.debug("allFieldsGson: ");
        logger.debug(allFieldsGsonString);

    }
}
