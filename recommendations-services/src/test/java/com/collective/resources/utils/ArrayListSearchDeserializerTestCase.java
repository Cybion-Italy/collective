package com.collective.resources.utils;

import com.collective.permanentsearch.model.Search;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class ArrayListSearchDeserializerTestCase {

    private static final String searchJsonArray = "" +
            "[\n" +
            "   {\n" +
            "      \"id\":143,\n" +
            "      \"title\":\"fake title created by user 56426246\",\n" +
            "      \"commonUris\":[\n" +
            "         {\n" +
            "            \"label\":\"common label\",\n" +
            "            \"url\":\"http://firstdomain.com/commonlabel\"\n" +
            "         }\n" +
            "      ],\n" +
            "      \"customUris\":[\n" +
            "         {\n" +
            "            \"label\":\"custom label\",\n" +
            "            \"url\":\"http://firstdomain.com/customlabel\"\n" +
            "         }\n" +
            "      ]\n" +
            "   },\n" +
            "   {\n" +
            "      \"id\":144,\n" +
            "      \"title\":\"fake title created by user 56426246\",\n" +
            "      \"commonUris\":[\n" +
            "         {\n" +
            "            \"label\":\"common label\",\n" +
            "            \"url\":\"http://firstdomain.com/commonlabel\"\n" +
            "         }\n" +
            "      ],\n" +
            "      \"customUris\":[\n" +
            "         {\n" +
            "            \"label\":\"custom label\",\n" +
            "            \"url\":\"http://firstdomain.com/customlabel\"\n" +
            "         }\n" +
            "      ]\n" +
            "   }\n" +
            "]";


    private static final Logger logger =
            Logger.getLogger(ArrayListSearchDeserializerTestCase.class);

    private Type arrayListSearchType;

    private Gson gson;


    @BeforeTest
    public void setUp() {

        arrayListSearchType = new TypeToken<ArrayList<Search>>() {}.getType();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(arrayListSearchType,
                                        new ArrayListSearchDeserializer())
                .registerTypeAdapter(Search.class,
                        new SearchDeserializer())
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @AfterTest
    public void tearDown() {
        this.arrayListSearchType = null;
        this.gson = null;
    }

    @Test
    public void shouldDeserializeJson() {

        ArrayList<Search> userSearchesLinked =
                    gson.fromJson(searchJsonArray, arrayListSearchType);

        for (Search search : userSearchesLinked) {
            logger.debug("deserialized json: ");
            logger.debug(search.toString());
        }

        Search firstSearch  = userSearchesLinked.get(0);
        Search secondSearch = userSearchesLinked.get(1);

        Assert.assertEquals(firstSearch.getId(),  Long.parseLong("143"));
        Assert.assertEquals(secondSearch.getId(), Long.parseLong("144"));
        Assert.assertEquals(firstSearch.getCommonUris().get(0).getUrl().toString(),
                            "http://firstdomain.com/commonlabel");
    }
}
