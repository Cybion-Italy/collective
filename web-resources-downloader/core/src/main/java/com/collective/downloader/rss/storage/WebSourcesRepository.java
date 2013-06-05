package com.collective.downloader.rss.storage;

import com.collective.downloader.rss.extractors.SourceRssExtractor;
import com.collective.downloader.rss.exceptions.SourceRssExtractorException;
import com.collective.downloader.rss.exceptions.WebSourcesRepositoryException;
import com.collective.model.persistence.Source;
import com.collective.model.persistence.SourceRss;
import com.collective.model.persistence.WebResource;
import com.collective.persistencewebresources.persistence.dao.SourceDao;
import com.collective.persistencewebresources.persistence.dao.SourceRssDao;
import com.collective.persistencewebresources.persistence.dao.WebResourceDao;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.util.*;

public class WebSourcesRepository {

    private static final Logger LOGGER = Logger.getLogger(WebSourcesRepository.class);
	
	private Map<Source, String> notAnalyedSources = new HashMap<Source, String>();

    private Map<String, String> insertErrors = new HashMap<String, String>();

	private SourceDao fonteDao;

    private WebResourceDao webResourceDao;

    private WebResourceDao urlDao;

    private SourceRssExtractor extractor;

    private SourceRssDao fonteRssDao;

    public WebSourcesRepository(Properties properties, SourceRssExtractor extractor) {
		fonteDao = new SourceDao(properties);
        webResourceDao = new WebResourceDao(properties);
		urlDao = new WebResourceDao(properties);
		fonteRssDao = new SourceRssDao(properties);
        this.extractor = extractor;
	}

	public Map<Source, String> getNotAnalyedSources() {
		return notAnalyedSources;
	}

    public List<WebResource> retrieveLatestWebResources(Source source)
            throws WebSourcesRepositoryException {

        List<WebResource> webResourcesList = new ArrayList<WebResource>();

        LOGGER.info("getting all SourceRSS of Source: " + source.getUrl().toString());

        Source sourceWithSourcesRSS = fonteDao.selectFonteAndFontiRss(source.getId());

        if (sourceWithSourcesRSS.getSourcesRss() == null) {
            final String emsg = "failed to load sources RSS";
            throw new WebSourcesRepositoryException(emsg);
        }

        List<SourceRss> sourceRssList = sourceWithSourcesRSS.getSourcesRss();

        for (SourceRss sourceRss : sourceRssList)
        {
            //calculate the last extracted WebResource from this sourceRss
            List<WebResource> latestExtractedWebResource = new ArrayList<WebResource>();
            latestExtractedWebResource.addAll(
                    urlDao.selectLastExtractedFrom(sourceRss)
            );

            /* if at least ONE WebResource has been already extracted form this sourceRss,
             * call the method that extracts new WebResources, stopping at that one
             * since it's the oldest one */

            List<WebResource> sourceRssWebResources = new ArrayList<WebResource>();
             if (latestExtractedWebResource.size() > 0) {
                try {
                    sourceRssWebResources.addAll(extractor.extractUrls(sourceRss,
                            latestExtractedWebResource.get(0))
                    );
                } catch (SourceRssExtractorException e) {
                    final String emsg = "failed to extract urls from sourceRss: '"
                            + sourceRss.getId()
                            + "' using as latest WebResource: '"
                            + latestExtractedWebResource.get(0).getId() +"'";
                    throw new WebSourcesRepositoryException(emsg, e);
                }
            } else {
                 /* this sourceRss has NEVER been extracted, so go on and extract all
                  * its webResources contained */
                 try {
                     sourceRssWebResources.addAll(extractor.extractAllUrls(sourceRss));
                 } catch (SourceRssExtractorException e) {
                     final String emsg = " failed to extract all urls from sourceRss: '" +
                             sourceRss.getId() + "'";
                     throw new WebSourcesRepositoryException(emsg, e);
                 }
             }

            DateTime latestPublicationDate = this.findLatestPublicationDate(sourceRssWebResources);
            if (latestPublicationDate.isAfter(new DateTime(0))) {
                this.updateLastExtractionDate(sourceRss, latestPublicationDate);
            }
            webResourcesList.addAll(sourceRssWebResources);
        }
        return webResourcesList;
    }

    private DateTime findLatestPublicationDate(List<WebResource> webResources) {
		DateTime lastExtractionDateTime = new DateTime(0);
		for (WebResource webResource : webResources) {
			if (webResource.getDataPubblicazione().isAfter(lastExtractionDateTime)) {
				lastExtractionDateTime = webResource.getDataPubblicazione();
			}
		}
		return lastExtractionDateTime;
	}

	private void updateLastExtractionDate(SourceRss fonteRss, DateTime lastExtractionDate) {
		fonteRss.setDataUltimaEstrazione(lastExtractionDate);
		fonteRssDao.update(fonteRss);
	}

    public void saveAllWebResources(List<WebResource> allRetrievedWebResources) {
        //save all extracted urls to db
        for (WebResource webResource : allRetrievedWebResources) {
            try {
                webResourceDao.insert(webResource);
            } catch (Exception e) {
                insertErrors.put(webResource.getUrl().toString(), e.getMessage());
            }
        }
        LOGGER.info(allRetrievedWebResources.size() + " web resources have been iterated");
        if (insertErrors.keySet().size() > 0) {
            LOGGER.error(
                    "Some webResources have been not saved (duplicates): '" + buildExceptionMessage(
                            this.insertErrors));
        }
    }

    private String buildExceptionMessage(Map<String, String> insertErrors) {
        StringBuilder sb = new StringBuilder();
        String exceptionHeader = "\nPROBLEMS WITH SOME WEBRESOURCES\n";
        sb.append(exceptionHeader);
        for (String url : insertErrors.keySet()) {
            String exception = "URL  = " + url.toString() + "\n"
                             + "ERROR = " + insertErrors.get(url) + "\n";
            sb.append(exception);
        }
        return sb.toString();
    }

    public List<Source> getSources() {
        List<Source> sources = this.fonteDao.selectAllFonteAndValidFontiRss();
        return sources;
    }
}
