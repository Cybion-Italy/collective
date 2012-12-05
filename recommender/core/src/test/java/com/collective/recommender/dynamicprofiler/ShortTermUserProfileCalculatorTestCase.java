package com.collective.recommender.dynamicprofiler;

import com.collective.analyzer.enrichers.EnrichmentService;
import com.collective.analyzer.enrichers.dbpedia.DBPediaAPI;
import com.collective.recommender.categories.model.MappedResource;
import it.cybion.extractor.ContentExtractor;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class ShortTermUserProfileCalculatorTestCase {

    private ShortTermUserProfileCalculator shortTermUserProfileCalculator;

    @BeforeClass
    public void setUp() {
        EnrichmentService enricher = new DBPediaAPI();
        ContentExtractor contentExtractor = new ContentExtractor();
        shortTermUserProfileCalculator = new ShortTermUserProfileCalculator(enricher, contentExtractor);
    }

    @AfterClass
    public void tearDown() {
        shortTermUserProfileCalculator = null;
    }

    @Test
    public void shouldUpdateProfileFromListOfMappedResources() throws URISyntaxException {
        List<MappedResource> latestMappedResources = new ArrayList<MappedResource>();
        long happenedAt = 1L;
        long userId = 55L;
        String resourceUrl = "http://www.businessinsider.com/profits-versus-wages";
        MappedResource mappedResource = new MappedResource(happenedAt, userId, resourceUrl);
        latestMappedResources.add(mappedResource);
        ShortTermUserProfile shortTermUserProfile = shortTermUserProfileCalculator
                .updateProfile( new URI(resourceUrl), latestMappedResources );

        latestMappedResources.add(mappedResource);
    }

}
