package com.collective.resources.gson;

import com.google.gson.*;
import org.joda.time.DateTime;

import java.lang.reflect.Type;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class JodaDateTimeSerializer implements JsonSerializer<DateTime> {

    public JsonElement serialize(
            DateTime dateTime,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return new JsonPrimitive(dateTime.toString());
    }
}
