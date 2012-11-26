package com.collective.resources;

import com.collective.resources.userfeedback.CreatedUserFeedbackResponse;
import com.collective.resources.utils.DomainFixtures;
import com.collective.usertests.model.UserFeedback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class UserFeedbackResourceTestCase extends AbstractJerseyRESTTestCase {

    private static final Logger logger =
            Logger.getLogger(UserFeedbackResourceTestCase.class);

    private Gson gson;
    private HttpClient httpClient;

    public UserFeedbackResourceTestCase() {
        super(9995);
    }

    @BeforeClass
    public void setUpUserFeedbackResourceTestCase() {
        //init gson serializer/deserializer
        this.gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
        this.httpClient = new HttpClient();
    }

    @AfterClass
    public void tearDownUserFeedbackResourceTestCase() {
        this.gson = null;
        this.httpClient = null;
    }

    @Test (enabled = true)
    public void shouldTestCRDUserFeedback() throws Exception {

        final String query = "user-feedback-test/userfeedback";
        String createdUserFeedbackId = "";

        try {
            //POST new user feedback
            //build fake userFeedback
            UserFeedback userFeedback = DomainFixtures.getUserFeedback(56426246L);

            //jsonize it
            String jsonUserFeedback = gson.toJson(userFeedback);

            // Perform POST
            PostMethod postMethod = new PostMethod(base_uri + query);
            postMethod.setRequestHeader("Content-Type",
                                        MediaType.APPLICATION_JSON);
            postMethod.setRequestEntity(
                    new StringRequestEntity(jsonUserFeedback,
                                            MediaType.APPLICATION_JSON, null));

            //execute
            int result = httpClient.executeMethod(postMethod);

            //get and parse response
            String postResponseBody = new String(postMethod.getResponseBody());
            logger.info("POST result code: " + result);
            logger.info("method: " + postMethod.getName() + " at uri: " +
                    base_uri + query);
            logger.info("response body: " + postResponseBody);
            assert result == HttpStatus.SC_OK : "Unexpected result: \n"
                    + result;

            //deserialize postResponseBody with gson
            CreatedUserFeedbackResponse createdUserFeedbackJson =
                    gson.fromJson(postResponseBody,
                            CreatedUserFeedbackResponse.class);

            Assert.assertNotNull(createdUserFeedbackJson.getUserFeedbackId());

            //save id to use it in DELETE
            createdUserFeedbackId =
                    createdUserFeedbackJson.getUserFeedbackId().toString();

            //GET
            // Perform GET to see if reasource is saved
            String getQuery = "user-feedback-test/userfeedback"
                               + "/" + createdUserFeedbackId;

            GetMethod getMethod = new GetMethod(base_uri + getQuery);

            int getResult = httpClient.executeMethod(getMethod);
            String getResponseBody = new String(getMethod.getResponseBody());
            logger.info("result code: " + result);
            logger.info("method: " + getMethod.getName() + " at uri: " +
                    base_uri + query);
            logger.info("GET response body: " + getResponseBody);
            assert result == HttpStatus.SC_OK : "Unexpected result: \n" + result;

            //deserialize
            UserFeedback gotUserFeedback = gson.fromJson(getResponseBody,
                                                         UserFeedback.class);

            //assert i got what i wanted
            Assert.assertEquals(gotUserFeedback.getProjectId(), userFeedback.getProjectId());
            Assert.assertEquals(gotUserFeedback.getReasonId(), userFeedback.getReasonId());
            Assert.assertEquals(gotUserFeedback.getUrlResource(), userFeedback.getUrlResource());
            Assert.assertTrue(gotUserFeedback.getId().equals(Long.parseLong(createdUserFeedbackId)));

        } catch (Exception e) {
            logger.error("error while saving userFeedback resource: " + e.getMessage());
            throw new Exception(e);
        } finally {
            //use the id of created UserFeedback
            String deleteQuery = query + "/" + createdUserFeedbackId;

            // DELETE
            DeleteMethod deleteMethod = new DeleteMethod(base_uri + deleteQuery);

            logger.debug("delete query path: " + base_uri + deleteQuery);

            //execute
            int result = 0;
            try {
                result = httpClient.executeMethod(deleteMethod);
            } catch (IOException e) {
                logger.error("error in tests while deleting userFeedback resource: " + e.getMessage());
                throw new Exception(e);
            }

            //get and parse response
//            String responseBody = new String(deleteMethod.getResponseBody());
            logger.info("result code: " + result);
            logger.info("method: " + deleteMethod.getName() + " at uri: " +
                    base_uri + query);
//            logger.info("response body: " + responseBody);
            assert result == HttpStatus.SC_NO_CONTENT : "Unexpected result: \n"
                    + result;
        }
    }
}
