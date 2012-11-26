package com.collective.resources.gson;

import com.collective.dynamicprofile.model.Interest;
import com.collective.permanentsearch.model.LabelledURI;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class InterestDeserializer implements JsonDeserializer<Interest>
{
    @Override
    public Interest deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException
    {
        JsonObject jsonInterest = jsonElement.getAsJsonObject();
        float weight = jsonInterest.get("weight").getAsFloat();
        LabelledURI concept = jsonDeserializationContext
                .deserialize(jsonInterest.get("labelledURI").getAsJsonObject(), LabelledURI.class);
        Interest interest = new Interest(weight, concept);
        return interest;
    }
}
