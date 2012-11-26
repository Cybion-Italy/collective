package com.collective.downloader.rss;


import com.collective.downloader.rss.exceptions.WebResourceExtractorException;
import com.collective.model.persistence.SourceRss;
import com.collective.model.persistence.WebResource;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import com.collective.downloader.rss.utils.XmlEncodingContentCleaner;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

public class EntriesFilterTestCase {

    private static Logger logger = Logger.getLogger(EntriesFilterTestCase.class);

    private EntriesFilter entriesFilter;

    @BeforeClass
    public void startup() {
        this.entriesFilter = new EntriesFilter();
    }

    @AfterClass
    public void shutdown () {
        this.entriesFilter = null;
    }

	//to solve #29 ticket, not sure if always valid
	//check resources/testfiles/feed_error_character.xml
	//at Policy <here is the incorrect character > Communications
    // TODO: check if this is still to be fixed and eventually fix it.
	@Test (enabled = false)
	public void testXmlParsing() {
		
		//the error was only with this feed on 5/1/2010
		//change to load local file to see the error
		String urlTest = "http://feeds.feedburner.com/readwriteweb";

        logger.info("Analysing rss " + urlTest);

		URL feedUrl = null;
		URLConnection urlConnectionFeed = null;
		SyndFeed feed = null;
		
		try {
			feedUrl = new URL(urlTest);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// try to open a connection and detect xml encoding
		try {
			urlConnectionFeed = feedUrl.openConnection();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// get as singleton for performance reasons?
		SyndFeedInput rssFeedInput = new SyndFeedInput();
		
		InputStream feedInputStream = null;
		InputStream cleanedFeedInputStream = null;
		XmlEncodingContentCleaner xmlEncodingContentCleaner = new XmlEncodingContentCleaner();

		try {
			feedInputStream = urlConnectionFeed.getInputStream();
			cleanedFeedInputStream = xmlEncodingContentCleaner.cleanInputStream(feedInputStream);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			//build the reader on cleaned content			
			XmlReader xmlReaderInput = new XmlReader(cleanedFeedInputStream);
			
			feed = rssFeedInput.build(xmlReaderInput);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("extractEx2:  " + e.getMessage());
		}

	}

    @Test
    public void shouldFilterOldUrls() throws MalformedURLException {

        WebResource lastExtractedUrl = new WebResource();
        lastExtractedUrl.setDataEstrazione(new DateTime());
        lastExtractedUrl.setTitolo("title");
        lastExtractedUrl.setUrl(new URL("http://www.example.com"));

        SyndEntry first = new SyndEntryImpl();
        first.setLink(lastExtractedUrl.getUrl().toString());
        first.setTitle(lastExtractedUrl.getTitolo());

        SyndEntry second = new SyndEntryImpl();
        second.setLink("second fake link");
        second.setTitle("second title");

        SyndEntry third = new SyndEntryImpl();
        third.setLink("third fake link");
        third.setTitle("third title");

        List<SyndEntry> syndEntries = new LinkedList<SyndEntry>();
        syndEntries.add(first);
        syndEntries.add(second);
        syndEntries.add(third);

        DateTime now = new DateTime();
        List<SyndEntry> filteredSyndEntries = entriesFilter.selectOnlyRecentEntries(
                syndEntries,
                now,
                lastExtractedUrl);
        Assert.assertTrue(filteredSyndEntries.size() == 0);

        List<SyndEntry> otherSyndEntries = new LinkedList<SyndEntry>();
        otherSyndEntries.add(second);
        otherSyndEntries.add(third);
        otherSyndEntries.add(first);

        List<SyndEntry> otherFilteredSyndEntries =
                entriesFilter.selectOnlyRecentEntries(otherSyndEntries, now, lastExtractedUrl);
        Assert.assertTrue(otherFilteredSyndEntries.size() == 2);
    }

    @Test
    public void shouldGetAllUrls() throws MalformedURLException {

        SyndEntry second = new SyndEntryImpl();
        second.setLink("second fake link");
        second.setTitle("second title");

        SyndEntry third = new SyndEntryImpl();
        third.setLink("third fake link");
        third.setTitle("third title");

        List<SyndEntry> syndEntries = new LinkedList<SyndEntry>();
        syndEntries.add(second);
        syndEntries.add(third);

        List<SyndEntry> filteredSyndEntries = entriesFilter.selectAllEntries(syndEntries);

        Assert.assertTrue(filteredSyndEntries.size() == 2);
    }

    //TODO: reenable after test refactoring
    @Test (enabled = false)
    public void shouldInvertDatesOfUrls() throws MalformedURLException, BoilerpipeProcessingException, WebResourceExtractorException {
        //create a fonteRss with lastExtractedUrl Url
        SourceRss fonteRss = new SourceRss();
        fonteRss.setUrl(new URL("http://ted.europa.eu/TED/rss/en/RSS_agfo_EU.xml"));
        fonteRss.setDataUltimaEstrazione(new DateTime().minusDays(5));

        WebResource lastExtractedUrl = new WebResource();
//        lastExtractedUrl.setTitolo("");
        lastExtractedUrl.setUrl(new URL("http://ted.europa.eu/udl?uri=TED:NOTICE:99158-2011:TEXT:EN:HTML"));

        //call extractUrls()
//        WebResourceExtractor webResourceExtractor = new WebResourceExtractor();
//        List<WebResource> extractedUrls = webResourceExtractor.extractUrls(fonteRss, lastExtractedUrl);


        //check that the list obtained is ordered by ascending date dataEstrazione
        // AND publication date of synd entries should be ASC accordingly

    }

}
