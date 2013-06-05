package com.collective.downloader.rss;

import java.net.MalformedURLException;

import com.collective.downloader.rss.model.fixtures.DomainFixture;
import com.collective.model.persistence.SourceRss;
import com.collective.model.persistence.WebResource;
import org.apache.log4j.Logger;


import com.sun.syndication.feed.synd.SyndEntry;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WebResourceCreatorTest {

    private static final Logger LOGGER = Logger.getLogger(WebResourceCreatorTest.class);

    @Test
    public void shouldCreateUrlFromSource() throws MalformedURLException, BoilerpipeProcessingException {

        WebResourceCreator webResourceCreator = new WebResourceCreator();

        SyndEntry entry = DomainFixture.newSyndEntry();
        SourceRss fonteRss = DomainFixture.newFonteRss();
        WebResource createdUrl;

        createdUrl = webResourceCreator.createWebResourceFromSyndEntry(entry, fonteRss);
        Assert.assertNotNull(createdUrl);
        Assert.assertTrue(entry.getTitle().equals(createdUrl.getTitolo()));
//        LOGGER.info("URL '" + createdUrl.toString() + "' correctly created");
    }

}