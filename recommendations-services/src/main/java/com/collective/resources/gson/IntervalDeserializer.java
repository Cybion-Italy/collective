package com.collective.resources.gson;

import com.google.gson.*;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.lang.reflect.Type;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class IntervalDeserializer implements JsonDeserializer<Interval>
{
    @Override
    public Interval deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException
    {
        JsonObject jsonInterest = jsonElement.getAsJsonObject();
        String startString = jsonInterest.get("start").getAsString();
        String endString = jsonInterest.get("end").getAsString();
        DateTime start = new DateTime(startString);
        DateTime end = new DateTime(endString);
        return new Interval(start, end);
    }
}
