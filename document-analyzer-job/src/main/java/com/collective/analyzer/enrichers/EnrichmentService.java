package com.collective.analyzer.enrichers;

import java.net.URI;
import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public interface EnrichmentService
{
    public List<URI> getConceptsURIs(String textToAnalyze) throws EnrichmentServiceException;
}
