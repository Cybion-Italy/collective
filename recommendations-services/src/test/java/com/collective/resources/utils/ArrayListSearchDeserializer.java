package com.collective.resources.utils;

import com.collective.permanentsearch.model.Search;
import com.google.gson.*;
import org.apache.log4j.Logger;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class ArrayListSearchDeserializer
        implements JsonDeserializer<ArrayList<Search>> {

        private static final Logger logger =
            Logger.getLogger(ArrayListSearchDeserializer.class);

    public ArrayList<Search> deserialize(JsonElement jsonElement,
                                    Type type,
                                    JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        JsonArray jsonSearches = jsonElement.getAsJsonArray();
        ArrayList<Search> searches = new ArrayList<Search>();

        for (JsonElement jsonSearch : jsonSearches) {
            Search deserialisedSearch =
                    jsonDeserializationContext.deserialize(jsonSearch,
                                                           Search.class);
            searches.add(deserialisedSearch);
        }
        return searches;
    }
}
