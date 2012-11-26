package com.collective.resources.gson;

import com.collective.dynamicprofile.model.Interest;
import com.collective.permanentsearch.model.LabelledURI;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class InterestSerializer implements JsonSerializer<Interest>
{
    @Override
    public JsonElement serialize(Interest interest, Type type, JsonSerializationContext jsonSerializationContext)
    {
        JsonObject object = new JsonObject();
        object.add("weight", new JsonPrimitive(interest.getWeight()));
        object.add("labelledURI",
                   jsonSerializationContext.serialize(interest.getConcept(), LabelledURI.class));
        return object;
    }
}
