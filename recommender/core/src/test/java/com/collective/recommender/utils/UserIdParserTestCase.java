package com.collective.recommender.utils;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class UserIdParserTestCase {

    private UserIdParser userIdParser;

    private static final Logger logger = Logger.getLogger(UserIdParserTestCase.class);

    @BeforeTest
    public void setUp() {
        this.userIdParser = new UserIdParser();
    }

    @AfterTest
    public void tearDown() {
        this.userIdParser = null;
    }

    @Test
    public void shouldParseUserFromUrl() throws UserIdParserException {

        String encodedCompany = "no_company";
        String encodedOwner = "12";
        String encodedName = "concept_label";

        URI customConcept = null;
        try {
            customConcept = new URI("http://collective.com/concepts/"
                    + encodedCompany + "/"
                    + encodedOwner + "/"
                    + encodedName);
        } catch (URISyntaxException e) {
            //should never happen
            e.printStackTrace();
        }

        Long parsedUserId = userIdParser.getUserId(customConcept);
        Long parsedEncodedOwner = Long.parseLong(encodedOwner);

        logger.debug("customConcept:" + customConcept.toString());
        logger.debug("ownerId:" + parsedEncodedOwner);

        Assert.assertEquals(parsedUserId, parsedEncodedOwner);
    }
}
