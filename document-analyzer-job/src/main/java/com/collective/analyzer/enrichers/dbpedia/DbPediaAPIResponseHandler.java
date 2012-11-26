package com.collective.analyzer.enrichers.dbpedia;

import com.collective.analyzer.enrichers.dbpedia.model.DBpediaAPIResponse;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class DbPediaAPIResponseHandler implements ResponseHandler<DBpediaAPIResponse>
{

    private Gson gson;

    private static final Logger logger = Logger.getLogger(DbPediaAPIResponseHandler.class);

    public DbPediaAPIResponseHandler(Gson gson)
    {
        this.gson = gson;
    }

    @Override
    public DBpediaAPIResponse handleResponse(HttpResponse response)
            throws ClientProtocolException, IOException
    {
        HttpEntity entity = response.getEntity();

        DBpediaAPIResponse dbpediaApiResponse = null;

        // If the response does not enclose an entity, there is no need
        // to bother about connection release
        if (entity != null) {
            InputStream instream = entity.getContent();
            String json = convertStreamToString(instream);
            logger.debug(json);
            dbpediaApiResponse = gson.fromJson(json, DBpediaAPIResponse.class);

            // Closing the input stream will trigger connection release
            try {
                instream.close();
            } catch (Exception ignore) {
            }
        }
        return dbpediaApiResponse;
    }

    String convertStreamToString(java.io.InputStream is)
    {
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }
}