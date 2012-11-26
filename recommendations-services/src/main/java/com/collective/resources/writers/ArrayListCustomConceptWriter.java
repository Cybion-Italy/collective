package com.collective.resources.writers;

import com.collective.concepts.core.Concept;
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
import java.util.ArrayList;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ArrayListCustomConceptWriter implements MessageBodyWriter<ArrayList<Concept>> {


    public boolean isWriteable(Class<?> aClass, Type type,
                               Annotation[] annotations, MediaType mediaType) {
        return ArrayList.class.isAssignableFrom(aClass);
    }

    public long getSize(ArrayList<Concept> concepts,
                        Class<?> aClass, Type type, Annotation[] annotations,
                        MediaType mediaType) {
        return -1;
    }

    public void writeTo(ArrayList<Concept> concepts,
                        Class<?> aClass, Type type, Annotation[] annotations,
                        MediaType mediaType,
                        MultivaluedMap<String, Object> stringObjectMultivaluedMap,
                        OutputStream outputStream)
            throws IOException, WebApplicationException {
        /*
        TODO (major): gson is configured to exclude non exposed fields,
        like in other classes needs further investigation to allow
        different configurations */
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder
                .excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(concepts);
        BufferedOutputStream baos = new BufferedOutputStream(outputStream);
        baos.write(json.getBytes());
        baos.close();
    }


}