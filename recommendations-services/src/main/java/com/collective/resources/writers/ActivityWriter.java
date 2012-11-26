package com.collective.resources.writers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.abdera2.activities.model.Activity;
import org.apache.log4j.Logger;

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
public class ActivityWriter implements MessageBodyWriter<Activity>
{
    private static final Logger logger = Logger.getLogger(ActivityWriter.class);

    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType)
    {
        return Activity.class.isAssignableFrom(aClass);
    }

    public long getSize(Activity activity, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }

    public void writeTo(Activity activity, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> stringObjectMultivaluedMap, OutputStream outputStream)
            throws IOException, WebApplicationException
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String json = gson.toJson(activity);
        BufferedOutputStream baos = new BufferedOutputStream(outputStream);
        baos.write(json.getBytes());
        baos.close();
    }
}
