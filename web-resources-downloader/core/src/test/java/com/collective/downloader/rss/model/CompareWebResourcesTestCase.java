package com.collective.downloader.rss.model;

import com.collective.model.persistence.WebResource;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.testng.annotations.Test;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class CompareWebResourcesTestCase {

    private static Logger logger = Logger.getLogger(CompareWebResourcesTestCase.class);

    @Test
    public void shouldSortUrlsByDate() {
        WebResource firstUrl = new WebResource();
        firstUrl.setDataEstrazione(new DateTime().minusDays(1));
        WebResource secondUrl = new WebResource();
        secondUrl.setDataEstrazione(new DateTime());

        List<WebResource> desiredUrlList = new LinkedList<WebResource>();
        desiredUrlList.add(firstUrl);
        desiredUrlList.add(secondUrl);
        logger.debug(desiredUrlList);
        Collections.sort(desiredUrlList, new CompareWebResourcesByDate());
        //TODO: add asserts
        logger.debug(desiredUrlList);
    }
}
