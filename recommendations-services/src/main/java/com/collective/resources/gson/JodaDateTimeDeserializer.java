package com.collective.resources.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.joda.time.DateTime;

import java.lang.reflect.Type;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class JodaDateTimeDeserializer implements JsonDeserializer<DateTime> {

    public DateTime deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return new DateTime(jsonElement.getAsString()); 
    }
}
