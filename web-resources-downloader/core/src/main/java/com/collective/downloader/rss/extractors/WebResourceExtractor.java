package com.collective.downloader.rss.extractors;

import com.collective.downloader.rss.exceptions.WebSourcesRepositoryException;
import com.collective.downloader.rss.storage.WebSourcesRepository;
import com.collective.model.persistence.Source;
import com.collective.model.persistence.WebResource;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebResourceExtractor {

    private static Logger logger = Logger.getLogger(WebResourceExtractor.class);

    private WebSourcesRepository webSourcesRepository;

    private Map<Source, String> sourcesNotAnalyzed = new HashMap<Source, String>();

    public WebResourceExtractor(WebSourcesRepository webSourcesRepository) {
        this.webSourcesRepository = webSourcesRepository;
    }

    //build a list of latest webResources to be saved in database
    public List<WebResource> fetchNewWebResources() {
        List<WebResource> webResources = new ArrayList<WebResource>();

        logger.info("Fetching all new webresources");

        //to get only sources and its with VALID fonti_rss
        List<Source> sources = webSourcesRepository.getSources();

        logger.info("'" + sources.size() + "'sources " +
                "- with at least ONE valid fonte_rss source");

        for (Source source : sources) {
            logger.info("retrieved: '" + source.getId()
                    + "' - '" + source.getNome() + "'");
            List<WebResource> webResourcesFromSource = new ArrayList<WebResource>();
            try {
                webResourcesFromSource.addAll(
                        webSourcesRepository.retrieveLatestWebResources(source));
            } catch (WebSourcesRepositoryException e) {
                logger.warn("source '" + source + "' has not been analyzed", e);
                sourcesNotAnalyzed.put(source, e.getMessage());
            }
            webResources.addAll(webResourcesFromSource);
        }
        return webResources;
    }
}
