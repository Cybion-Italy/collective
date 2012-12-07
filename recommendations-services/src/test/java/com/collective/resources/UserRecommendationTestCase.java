package com.collective.resources;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.ProjectProfile;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class UserRecommendationTestCase extends AbstractJerseyRESTTestCase {

    private static final Logger logger = Logger.getLogger(UserRecommendationTestCase.class);

    public UserRecommendationTestCase() {
        super(9995);
    }

    @Test
    public void shouldGetSuitableProjectsForExistingUser() throws IOException {
        int userId = 21;
        int maxItems = 2;
        final String query = "userrecommendation/"
                + AbstractRecommendation.PROJECTS + "/"
                + userId + "/" + maxItems;

        // Perform GET
        GetMethod getMethod = new GetMethod(base_uri + query);
        HttpClient client = new HttpClient();
        int result = client.executeMethod(getMethod);
        String responseBody = new String(getMethod.getResponseBody());
        logger.info("result code: " + result);
        logger.info("method: " + getMethod.getName() + " at uri: " + base_uri + query);
        logger.info("response body: " + responseBody);
        assert result == HttpStatus.SC_OK : "Unexpected result: \n" + result;

        //deserialize responseBody with gson
        List<ProjectProfile> projectProfiles =
                gson.fromJson(responseBody, new TypeToken<List<ProjectProfile>>(){}.getType());

//        logger.debug("projectProfile: " + responseBody);

        //check it
        assertNotNull(projectProfiles);
        assertTrue((projectProfiles.size() >= 0) &&
                          (projectProfiles.size() <= maxItems));
    }

    @Test
    public void shouldGetWebMagazinesRecommendationsForExistingUser() throws IOException {
        //TODO get it from the virtuoso graph index
        //get an existing userId from graph index
        int userId = 22;
        int maxItems = 2;

        //use it to query the service for webmagazines recommendations
        final String query = "userrecommendation/"
                + AbstractRecommendation.WEB_MAGAZINES + "/"
                + userId + "/" + maxItems;
        // Perform GET
        GetMethod getMethod = new GetMethod(base_uri + query);
        HttpClient client = new HttpClient();
        int result = client.executeMethod(getMethod);
        String responseBody = new String(getMethod.getResponseBody());
        logger.info("result code: " + result);
        logger.info("method: " + getMethod.getName() + " at uri: " + base_uri + query);
        logger.info("response body: " + responseBody);
        assert result == HttpStatus.SC_OK : "Unexpected result: \n" + result;

        //deserialize responseBody with gson
        List<WebResourceEnhanced> webResourceEnhancedList =
                gson.fromJson(responseBody, new TypeToken<List<WebResourceEnhanced>>(){}.getType());

        //check it
        assertNotNull(webResourceEnhancedList);
        assertTrue((webResourceEnhancedList.size() >= 0) &&
                          (webResourceEnhancedList.size() <= maxItems));
    }


    @Test
    public void shouldThrowExceptionForTooManyRecommendations() throws IOException {
        //use any user id
        int userId = 22531;
        int maxItems = AbstractRecommendation.MAX_RECOMMENDATIONS + 1;

        //use it to query the service for webmagazines recommendations
        final String query = "userrecommendation/"
                + AbstractRecommendation.WEB_MAGAZINES + "/"
                + userId + "/" + maxItems;
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

    @Test
    public void shouldGetShortTermProfileRecommendations() throws IOException {
        int userId = 105;
        int maxItems = 3;

        //use it to query the service for webmagazines recommendations
        final String query = "userrecommendation/"
                + AbstractRecommendation.SHORT_TERM_PROFILE + "/"
                + userId + "/" + maxItems;
        // Perform GET
        GetMethod getMethod = new GetMethod(base_uri + query);
        HttpClient client = new HttpClient();
        int result = client.executeMethod(getMethod);
        String responseBody = new String(getMethod.getResponseBody());
        logger.info("result code: " + result);
        logger.info("method: " + getMethod.getName() + " at uri: " + base_uri + query);
        logger.info("response body: " + responseBody);
        assert result == HttpStatus.SC_OK : "Unexpected result: \n" + result;

        //deserialize responseBody with gson
        List<WebResourceEnhanced> webResourceEnhancedList =
                gson.fromJson(responseBody, new TypeToken<List<WebResourceEnhanced>>(){}.getType());

        //check it
        assertNotNull(webResourceEnhancedList);
        assertTrue((webResourceEnhancedList.size() >= 0) &&
                (webResourceEnhancedList.size() <= maxItems));
    }
}
