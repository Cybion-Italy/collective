package com.collective.resources.writers;

import com.collective.dynamicprofile.model.DynamicUserProfile;
import com.collective.dynamicprofile.model.Interest;
import com.collective.resources.gson.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class DynamicUserProfileWriter
        implements MessageBodyWriter<DynamicUserProfile>
{
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType)
    {
        return DynamicUserProfile.class.isAssignableFrom(aClass);
    }

    public long getSize(DynamicUserProfile dynamicUserProfile, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }

    public void writeTo(DynamicUserProfile dynamicUserProfile,
                        Class<?> aClass,
                        Type type,
                        Annotation[] annotations,
                        MediaType mediaType,
                        MultivaluedMap<String, Object> stringObjectMultivaluedMap,
                        OutputStream outputStream)
            throws IOException, WebApplicationException
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder
                .registerTypeAdapter(DateTime.class, new JodaDateTimeSerializer())
                .registerTypeAdapter(DateTime.class, new JodaDateTimeDeserializer())
                .registerTypeAdapter(Interest.class, new InterestSerializer())
                .registerTypeAdapter(Interval.class, new IntervalSerializer())
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String json = gson.toJson(dynamicUserProfile);
        BufferedOutputStream baos = new BufferedOutputStream(outputStream);
        baos.write(json.getBytes());
        baos.close();
    }
}
