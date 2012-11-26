package com.collective.resources;

import com.collective.usertests.model.Reason;
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
 */
public class ReasonResourceTestCase extends AbstractJerseyRESTTestCase {

    private static final Logger logger =
            Logger.getLogger(ReasonResourceTestCase.class);

    public ReasonResourceTestCase() {
        super(9995);
    }

    @Test
    public void shouldTestGetAllReasons() throws IOException {

        final String query = "user-feedback-test/reason/list";

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
        List<Reason> reasonList =
                gson.fromJson(responseBody,
                        new TypeToken<List<Reason>>(){}.getType());

        logger.debug(responseBody);

        //check it
        Assert.assertNotNull(reasonList);
    }
}
