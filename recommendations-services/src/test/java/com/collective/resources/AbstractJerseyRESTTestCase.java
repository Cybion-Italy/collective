package com.collective.resources;

import com.collective.resources.gson.ConceptSerializer;
import com.collective.resources.jersey.JerseyRESTFrontendConfig;
import com.collective.resources.jersey.JerseyRESTFrontendFactory;
import com.collective.resources.jersey.RESTFrontend;
import com.collective.resources.jersey.RESTFrontendConfig;
import com.collective.resources.gson.JodaDateTimeDeserializer;
import com.collective.resources.gson.JodaDateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public abstract class AbstractJerseyRESTTestCase {

    private static final Logger logger = Logger.getLogger(AbstractJerseyRESTTestCase.class);

    public static final int JERSEY_PORT = 9995;

    //some shared variable to be accessed from extending classes
    protected static final String root_dir = "";
    private static final String base_uri_str = "http://localhost:%d/";
    protected final URI base_uri;
    protected Gson gson;
    private RESTFrontend frontend;

    protected AbstractJerseyRESTTestCase(int port) {
        try {
            base_uri = new URI(String.format(base_uri_str, port));
        } catch (URISyntaxException urise) {
            throw new RuntimeException(urise);
        }
    }

    @BeforeClass
    public void setUp() throws Exception {
        //for the gson deserializer
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new JodaDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(DateTime.class, new JodaDateTimeDeserializer());
        gsonBuilder.registerTypeAdapter(
                new TypeToken<ArrayList<com.collective.concepts.core.Concept>>(){}.getType(),
                new ConceptSerializer());
        this.gson = gsonBuilder.create();

        startFrontendService();
    }

    protected void startFrontendService() {
        RESTFrontendConfig config = new JerseyRESTFrontendConfig(base_uri);
        JerseyRESTFrontendFactory factory = JerseyRESTFrontendFactory.getInstance();

        frontend = factory.create(config);
        logger.info("Starting Grizzly...");
        frontend.startService();
        logger.info(
                String.format("Grizzly started at location %s.\n", base_uri)
        );
    }

    @AfterClass
    public void tearDown() throws InterruptedException {
        frontend.stopService();
        logger.info("Grizzly stopped");
        this.gson = null;
    }
}
