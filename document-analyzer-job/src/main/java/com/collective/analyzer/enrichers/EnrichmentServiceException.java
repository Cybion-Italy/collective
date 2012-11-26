package com.collective.analyzer.enrichers;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class EnrichmentServiceException extends Exception
{
    public EnrichmentServiceException(String emsg, Exception e) {
           super(emsg, e);
    }
}
