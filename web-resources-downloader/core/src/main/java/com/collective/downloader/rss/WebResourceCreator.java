package com.collective.downloader.rss;

import com.collective.downloader.rss.utils.HtmlCleaner;
import com.collective.downloader.rss.utils.SimpleRegexHtmlCleaner;
import com.collective.model.persistence.SourceRss;
import com.collective.model.persistence.WebResource;
import com.sun.syndication.feed.synd.SyndEntry;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import org.joda.time.DateTime;

import java.net.MalformedURLException;
import java.net.URL;

public class WebResourceCreator {
	
	private HtmlCleaner htmlCleaner;
	
	public WebResourceCreator() {
		htmlCleaner = new SimpleRegexHtmlCleaner();
	}

	public WebResource createWebResourceFromSyndEntry(SyndEntry entry, SourceRss fonteRss)
            throws MalformedURLException, BoilerpipeProcessingException {
		WebResource createdUrl = new WebResource();
		createdUrl.setSourceRss(fonteRss);
		//to jodatime
		createdUrl.setDataPubblicazione(new DateTime(entry.getPublishedDate()));
		createdUrl.setTitolo(entry.getTitle());
		createdUrl.setUrl(new URL(entry.getLink()));
		//short summary, contains rich html
        String cleanedSummary = "";
        if (entry.getDescription() != null) {
		 cleanedSummary = htmlCleaner.stripHtml(entry.getDescription().getValue());
        }
		createdUrl.setDescrizione(cleanedSummary);
		String contenutoTesto = "";
        //TODO EXCLUDED BOILERPIPE when using on twitter
//		ContentExtractor ce = new ContentExtractor();
//		contenutoTesto = ce.getContentFromUrl(new URL(entry.getLink()));
		createdUrl.setContenutoTesto(contenutoTesto);
		//jodatime, right now since i am extracting it now!
		createdUrl.setDataEstrazione(new DateTime());
		return createdUrl;
	}

}
