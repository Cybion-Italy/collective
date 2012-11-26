package com.collective.resources.writers;

import com.collective.usertests.model.UserFeedback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

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
import java.util.ArrayList;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ArrayListUserFeedbackWriter implements MessageBodyWriter<ArrayList<UserFeedback>> {

    private static Logger logger = Logger.getLogger(ArrayListUserFeedbackWriter.class);

    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return ArrayList.class.isAssignableFrom(aClass);
    }

    public long getSize(ArrayList<UserFeedback> userFeedbacks,
                        Class<?> aClass, Type type, Annotation[] annotations,
                        MediaType mediaType) {
        return -1;
    }

    public void writeTo(ArrayList<UserFeedback> userFeedbacks,
                        Class<?> aClass, Type type, Annotation[] annotations,
                        MediaType mediaType, MultivaluedMap<String, Object> stringObjectMultivaluedMap,
                        OutputStream outputStream) throws IOException,
            WebApplicationException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String json = gson.toJson(userFeedbacks);
        BufferedOutputStream baos = new BufferedOutputStream(outputStream);
        baos.write(json.getBytes());
        baos.close();
    }
}
