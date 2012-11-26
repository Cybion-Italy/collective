package com.collective.analyzer.enrichers.lupedia;

import com.collective.analyzer.enrichers.EnrichmentService;
import com.collective.analyzer.enrichers.EnrichmentServiceException;

import java.net.URI;
import java.util.List;

/**
 * implements an {@link EnrichmentService} using the api at
 * http://lupedia.ontotext.com/help.html
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class LuPediaAPI implements EnrichmentService
{

    @Override
    public List<URI> getConceptsURIs(String textToAnalyze)
            throws EnrichmentServiceException
    {

        throw new UnsupportedOperationException(
                "not implemented yet");  //To change body of implemented methods use File | Settings | File Templates.
    }
}
