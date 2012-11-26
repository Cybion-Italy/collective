package com.collective.model.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class SearchProfileTest {

    private Logger logger = Logger.getLogger(SearchProfileTest.class);


    @Test
    public void gsonSerializationTest() throws MalformedURLException, URISyntaxException {
        //should not yield
        SearchProfile object = getSearchProfile();

        object.setId(1L);
        object.setTitle("first search example");
        object.addConcept(new URI("http://dbpedia.org/resource/Semantic_Web"));
        object.addConcept(new URI("http://dbpedia.org/resource/B-tree"));

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        logger.debug("gson: \n" + gson.toJson(object));
    }

    private SearchProfile getSearchProfile() {
        SearchProfile searchProfile = new SearchProfile();
        return searchProfile;
    }

}
