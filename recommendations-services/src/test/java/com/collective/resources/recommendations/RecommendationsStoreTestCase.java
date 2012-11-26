package com.collective.resources.recommendations;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.ProjectProfile;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

/**
 * @author Davide Palmisano (dpalmisano@gmail.com)
 */
public class RecommendationsStoreTestCase {

    private final static String BASE_URI = "http://collective.com/profile/user";

    private RecommendationsStore recommendationsStore;

    @BeforeTest
    public void setUp() {
        /* TODO (med) refactor to use xml configuration */
        Properties properties = new Properties();
        properties.setProperty("url", "jdbc:mysql://cibionte.cybion.eu:3306/recommender");
        properties.setProperty("username", "rec");
        properties.setProperty("password", "recpass");
        recommendationsStore = new RecommendationsStore(properties);
    }

    @AfterTest
    public void tearDown() {
        recommendationsStore = null;
    }

    @Test
    public void testGetResourceRecommendationsForUser()
            throws URISyntaxException, RecommendationsStoreException {
        final int userId = 22;
        List<WebResourceEnhanced> recs = recommendationsStore.getResourceRecommendationsForUser(
                new URI(BASE_URI + "/" + userId),
                ResourceRecommendationType.USERS

        );
        Assert.assertNotNull(recs);
        Assert.assertTrue(recs.size() > 0);
    }

    @Test
    public void testGetProjectRecommendationsForUser()
            throws URISyntaxException, RecommendationsStoreException {
        final int userId = 21;
        List<ProjectProfile> recs =
                recommendationsStore.getProjectRecommendationsForUser(
                        new URI(BASE_URI + "/" + userId)
                );
        Assert.assertNotNull(recs);
        Assert.assertTrue(recs.size() > 0);

    }

}
