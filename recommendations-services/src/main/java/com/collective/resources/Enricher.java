package com.collective.resources;

import com.collective.analyzer.enrichers.EnrichmentServiceException;
import com.collective.resources.enrichment.EnrichmentRequest;
import com.sun.jersey.api.core.InjectParam;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Used by external clients to analyse some plain text or some url,
 * getting DBpedia or AlchemyAPI URIs in response.
 *
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
@Path("/enricher")
@Produces(MediaType.APPLICATION_JSON)
public class Enricher
{
    private static final Logger logger = Logger.getLogger(Enricher.class);

    @InjectParam
    private InstanceManager instanceManager;

    public Enricher() {}

    @POST
    @Path("/url")
    @Produces(MediaType.APPLICATION_JSON)
    public List<URI> getConceptsURIsFromUrl(EnrichmentRequest enrichmentRequest)
    {
        logger.info("got url: '" + enrichmentRequest.getUrl() + "'");
        String urlAfterDecoding = "";
        try {
            urlAfterDecoding = URLDecoder.decode(enrichmentRequest.getUrl(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(
                    "failed to decode parameters using UTF-8");
        }
        logger.info("got decoded url: '" + urlAfterDecoding + "'");

        URL urlToAnalyse = null;
        try {
            urlToAnalyse = new URL(urlAfterDecoding);
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "failed to build url from" + urlToAnalyse.toString());
        }

        //use boilerpipe to extract text from url

        String text = "";

        try {
            text = ArticleExtractor.INSTANCE.getText(urlToAnalyse);
        } catch (BoilerpipeProcessingException e) {
            final String emsg = "failed to extract text from url '" + urlToAnalyse.toString() + "'";
            throw new RuntimeException(emsg, e);
        }
        return extractConcepts(text);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/text")
    public List<URI> getConceptsURIsFromText(EnrichmentRequest enrichmentRequest)
            throws URISyntaxException
    {
        logger.info("got text to analyse: " + enrichmentRequest.getText() + "'");

        return extractConcepts(enrichmentRequest.getText());
    }

    private List<URI> extractConcepts(String text)
    {

        List<URI> concepts = new ArrayList<URI>();
        try {
            concepts.addAll(
                    instanceManager
                            .getDBpediaEnrichmentService()
                            .getConceptsURIs(text)
            );
        } catch (EnrichmentServiceException e) {
            throw new RuntimeException(
                    "failed to analyse text: '" + text + "'");
        }
        return concepts;
    }

}
