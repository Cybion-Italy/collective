package com.collective.resources;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class EnricherTestCase extends AbstractJerseyRESTTestCase
{
    private static final Logger logger = Logger.getLogger(EnricherTestCase.class);

    public EnricherTestCase()
    {
        super(9995);
    }

    @Test
    public void shouldTestUrl() throws IOException
    {
        String urlEncoded = URLEncoder.encode("http://musicdatascience.com/", "UTF-8");
        final String query = "enricher/url/";

        final String jsonUrl = "{\"url\":\"" + urlEncoded + "\"}";

        // Perform POST
        PostMethod postMethod = new PostMethod(base_uri + query);
        postMethod.setRequestHeader("Content-Type",
                                    MediaType.APPLICATION_JSON);
        postMethod.setRequestEntity(
                new StringRequestEntity(jsonUrl,
                                        MediaType.APPLICATION_JSON,
                                        "UTF-8"));

        HttpClient client = new HttpClient();

        int result = client.executeMethod(postMethod);
        String responseBody = new String(postMethod.getResponseBody());
        logger.info("result code: " + result);
        logger.info("method: " + postMethod.getName() + " at uri: " +
                            base_uri + query);
        logger.info("response body: " + responseBody);
        assert result == HttpStatus.SC_OK : "Unexpected result: \n" + result;

        //deserialize responseBody with gson
        List<URI> concepts =
                gson.fromJson(responseBody, new TypeToken<List<URI>>(){}.getType());

        logger.debug(responseBody);

        //no asserts since it depends on dbpedia
        for (URI concept : concepts) {
            logger.debug("found concept: " + concept.toString());
        }
    }

    @Test
    public void shouldTestText() throws IOException
    {
        final String query = "enricher/text/";

        //jsonize it
        String textContent = "President Obama on Monday will call for a new " +
                "minimum tax rate for individuals making more than $1 million " +
                "a year to ensure that they pay at least the same percentage of " +
                "their earnings as other taxpayers, according to administration" +
                " officials.";
        String jsonText = "{ \"text\":\""+ textContent +"\" }";

        // Perform POST
        PostMethod postMethod = new PostMethod(base_uri + query);
        postMethod.setRequestHeader("Content-Type",
                                    MediaType.APPLICATION_JSON);
        postMethod.setRequestEntity(
                new StringRequestEntity(jsonText,
                                        MediaType.APPLICATION_JSON,
                                        "UTF-8"));

        HttpClient client = new HttpClient();
        int result = client.executeMethod(postMethod);
        String responseBody = new String(postMethod.getResponseBody());
        logger.info("result code: " + result);
        logger.info("method: " + postMethod.getName() + " at uri: " +
                            base_uri + query);
        logger.info("response body: " + responseBody);
        assert result == HttpStatus.SC_OK : "Unexpected result: \n" + result;

        //deserialize responseBody with gson
        List<URI> concepts =
                gson.fromJson(responseBody, new TypeToken<List<URI>>(){}.getType());

        logger.debug(responseBody);

        //no asserts since it depends on dbpedia
        for (URI concept : concepts) {
            logger.debug("found concept: " + concept.toString());
        }
    }
}
