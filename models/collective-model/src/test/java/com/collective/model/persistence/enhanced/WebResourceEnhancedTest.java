package com.collective.model.persistence.enhanced;

import com.collective.model.persistence.Source;
import com.collective.model.persistence.WebResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class WebResourceEnhancedTest {

    private Logger logger = Logger.getLogger(WebResourceEnhancedTest.class);

    @Test
    public void gsonSerializationTest() throws MalformedURLException, URISyntaxException {
        //should not yield
        WebResourceEnhanced object = getWebResourceEnhanced();

        object.setId(1);
        object.setTitolo("title");
        object.setDescrizione("description");
        object.addTopic(new URI("http://dbpedia.org/resource/Concept"));
        object.addTopic(new URI("http://dbpedia.org/resource/Concept2"));
        object.setUrl(new URL("http://www.url.com"));
        object.setSourceRssEnhanced(getSourceRssEnhanced());

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        logger.debug("gson: \n" + gson.toJson(object));
    }

    private WebResourceEnhanced getWebResourceEnhanced() throws MalformedURLException {
        WebResourceEnhanced object = new WebResourceEnhanced();
        object.setId(1);
        object.setTitolo("title");
        object.setDescrizione("description");
        object.setSourceRssEnhanced(getSourceRssEnhanced());
        return object;
    }

    private SourceRssEnhanced getSourceRssEnhanced() throws MalformedURLException {
        SourceRssEnhanced source = new SourceRssEnhanced();
        source.setId(1);
        source.setUrl(new URL("http://www.location.com"));
        source.setSource(getSource());
        return source;
    }

    private Source getSource() throws MalformedURLException {
        Source source = new Source();
        source.setId(3);
        source.setNome("source");
        source.setUrl(new URL("http://www.url.com"));
        return source;
    }


}
