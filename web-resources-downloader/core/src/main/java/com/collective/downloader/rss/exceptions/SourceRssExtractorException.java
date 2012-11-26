package com.collective.downloader.rss.exceptions;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class SourceRssExtractorException extends Exception {

    public SourceRssExtractorException(String emsg, Exception e) {
        super(emsg, e);
    }
}
