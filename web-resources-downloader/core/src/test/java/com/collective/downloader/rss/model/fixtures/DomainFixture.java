package com.collective.downloader.rss.model.fixtures;

import java.util.Date;

import com.collective.model.persistence.SourceRss;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;

/**
 * This class is just an utility to provide some test data.
 */
public class DomainFixture {
	
	public static SyndEntry newSyndEntry() {
		SyndEntry syndEntry = new SyndEntryImpl();
		SyndContent syndContent = new SyndContentImpl();
		syndContent.setValue("syndContent");
		Date date = new Date();
		syndEntry.setPublishedDate(date);
		syndEntry.setTitle("title example");
		syndEntry.setLink("file:src/test/resources/test_extraction.html");
		syndEntry.setDescription(syndContent);
		return syndEntry;
	}

	public static SourceRss newFonteRss() {
		SourceRss fonteRss = new SourceRss();
		fonteRss.setLingua("inglese esempio");
		fonteRss.setParola("aa bb cc");
		return fonteRss;
	}

}
