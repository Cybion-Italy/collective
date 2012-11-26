package com.collective.analyzer.enrichers.dbpedia.adapters;

import com.collective.analyzer.enrichers.dbpedia.model.DBpediaResource;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.net.URISyntaxException;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class DBpediaResourceAdapter implements JsonDeserializer<DBpediaResource>
{

    private static final Logger logger = Logger.getLogger(DBpediaResourceAdapter.class);

    @Override
    public DBpediaResource deserialize(JsonElement jsonElement,
                                       Type type,
                                       JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException
    {
        try {
            String uriValue = jsonElement.getAsJsonObject().get("@URI").getAsString();
            logger.debug(uriValue);
            return new DBpediaResource(uriValue);
        } catch (URISyntaxException e) {
            final String emsg = "uri syntax error";
            throw new JsonParseException(emsg, e);
        }
    }
}
