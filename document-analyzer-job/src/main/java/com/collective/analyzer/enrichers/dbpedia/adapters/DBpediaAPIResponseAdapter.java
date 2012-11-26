package com.collective.analyzer.enrichers.dbpedia.adapters;

import com.collective.analyzer.enrichers.dbpedia.model.DBpediaAPIResponse;
import com.collective.analyzer.enrichers.dbpedia.model.DBpediaResource;
import com.google.gson.*;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class DBpediaAPIResponseAdapter implements JsonDeserializer<DBpediaAPIResponse>
{

    private static final Logger logger = Logger.getLogger(DBpediaAPIResponseAdapter.class);

    @Override
    public DBpediaAPIResponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException
    {
        String textValue = jsonElement.getAsJsonObject().get("@text").getAsString();
        logger.debug(textValue);
        DBpediaAPIResponse dBpediaAPIResponse = new DBpediaAPIResponse(textValue);

        if ( jsonElement.getAsJsonObject().has("Resources") )
        {
            JsonElement resourcesArray = jsonElement.getAsJsonObject().get("Resources");

            if (resourcesArray.isJsonArray()) {
                JsonArray array = resourcesArray.getAsJsonArray();
                for (int i = 0; i < array.size(); i++) {
                    JsonElement element = array.get(i);
                    DBpediaResource dbp = jsonDeserializationContext.deserialize(element,
                                                                                 DBpediaResource.class);
                    dBpediaAPIResponse.addDbpediaResource(dbp);
                }
            }
        }


        return dBpediaAPIResponse;
    }
}
