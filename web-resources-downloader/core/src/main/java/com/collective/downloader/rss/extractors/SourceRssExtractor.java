package com.collective.downloader.rss.extractors;

import com.collective.downloader.rss.WebResourceCreator;
import com.collective.downloader.rss.EntriesFilter;
import com.collective.downloader.rss.exceptions.SourceRssExtractorException;
import com.collective.downloader.rss.utils.XmlEncodingContentCleaner;
import com.collective.model.persistence.SourceRss;
import com.collective.model.persistence.WebResource;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class SourceRssExtractor {

    private static final Logger logger = Logger.getLogger(SourceRssExtractor.class);

    private EntriesFilter entriesFilter;

    private WebResourceCreator webResourceCreator;

    public SourceRssExtractor(EntriesFilter entriesFilter, WebResourceCreator creator) {
        this.webResourceCreator = creator;
        this.entriesFilter = entriesFilter;
    }

    public List<WebResource> extractUrls(SourceRss fonteRss,
                                         WebResource lastExtractedUrl)
        throws SourceRssExtractorException
    {
        List<WebResource> extractedUrls = new ArrayList<WebResource>();

        URL feedUrl = fonteRss.getUrl();
        URLConnection urlConnectionFeed;

        InputStream cleanedFeedInputStream = null;

        logger.info("Cleaning RSS feed xml:'" + fonteRss.getUrl() + "'");
        try {
            urlConnectionFeed = feedUrl.openConnection();

            InputStream feedInputStream = null;
            XmlEncodingContentCleaner xmlEncodingContentCleaner =
                    new XmlEncodingContentCleaner();

            feedInputStream = urlConnectionFeed.getInputStream();
            cleanedFeedInputStream = xmlEncodingContentCleaner
                    .cleanInputStream(feedInputStream);

        } catch (IOException e) {
            final String emsg = "error while cleaning XML of feed: "
                    + fonteRss.getUrl();
            logger.error(emsg);
            throw new SourceRssExtractorException(emsg,e);
        }

        logger.info("Reading RSS feed xml:'" + fonteRss.getUrl() + "'");
        XmlReader xmlReaderInput;
        try {
            xmlReaderInput = new XmlReader(cleanedFeedInputStream);
        } catch (IOException e) {
            final String emsg = "I/O Error while reading cleaned feed stream:'"
                    + cleanedFeedInputStream + "'";
            logger.error(emsg);
            throw new SourceRssExtractorException(emsg,e);
        }

        // get as singleton for performance reasons?
        SyndFeedInput rssFeedInput = new SyndFeedInput();
        SyndFeed feed;

        logger.info("Building SyndFeed from xml: '" + fonteRss.getUrl() + "'");
        try {
            feed = rssFeedInput.build(xmlReaderInput);
        } catch (FeedException e) {
            final String emsg = "Error while reading the feed stream:'\" + xmlReaderInput + \"'";
            logger.error(emsg);
            throw new SourceRssExtractorException(emsg, e);
        }

        checkNulls(feedUrl, feed);

        List<SyndEntry> syndEntries = new ArrayList<SyndEntry>();
        if (lastExtractedUrl != null)
        {
            syndEntries.addAll(
                    this.entriesFilter.selectOnlyRecentEntries(
                            feed.getEntries(),
                            fonteRss.getDataUltimaEstrazione(),
                            lastExtractedUrl)
            );
        } else {
            syndEntries.addAll(
                    this.entriesFilter.selectAllEntries(
                            feed.getEntries())
            );
        }

        if (syndEntries.size() > 0)
        {
            //to browse articles from OLDEST to MOST RECENT
            Collections.reverse(syndEntries);
            for (SyndEntry entry : syndEntries) {
                try {
                    WebResource createdUrl;
                    createdUrl = webResourceCreator.createWebResourceFromSyndEntry(
                            entry,
                            fonteRss);
                    extractedUrls.add(createdUrl);
                } catch (MalformedURLException e) {
                    final String emsg = "url is malformed";
                    throw new SourceRssExtractorException(emsg, e);
                } catch (BoilerpipeProcessingException e) {
                    final String emsg = "boilerpipe extraction failed";
                    throw new SourceRssExtractorException(emsg, e);
                }
            }
        }
        return extractedUrls;
    }

    public List<WebResource> extractAllUrls(SourceRss sourceRss)
    throws SourceRssExtractorException {
        return this.extractUrls(sourceRss, null);
    }

    private void checkNulls(URL feedUrl, SyndFeed feed) {
        if (feedUrl == null || feed == null) {
            final String emsg = "Problems creating an URL object " +
                    "and reading the rss page of that URL: inconsistent state.";
            logger.error(emsg);
            throw new IllegalStateException(emsg);
        }
    }
}
