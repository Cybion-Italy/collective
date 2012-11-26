package com.collective.resources.readers;

import com.collective.resources.search.model.SearchCreationBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class SearchCreationBeanReader implements MessageBodyReader<SearchCreationBean> {

    private static final Logger logger = Logger.getLogger(SearchCreationBeanReader.class);

    public boolean isReadable(Class<?> aClass,
                              Type type, Annotation[] annotations,
                              MediaType mediaType) {
        return SearchCreationBean.class.isAssignableFrom(aClass);
    }

    public SearchCreationBean readFrom(Class<SearchCreationBean> searchCreationBeanClass,
                                       Type type, Annotation[] annotations,
                                       MediaType mediaType,
                                       MultivaluedMap<String, String> stringStringMultivaluedMap,
                                       InputStream inputStream)
            throws IOException, WebApplicationException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .create();
        Reader reader = new InputStreamReader(inputStream);
        SearchCreationBean searchCreationBean =
                gson.fromJson(reader, SearchCreationBean.class);
        return searchCreationBean;
    }
}
