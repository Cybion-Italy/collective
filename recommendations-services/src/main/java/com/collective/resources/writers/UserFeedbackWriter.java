package com.collective.resources.writers;

import com.collective.usertests.model.UserFeedback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class UserFeedbackWriter implements MessageBodyWriter<UserFeedback> {
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return UserFeedback.class.isAssignableFrom(aClass);
    }

    public long getSize(UserFeedback userFeedback, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    public void writeTo(UserFeedback userFeedback, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> stringObjectMultivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder
                .excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(userFeedback);
        BufferedOutputStream baos = new BufferedOutputStream(outputStream);
        baos.write(json.getBytes());
        baos.close();
    }
}
