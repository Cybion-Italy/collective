package com.collective.resources;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class SearchRecommendationTestCase extends AbstractJerseyRESTTestCase {

    private static final Logger logger = Logger.getLogger(SearchRecommendationTestCase.class);

    public SearchRecommendationTestCase() {
         super(9995);
    }

    @Test(enabled = true)
    public void shouldReceiveErrorMessageSinceNonExistingSearch() throws IOException {
        //use any user id
        String searchId = "53622531";
        int maxItems = AbstractRecommendation.MAX_RECOMMENDATIONS + 1;

        //use it to query the service for webmagazines recommendations
        final String query = "searchrecommendation/"
                + AbstractRecommendation.WEB_MAGAZINES + "/"
                + searchId;
        // Perform GET
        GetMethod getMethod = new GetMethod(base_uri + query);
        HttpClient client = new HttpClient();
        int result = client.executeMethod(getMethod);
        // Check result.
        String responseBody = new String(getMethod.getResponseBody());
        logger.info( String.format("Response body for [%s]: \n%s", base_uri + query, responseBody) );
        logger.info("result code: " + result);
        assert result == HttpStatus.SC_BAD_REQUEST : "Unexpected result: \n" + result;
    }
}
