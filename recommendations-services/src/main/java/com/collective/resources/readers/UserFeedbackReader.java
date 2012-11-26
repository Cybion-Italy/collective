package com.collective.resources.readers;

import com.collective.usertests.model.UserFeedback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class UserFeedbackReader implements MessageBodyReader<UserFeedback> {

    private static final Logger logger = Logger.getLogger(UserFeedbackReader.class);

    public boolean isReadable(Class<?> aClass, Type type,
                              Annotation[] annotations, MediaType mediaType) {
        return UserFeedback.class.isAssignableFrom(aClass);
    }

    public UserFeedback readFrom(Class<UserFeedback> userFeedbackClass,
                                 Type type, Annotation[] annotations,
                                 MediaType mediaType,
                                 MultivaluedMap<String, String> stringStringMultivaluedMap,
                                 InputStream inputStream) throws IOException, WebApplicationException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .create();
        Reader reader = new InputStreamReader(inputStream);
        UserFeedback userFeedback = gson.fromJson(reader, UserFeedback.class);
        return userFeedback;
    }
}
