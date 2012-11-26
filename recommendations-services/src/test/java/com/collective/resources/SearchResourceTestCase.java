package com.collective.resources;

import com.collective.permanentsearch.model.Search;
import com.collective.resources.utils.ArrayListSearchDeserializer;
import com.collective.resources.search.model.SearchCreationBean;
import com.collective.resources.utils.DomainFixtures;
import com.collective.resources.utils.SearchDeserializer;
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
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class SearchResourceTestCase extends AbstractJerseyRESTTestCase {

    private static final Logger logger =
            Logger.getLogger(SearchResourceTestCase.class);

    private HttpClient httpClient;

    private Gson localGson;

    private Type listType;

    public SearchResourceTestCase() {
        super(9995);
    }

    @BeforeClass
    public void initializeGsonSearchResourceTestCase() {
        listType = new TypeToken<ArrayList<Search>>() {}.getType();

        //init local gson serializer/deserializer
        this.localGson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(listType,
                        new ArrayListSearchDeserializer())
                .registerTypeAdapter(Search.class,
                        new SearchDeserializer())
                .create();
        this.httpClient = new HttpClient();
    }

    @AfterClass
    public void tearDownGsonSearchResourceTestCase() {
        this.localGson = null;
        this.httpClient = null;
    }

    @Test
    public void overallTest() throws Exception {

        //create search
        String userSearch = "user/search";
        String search = "search";
        String userSearches = "user/searches";
        String createdSearchResource = "";
        Long userId = 56426246L;

        try {
            //POST new user search
            SearchCreationBean searchCreationBean =
                    DomainFixtures.getSearchCreationBean(userId);

            //jsonize it
            String searchCreationBeanJson = gson.toJson(searchCreationBean);

            logger.debug("created json searchCreationBean: \n"
                    + searchCreationBeanJson);

            logger.debug("posting it to uri: " + base_uri + userSearch);

            // Perform POST
            PostMethod postMethod = new PostMethod(base_uri + userSearch);
            postMethod.setRequestHeader("Content-Type",
                    MediaType.APPLICATION_JSON);
            postMethod.setRequestEntity(
                    new StringRequestEntity(searchCreationBeanJson,
                            MediaType.APPLICATION_JSON, null));

            //execute
            int result = -1;

            result = httpClient.executeMethod(postMethod);

            //get and parse response

            String postResponseBody = new String(postMethod.getResponseBody());
            logger.info("POST result code: " + result);
            logger.info("method: " + postMethod.getName() + " at uri: " +
                    base_uri + userSearch);
            logger.info("response body: " + postResponseBody);
            assert result == HttpStatus.SC_OK : "Unexpected result: \n"
                    + result;

            Result resultContent = gson.fromJson(postResponseBody, Result.class);

            Assert.assertNotNull(resultContent);
            Assert.assertTrue(resultContent.getStatus().equals(Result.Status.OK));


            //GET all the searches of the user

            String allUserSearchesPath = base_uri + userSearches
                    + "/" + userId.toString();

            GetMethod getAllUserSearches = new GetMethod(allUserSearchesPath);

            logger.debug("getting all user searches from : "
                    + allUserSearchesPath);

            //execute
            int getResult = -1;

            getResult = httpClient.executeMethod(getAllUserSearches);

            //get and parse response
            String getResponseBody =
                    new String(getAllUserSearches.getResponseBody());
            logger.debug("GET result code: " + getResult);
            logger.debug("method: " + getAllUserSearches.getName() + " at uri: "
                    + allUserSearchesPath);
            logger.debug("GET response body: " + getResponseBody);
            assert getResult == HttpStatus.SC_OK : "Unexpected result: \n"
                    + getResult;

            logger.debug("deserializer built for: " + listType.toString());

            ArrayList<Search> retrievedUserSearches =
                    localGson.fromJson(getResponseBody, listType);

            logger.debug("retrievedUserSearches: "
                    + retrievedUserSearches.toString());

            //parse response content
            ArrayList<Search> userSearchesArray = new ArrayList<Search>();
            userSearchesArray.addAll(retrievedUserSearches);

            Assert.assertNotNull(userSearchesArray);
            logger.debug("userSearchesArray:  " + userSearchesArray.toString());
            logger.debug("searchCreationBean: " + searchCreationBean.toString());

            Assert.assertEquals(userSearchesArray.size(), 1);

            Assert.assertEquals(userSearchesArray.get(0).getTitle(),
                    searchCreationBean.getTitle());
            Assert.assertEquals(userSearchesArray.get(0).getCommonUris(),
                    searchCreationBean.getCommonConcepts());
            Assert.assertEquals(userSearchesArray.get(0).getCustomUris(),
                    searchCreationBean.getCustomConcepts());

            //get id of created resource
            createdSearchResource = ""
                    + Long.toString(userSearchesArray.get(0).getId());
            logger.debug("RETRIEVED userSearch: "
                    + userSearchesArray.get(0).getId());

            //GET the single saved user search

            GetMethod getSearch = new GetMethod(base_uri + search + "/"
                    + createdSearchResource);
            logger.debug("getting single search from : "
                    + getSearch);

            //execute
            int getSingleResult = -1;

            getSingleResult = httpClient.executeMethod(getSearch);

            //get and parse response
            String getSingleResponseBody =
                    new String(getSearch.getResponseBody());
            logger.debug("GET result code: " + getSingleResult);
            logger.debug("method: " + getSearch.getName() + " at uri: "
                    + base_uri + "/" + search + "/" + createdSearchResource);
            logger.debug("GET response body: " + getSingleResponseBody);
            assert getSingleResult == HttpStatus.SC_OK : "Unexpected result: \n"
                    + getSingleResult;

            Search retrievedSearch =
                    localGson.fromJson(getSingleResponseBody, Search.class);

            Assert.assertNotNull(retrievedSearch);
            logger.debug("retrievedSearch: " + retrievedSearch.toString());

            Assert.assertEquals(retrievedSearch.getTitle(),
                    searchCreationBean.getTitle());
            Assert.assertEquals(retrievedSearch.getCommonUris(),
                    searchCreationBean.getCommonConcepts());
            Assert.assertEquals(retrievedSearch.getCustomUris(),
                    searchCreationBean.getCustomConcepts());

        } catch (Exception e) {
            logger.error("error while deserialing " + e.getMessage());
        } finally {

            String deleteQuery = search + "/" + createdSearchResource;

            // DELETE
            DeleteMethod deleteMethod = new DeleteMethod(base_uri + deleteQuery);

            logger.debug("DELETE userSearch path: " + base_uri + deleteQuery);

            //execute
            int result = -1;

            try {
                result = httpClient.executeMethod(deleteMethod);
            } catch (IOException e) {
                logger.error("error in tests while deleting userFeedback " +
                        "resource: " + e.getMessage());
                throw new Exception(e);
            }

            //get and parse response
            String responseBody = new String(deleteMethod.getResponseBody());
            logger.info(" DELETE result code: " + result);
            logger.info("method: " + deleteMethod.getName() + " at uri: " +
                    base_uri + deleteQuery);
            logger.info("response body: " + responseBody);
            assert result == HttpStatus.SC_OK : "Unexpected result: \n"
                    + result + " " + responseBody;
            logger.debug("ended test");
        }
    }
}
