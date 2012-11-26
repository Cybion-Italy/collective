package com.collective.resources;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.UserProfile;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 * @author Davide Palmisano ( dpalmisano @ gmail.com )
 */
public class GeneralProjectRecommendationTestCase
        extends AbstractJerseyRESTTestCase {

    private static final Logger logger =
            Logger.getLogger(GeneralProjectRecommendationTestCase.class);

    public GeneralProjectRecommendationTestCase() {
        super(9995);
    }

    @Test
    public void shouldTestExpertsRecommandationsForProjects()
            throws IOException {
        int maxItems = AbstractRecommendation.MAX_RECOMMENDATIONS - 1;
        String projectId = "29";

        final String query = "generalprojectrecommendation/" +
                              AbstractRecommendation.USERS + "/" + projectId +
                "/" + maxItems;
        // Perform GET
        GetMethod getMethod = new GetMethod(base_uri + query);
        HttpClient client = new HttpClient();
        int result = client.executeMethod(getMethod);
        String responseBody = new String(getMethod.getResponseBody());
        logger.info("result code: " + result);
        logger.info("method: " + getMethod.getName() + " at uri: "
                + base_uri + query);
        logger.info("response body: " + responseBody);
        assert result == HttpStatus.SC_OK : "Unexpected result: \n" + result;

        //deserialize responseBody with gson
        List<UserProfile> userProfiles =
                gson.fromJson(responseBody,
                        new TypeToken<List<UserProfile>>(){}.getType());

//        logger.debug(responseBody);

        //check it
        Assert.assertNotNull(userProfiles);
        Assert.assertTrue((userProfiles.size() >= 0) &&
                          (userProfiles.size() <= maxItems));
    }

    @Test
    public void shouldTestRumoursRecommendations() throws IOException {
        int maxItems = 1;
        String projectId = "27";

        final String query = "generalprojectrecommendation/" +
                              AbstractRecommendation.RUMOURS + "/" +
                              projectId + "/" + maxItems;
        // Perform GET
        GetMethod getMethod = new GetMethod(base_uri + query);
        HttpClient client = new HttpClient();
        int result = client.executeMethod(getMethod);
        String responseBody = new String(getMethod.getResponseBody());
        logger.info("result code: " + result);
        logger.info("method: " + getMethod.getName() + " at uri: " +
                    base_uri + query);
        logger.info("response body: " + responseBody);
        assert result == HttpStatus.SC_OK : "Unexpected result: \n" + result;

        //deserialize responseBody with gson
        List<WebResourceEnhanced> webResources =
                gson.fromJson(responseBody,
                        new TypeToken<List<WebResourceEnhanced>>(){}.getType());

        //check it
        Assert.assertNotNull(webResources);
        Assert.assertTrue((webResources.size() >= 0) &&
                          (webResources.size() <= maxItems));

    }
}
