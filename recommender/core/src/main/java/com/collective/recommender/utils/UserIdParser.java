package com.collective.recommender.utils;

import org.apache.log4j.Logger;

import java.net.URI;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class UserIdParser {

    //TODO med: it is a code smell, it is the same as the custom concepts base url
    public static final String BASE = "http://collective.com/concepts/";

    private static final Logger logger = Logger.getLogger(UserIdParser.class);

    public UserIdParser() {}

    public Long getUserId(URI url) throws UserIdParserException {
        Long userId = -1L;
        //BASE + encodedCompany + "/" + encodedOwner + "/" + encodedName);
        String[] splittedOnSlashes = url.toString().split("/");
        userId = Long.parseLong(splittedOnSlashes[5]);
        //TODO manage exception throwing
        return userId;
    }
}
