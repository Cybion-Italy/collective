package com.collective.resources.readers;

import com.collective.resources.enrichment.EnrichmentRequest;
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
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class TextEnrichmentRequest implements MessageBodyReader<EnrichmentRequest>
{
    private static final Logger logger = Logger.getLogger(TextEnrichmentRequest.class);

    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType)
    {
        return EnrichmentRequest.class.isAssignableFrom(aClass);
    }

    public EnrichmentRequest readFrom(Class<EnrichmentRequest> enrichmentRequestClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> stringStringMultivaluedMap, InputStream inputStream)
            throws IOException, WebApplicationException
    {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .create();
        Reader reader = new InputStreamReader(inputStream);
        EnrichmentRequest er = gson.fromJson(reader, EnrichmentRequest.class);
        logger.debug(er.toString());
        return er;
    }
}
