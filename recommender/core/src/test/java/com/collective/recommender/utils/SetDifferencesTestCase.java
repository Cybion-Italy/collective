package com.collective.recommender.utils;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class SetDifferencesTestCase {

    @Test
    public void shouldTestSetDifferences() {

        WebResourceEnhanced webResourceEnhanced =
                DomainFixtures.getWebResourceEnhanced(5);
        WebResourceEnhanced otherWebResourceEnhanced =
                DomainFixtures.getWebResourceEnhanced(6);

        Set<WebResourceEnhanced> oldResourceRecommendations =
                new HashSet<WebResourceEnhanced>();
        oldResourceRecommendations.add(webResourceEnhanced);

        Set<WebResourceEnhanced> newResourceRecommendations =
                new HashSet<WebResourceEnhanced>();
        newResourceRecommendations.add(webResourceEnhanced);
        newResourceRecommendations.add(otherWebResourceEnhanced);

        //test
        newResourceRecommendations.removeAll(oldResourceRecommendations);

        int newRecommendations = newResourceRecommendations.size();

        Assert.assertEquals(newRecommendations, 1);
        Assert.assertTrue(newResourceRecommendations
                            .contains(otherWebResourceEnhanced));
    }
}
