package com.collective.analyzer.enrichers.dbpedia;

import com.collective.analyzer.enrichers.EnrichmentServiceException;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class DBpediaAPIException extends EnrichmentServiceException
{
    public DBpediaAPIException(String emsg, Exception e) {
        super(emsg, e);
    }
}
