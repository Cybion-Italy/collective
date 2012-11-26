package com.collective.resources;

import com.collective.resources.activities.model.UserVisitActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class ActivityServiceTestCase extends AbstractJerseyRESTTestCase
{

    private static final Logger logger = Logger.getLogger(ActivityServiceTestCase.class);

    private Gson localGson;

    public ActivityServiceTestCase()
    {
        super(9995);
    }

    @BeforeClass
    public void initializeGsonSearchResourceTestCase()
    {

        this.localGson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }

    @AfterClass
    public void tearDownGsonSearchResourceTestCase()
    {
        this.localGson = null;

    }

    @Test(enabled = true)
    public void overallCRUDTest() throws IOException
    {

        String userId = "test-user-hf9hu34uf30buq30uq340";

        final String query = "activity/" + userId;

        //POST new user activity
        List<String> topics = new ArrayList<String>();
        topics.add("http://dbpedia.org/resource/United_States");
        topics.add("http://dbpedia.org/resource/Berlin");

        UserVisitActivity userVisitActivity =
                new UserVisitActivity("save", topics, "activity content",
                                      "http://activity.com/rss");

        // Perform POST
        PostMethod postMethod = new PostMethod(base_uri + query);
        postMethod.setRequestHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        String gsonToPost = gson.toJson(userVisitActivity);

        logger.debug("posting json content: " + gsonToPost);

        postMethod.setRequestEntity(
                new StringRequestEntity(gsonToPost, MediaType.APPLICATION_JSON, null));

        HttpClient client = new HttpClient();
        int result = client.executeMethod(postMethod);
        String responseBody = new String(postMethod.getResponseBody());
        assert result == HttpStatus.SC_OK : "Unexpected HTTP status: " + result;

        logger.debug("creation responseBody: " + responseBody);
        String createdActivityId = responseBody;

        result = -1;
        //GET all user activities of last day
        String startTimeMsec = "" + new DateTime().minusDays(1).getMillis();
        String endTimeMsec = "" + new DateTime().getMillis();

        String getAllActivitiesQuery = "activity/" + userId + "/" + startTimeMsec + "/" + endTimeMsec;
        GetMethod getAllActivitiesMethod = new GetMethod(base_uri + getAllActivitiesQuery);
        getAllActivitiesMethod.setRequestHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);

        client = new HttpClient();
        result = client.executeMethod(getAllActivitiesMethod);
        String jsonUserActivities = new String(getAllActivitiesMethod.getResponseBody());
        assert result == HttpStatus.SC_OK : "Unexpected HTTP status: " + jsonUserActivities;
        logger.debug("got activities from server");
        logger.debug(jsonUserActivities);

        //DELETE all the user activities
        String deleteQuery = "activity/" + userId;
        DeleteMethod deleteMethod = new DeleteMethod(base_uri + deleteQuery);
        client = new HttpClient();
        result = client.executeMethod(deleteMethod);
        String deleteResponseBody = new String(deleteMethod.getResponseBody());
        assert result == HttpStatus.SC_OK : "Unexpected HTTP status: " + deleteResponseBody;

        //GET all user activities of last hour, check that it was deleted
        getAllActivitiesMethod = new GetMethod(base_uri + getAllActivitiesQuery);
        getAllActivitiesMethod.setRequestHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
        client = new HttpClient();
        result = client.executeMethod(getAllActivitiesMethod);
        String retrievedJsonUserActivities = new String(getAllActivitiesMethod.getResponseBody());
        assert result == HttpStatus.SC_OK : "Unexpected HTTP status: " + jsonUserActivities;
        logger.debug("should get NO activities from server");
        logger.debug(retrievedJsonUserActivities);

    }
}
