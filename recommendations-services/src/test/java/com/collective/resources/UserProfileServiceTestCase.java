package com.collective.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class UserProfileServiceTestCase extends AbstractJerseyRESTTestCase
{
    private Gson localGson;

    private static final Logger logger = Logger.getLogger(UserProfileServiceTestCase.class);

    public UserProfileServiceTestCase()
    {
        super(JERSEY_PORT);
    }

    @BeforeClass
    public void initializeGsonSearchResourceTestCase()
    {
        //init local gson serializer/deserializer
        this.localGson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @AfterClass
    public void tearDownGsonSearchResourceTestCase()
    {
        this.localGson = null;
    }

    @Test
    public void shouldGetUserShortTermProfile() throws IOException
    {
        //IT WORKS FOR ANY USERID
        String userId = UUID.randomUUID().toString();

        final String query = "user-profile/short-term/" + userId;

        // Perform GET
        GetMethod getMethod = new GetMethod(base_uri + query);

        HttpClient client = new HttpClient();
        int result = client.executeMethod(getMethod);
        String responseBody = new String(getMethod.getResponseBody());
        logger.info("result code: " + result);
        logger.info("method: " + getMethod.getName() + " at uri: " + base_uri + query);
        logger.info("response body: " + responseBody);
        assert result == HttpStatus.SC_OK : "Unexpected result: \n" + result;
        //TODO add something to evaluate deserialization (after being sure that data is present
        // for a userId)
    }
}
