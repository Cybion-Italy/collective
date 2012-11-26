package com.collective.resources.utils;

import com.collective.permanentsearch.model.Search;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class SearchDeserializerTestCase {

    private Gson localGson;
    private static final Logger logger = Logger.getLogger(SearchDeserializerTestCase.class);

    @BeforeClass
    public void initializeGsonSearchResourceTestCase() {


        //init local gson serializer/deserializer
        this.localGson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(Search.class,
                        new SearchDeserializer())
                .create();
    }

    @AfterClass
    public void tearDownGsonSearchResourceTestCase() {
        this.localGson = null;
    }

    @Test
    public void shouldDeserializeJson() {

        String jsonSearch = "{\"id\":30," +
                "\"title\":\"fake title created by user 56426246\"," +
                "\"commonUris\":[{\"label\":\"first common label\"," +
                "\"url\":\"http://firstdomain.com/firstcommonlabel\"}], " +
                "\"customUris\":[{\"label\":\"first custom label\"," +
                "\"url\":\"http://firstdomain.com/firstcustomlabel\"}]}";

        logger.debug("deserializing json: " + jsonSearch);

        Search retrievedSearch =
                    localGson.fromJson(jsonSearch, Search.class);

        logger.debug("deserialized: " + retrievedSearch.toString());

        Assert.assertEquals(retrievedSearch.getId(), 30L);
        Assert.assertEquals(retrievedSearch.getTitle(),
                "fake title created by user 56426246");
        Assert.assertEquals(retrievedSearch.getCommonUris().get(0).getLabel(),
                "first common label");
        Assert.assertEquals(retrievedSearch.getCustomUris().get(0).getLabel(),
                        "first custom label");

    }
}
