package com.collective.analyzer.enrichers.dbpedia;

import com.collective.analyzer.enrichers.EnrichmentService;
import com.collective.analyzer.enrichers.EnrichmentServiceException;
import com.collective.analyzer.enrichers.dbpedia.adapters.DBpediaAPIResponseAdapter;
import com.collective.analyzer.enrichers.dbpedia.adapters.DBpediaResourceAdapter;
import com.collective.analyzer.enrichers.dbpedia.model.DBpediaAPIResponse;
import com.collective.analyzer.enrichers.dbpedia.model.DBpediaResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class DBPediaAPI implements EnrichmentService
{

    private static final String endpoint = "http://spotlight.dbpedia.org/rest/annotate";

    private static final Logger logger = Logger.getLogger(DBPediaAPI.class);

    private static final String CONFIDENCE_VALUE = "0.5";

    private static final String SUPPORT_VALUE = "100";

    private Gson gson;

    public DBPediaAPI()
    {
//        logger.debug("creating dbpedia api");
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(DBpediaResource.class, new DBpediaResourceAdapter())
                .registerTypeAdapter(DBpediaAPIResponse.class, new DBpediaAPIResponseAdapter())
                .excludeFieldsWithoutExposeAnnotation();

        gson = gsonBuilder.create();

//        logger.debug("dbpedia api created");
    }

    @Override
    public List<URI> getConceptsURIs(String textToAnalyze)
            throws EnrichmentServiceException
    {
        DBpediaAPIResponse dbPediaApiResponse = this.getNamedEntities(textToAnalyze);

        List<URI> conceptsURIs = new ArrayList<URI>();

        for (DBpediaResource dBpediaResource : dbPediaApiResponse.getResources()) {
            conceptsURIs.add(dBpediaResource.getUri());
        }
        return conceptsURIs;
    }

    private DBpediaAPIResponse getNamedEntities(String textToAnalyze)
            throws DBpediaAPIException
    {

        logger.debug("text to analyse: " + textToAnalyze);

        String encodedText = "";

        try {
            encodedText = URLEncoder.encode(textToAnalyze, "UTF8");
        } catch (UnsupportedEncodingException e) {
            String emsg = "failed to encode content";
            logger.error(emsg);
            throw new DBpediaAPIException(emsg, e);
        }

        HttpPost postMethod = new HttpPost(endpoint);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("disambiguator", "Document"));
        nvps.add(new BasicNameValuePair("confidence", CONFIDENCE_VALUE));
        nvps.add(new BasicNameValuePair("support", SUPPORT_VALUE));
        nvps.add(new BasicNameValuePair("text", encodedText));

        postMethod.setHeader("Accept", "application/json");
        postMethod.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");


//        postMethod.setParams(params); setParameter("disambiguator", "Document");
//        postMethod.setParameter("confidence", CONFIDENCE_VALUE);
//        postMethod.setParameter("support", SUPPORT_VALUE);
//        postMethod.setParameter("text", encodedText);
//        postMethod.setRequestHeader("Accept", "application/json");
//        postMethod.setRequestHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");


        try {
            postMethod.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            throw new DBpediaAPIException("encoding not supported for " + nvps.toString(), e);
        }

        HttpClient httpClient = new DefaultHttpClient();

        DBpediaAPIResponse response = null;
        ResponseHandler<DBpediaAPIResponse> responseHandler = new DbPediaAPIResponseHandler(gson);
        try {

            response = httpClient.execute(postMethod, responseHandler);
        } catch (IOException e) {
            throw new DBpediaAPIException("failed when executing POST to dbpedia api", e);
        }
//        HttpEntity entity = response.getEntity();

//        PostMethod postMethod = new PostMethod(endpoint);
//        postMethod.setParams(params); setParameter("disambiguator", "Document");
//        postMethod.setParameter("confidence", CONFIDENCE_VALUE);
//        postMethod.setParameter("support", SUPPORT_VALUE);
//        postMethod.setParameter("text", encodedText);
//        postMethod.setRequestHeader("Accept", "application/json");
//        postMethod.setRequestHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
//

//        DBpediaAPIResponse dBpediaAPIResponse = null;

//        int result = 0;
//        try {
//            result = client.executeMethod(postMethod);
//            String responseBody = new String(postMethod.getResponseBody());
//            logger.debug(postMethod.getStatusCode());
//            logger.debug(responseBody);
//            dBpediaAPIResponse = gson.fromJson(responseBody, DBpediaAPIResponse.class);
//        } catch (IOException e) {
//            String emsg = "failed to execute POST - code: " + result;
//            throw new DBpediaAPIException(emsg, e);
//        }
        return response;
    }

}
