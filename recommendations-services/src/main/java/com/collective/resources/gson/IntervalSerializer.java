package com.collective.resources.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.lang.reflect.Type;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class IntervalSerializer implements JsonSerializer<Interval>
{

    @Override
    public JsonElement serialize(Interval interval, Type type, JsonSerializationContext jsonSerializationContext)
    {
        JsonObject object = new JsonObject();
        object.add("start", jsonSerializationContext.serialize(interval.getStart(), DateTime.class));
        object.add("end", jsonSerializationContext.serialize(interval.getEnd(), DateTime.class));
        return object;
    }
}