package com.collective.downloader.rss;

import com.collective.model.persistence.WebResource;
import com.sun.syndication.feed.synd.SyndEntry;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class EntriesFilter {

    private static final Logger LOGGER = Logger.getLogger(EntriesFilter.class);

    public List<SyndEntry> selectOnlyRecentEntries(List syndEntriesToFilter,
                                                   DateTime dataUltimaEstrazione,
                                                   WebResource lastExtractedUrl)
    {
        LOGGER.debug("last extracted url: " + lastExtractedUrl.getUrl() + " title: " +
                     lastExtractedUrl.getTitolo());

        List<SyndEntry> filteredSyndEntries = new ArrayList<SyndEntry>();

        Iterator<SyndEntry> iterator = syndEntriesToFilter.iterator();
        boolean newEntry = true;
        while (iterator.hasNext() && newEntry) {
            SyndEntry entry = iterator.next();
            // removed if clause: (publishedDate.compareTo(lastExtractionDate) == 0)
            if (entry.getLink().equals(lastExtractedUrl.getUrl().toString())) {
                LOGGER.debug("reached oldest syndEntry, it was already downloaded " +
                             "- not adding it to the list and terminating iterations");
                LOGGER.debug("'" + entry.getTitle() + "' and old one was '" +
                             lastExtractedUrl.getTitolo() + "'");
                newEntry = false;
            } else {
                // it is newer
                LOGGER.debug("adding url: '" + entry.getLink() + "'");
                LOGGER.debug("with title: '" + entry.getTitle() + "'");
                LOGGER.debug("lastExtractedUrl title: " + lastExtractedUrl.getTitolo());
                filteredSyndEntries.add(entry);
            }
        }
        return filteredSyndEntries;
    }

    public List<SyndEntry> selectAllEntries(List syndEntries)
    {
        List<SyndEntry> listSyndEntries = new ArrayList<SyndEntry>();
        Iterator<SyndEntry> iterator = syndEntries.iterator();
        while (iterator.hasNext()) {
            SyndEntry entry = iterator.next();
            LOGGER.debug("adding url " + entry.getLink());
            LOGGER.debug("with title: " + entry.getTitle());
            listSyndEntries.add(entry);
        }
        return listSyndEntries;
    }
}
