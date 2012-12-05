package com.collective.recommender.dynamicprofiler;

import com.collective.analyzer.enrichers.EnrichmentService;
import com.collective.analyzer.enrichers.EnrichmentServiceException;
import com.collective.recommender.categories.model.MappedResource;
import com.google.common.collect.Lists;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import it.cybion.extractor.ContentExtractor;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class ShortTermUserProfileCalculator {

    private EnrichmentService enrichmentService;

    private ContentExtractor contentExtractor;

    private final static Logger LOGGER = Logger.getLogger(ShortTermUserProfileCalculator.class);

    public ShortTermUserProfileCalculator(final EnrichmentService enrichmentService,
                                          final ContentExtractor contentExtractor) {
        this.enrichmentService = enrichmentService;
        this.contentExtractor = contentExtractor;
    }

    public ShortTermUserProfile updateProfile(URI userId, List<MappedResource> latestMappedResources) {

        List<URI> concepts = Lists.newArrayList();

        for (MappedResource resource : latestMappedResources) {

            URL resourceUrl;
            try {
                resourceUrl = new URL(resource.getResourceUrl());

                String content = "";
                content = contentExtractor.getContentFromUrl(resourceUrl);

                concepts.addAll( enrichmentService.getConceptsURIs(content) );

            } catch (MalformedURLException e) {
                final String urlError = "failed building an url from '"
                        + resource.getResourceUrl()
                        + "' it should have been an url";
                LOGGER.error(urlError);
                continue;
            } catch (BoilerpipeProcessingException e) {
                final String extractionError = "failed while extracting content from '"
                        + resource.getResourceUrl() + "'";
                LOGGER.error(extractionError);
                continue;
            } catch (EnrichmentServiceException e) {
                final String enrichmentError = "failed to enrich content using external service";
                LOGGER.error(enrichmentError);
            }

        }   //for all MappedResources

        //eventually, do some counting
        ShortTermUserProfile shortTermProfile = new ShortTermUserProfile(concepts);
        LOGGER.debug("found " + shortTermProfile.getInterests().size()
                + " concepts for short term user profile " + userId);
        return shortTermProfile;
    }
}
