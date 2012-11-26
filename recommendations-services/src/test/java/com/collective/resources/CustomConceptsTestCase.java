package com.collective.resources;

import com.collective.concepts.core.Concept;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class CustomConceptsTestCase extends AbstractJerseyRESTTestCase {

    private static final Logger logger = Logger.getLogger(CustomConceptsTestCase.class);

    public CustomConceptsTestCase() {
        super(9995);
    }

    //TODO (med) test depend on database status: change
    @Test (enabled = true)
    public void overallTest() throws IOException {

        final String company = "acme";
        String owner = "mox";
        final String name = "Book";
        final String label = "Book";

        final String userId = "374263";
        final String description = "just a test concept representing a Book";
        List<String> kws = new ArrayList<String>();
        kws.add("book");
        kws.add("publication");
        kws.add("article");

        String query = "concepts/" + company + "/" + owner + "/"
                                   + name + "/" + label;

        String keywords = convertListToString(kws);

        PostMethod postMethod = new PostMethod(base_uri + query);
        postMethod.setParameter("user", userId);
        postMethod.setParameter("description", description);
        postMethod.setParameter("keywords", keywords);

        HttpClient client = new HttpClient();
        int result = client.executeMethod(postMethod);
        String responseBody = new String(postMethod.getResponseBody());
        assert result == HttpStatus.SC_OK : "Unexpected HTTP status: " + result;

        Result resultObj = gson.fromJson(responseBody, Result.class);
        Assert.assertNotNull(resultObj);
        Assert.assertEquals(resultObj.getStatus(), Result.Status.OK);

        query = "concepts/" + userId;
        logger.debug("path of get query: " + base_uri + query);
        GetMethod getMethod = new GetMethod(base_uri + query);
        client.executeMethod(getMethod);
        responseBody = new String(getMethod.getResponseBody());
        logger.debug("response Body: " + responseBody);
        assert result == HttpStatus.SC_OK : "Unexpected HTTP status: " + result;

        List<Concept> actual = gson.fromJson(
                responseBody,
                new TypeToken<ArrayList<Concept>>(){}.getType()
        );

        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.size(), 1);
        Assert.assertEquals(actual.get(0).getURL(),
                new URL("http://collective.com/concepts/"
                                          + company +"/" + owner + "/" + name));

        Concept actualConcept = actual.get(0);
        logger.debug("actual concepts from service: "
                + actualConcept.toString());

        String actualKeywordsString =
                convertListToString(actualConcept.getKeywords());

        Assert.assertEquals(actualConcept.getCompany(), company);
        Assert.assertEquals(actualConcept.getName(), name);
        Assert.assertEquals(actualKeywordsString, keywords);
        Assert.assertEquals(actualConcept.getLabel(), label);
        Assert.assertEquals(actualConcept.getDescription(), description);

        query = "concepts/" + userId;
        DeleteMethod deleteMethod = new DeleteMethod(base_uri + query);
        client.executeMethod(deleteMethod);
        responseBody = new String(deleteMethod.getResponseBody());
        assert result == HttpStatus.SC_OK : "Unexpected HTTP status: " + result;

        resultObj = gson.fromJson(responseBody, Result.class);
        Assert.assertNotNull(resultObj);
        Assert.assertEquals(resultObj.getStatus(), Result.Status.OK);


        //check that no concepts are left
        query = "concepts/" + userId;
        logger.debug("path of get query: " + base_uri + query);
        getMethod = new GetMethod(base_uri + query);
        client.executeMethod(getMethod);
        responseBody = new String(getMethod.getResponseBody());
        logger.debug("response body: " + responseBody);
        assert result == HttpStatus.SC_OK : "Unexpected HTTP status: " + result;

        List<Concept> emptyList = gson.fromJson(
                responseBody,
                new TypeToken<ArrayList<Concept>>(){}.getType()
        );

        Assert.assertTrue(emptyList.size() == 0);
    }

    private static String convertListToString(List<String> keywords) {
        String keywordsString = "";
        for (String keyword : keywords) {
            keywordsString += keyword + ",";
        }
        keywordsString = keywordsString.substring(0, keywordsString.length() - 1);
        return keywordsString;
    }


}
