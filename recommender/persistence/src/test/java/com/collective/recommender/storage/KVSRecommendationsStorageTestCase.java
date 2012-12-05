package com.collective.recommender.storage;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.ProjectProfile;
import com.collective.model.profile.UserProfile;
import com.collective.recommender.configuration.ConfigurationManager;
import com.collective.recommender.utils.PersistenceDomainFixtures;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;

/**
 *
 * TODO this should not hit the database
 * @author Davide Palmisano ( dpalmisano@gmail.com ) 
 */
public class KVSRecommendationsStorageTestCase {

    private static final String USER_URI = "http://collective.com/user/ontology/26261";

    private static final String PROJECT_URI = "http://collective.com/project/ontology/4625";

    private final static String CONFIG_FILE = "src/test/resources/recommender-configuration.xml";

    private RecommendationsStorage recommendationsStorage;

    @BeforeTest
    public void setUp() {
        Properties properties = ConfigurationManager.getInstance(CONFIG_FILE)
                .getRecommendationsStorageConfiguration()
                .getProperties();
        recommendationsStorage = new KVSRecommendationsStorage(properties);
    }

    @AfterTest
    public void tearDown() {
        recommendationsStorage = null;
    }

    @Test
    public void testResourceRecommendationsCRUD()
            throws URISyntaxException, MalformedURLException,
            RecommendationsStorageException {
        List<WebResourceEnhanced> expected = PersistenceDomainFixtures.getResources();
        recommendationsStorage.deleteResourceRecommendations(new URI(USER_URI));
        recommendationsStorage.storeResourceRecommendations(new URI(USER_URI),
                expected);
        List<WebResourceEnhanced> actual =
                recommendationsStorage.getResourceRecommendations(new URI(USER_URI));
        Assert.assertNotNull(actual);
        Assert.assertEqualsNoOrder(actual.toArray(), expected.toArray());
        recommendationsStorage.deleteResourceRecommendations(new URI(USER_URI));
    }

    @Test
    public void shouldSerializeAndDeserializeEmptyList()
            throws URISyntaxException, RecommendationsStorageException {
        recommendationsStorage.deleteResourceRecommendations(new URI(USER_URI));
        List<WebResourceEnhanced> emptyList = new ArrayList<WebResourceEnhanced>();
        recommendationsStorage.storeResourceRecommendations(new URI(USER_URI),
                emptyList);
        List<WebResourceEnhanced> actual =
                recommendationsStorage.getResourceRecommendations(new URI(USER_URI));
        Assert.assertNotNull(actual);
        Assert.assertEqualsNoOrder(actual.toArray(), emptyList.toArray());
        recommendationsStorage.deleteResourceRecommendations(new URI(USER_URI));
    }

    @Test
    public void testProjectRecommendationsCRUD()
            throws URISyntaxException, MalformedURLException,
            RecommendationsStorageException {
        List<ProjectProfile> expected = PersistenceDomainFixtures.getProjects();
        recommendationsStorage.deleteProjectProfileRecommendations(new URI(USER_URI));
        recommendationsStorage.storeProjectProfileRecommendations(new URI(USER_URI),
                expected);
        List<ProjectProfile> actual =
                recommendationsStorage.getProjectRecommendations(new URI(USER_URI));
        Assert.assertNotNull(actual);
        Assert.assertEqualsNoOrder(actual.toArray(), expected.toArray());
        recommendationsStorage.deleteProjectProfileRecommendations(new URI(USER_URI));
    }

    @Test
    public void testExpertsRecommendationsCRUD()
            throws URISyntaxException, RecommendationsStorageException {
        recommendationsStorage.deleteExpertsRecommandationsForProject(new URI(PROJECT_URI));
        List<UserProfile> expected = PersistenceDomainFixtures.getUserProfiles();
        recommendationsStorage.storeExpertsRecommendations(new URI(PROJECT_URI),
                expected);
        List<UserProfile> actual =
                recommendationsStorage.getExpertsRecommendations(new URI(PROJECT_URI));
        Assert.assertNotNull(actual);
        Assert.assertEqualsNoOrder(actual.toArray(), expected.toArray());
        recommendationsStorage.deleteExpertsRecommandationsForProject(new URI(PROJECT_URI));
    }

    @Test
    public void shouldTestCRUDShortTermRecommendations() throws URISyntaxException, MalformedURLException {
        try {
            recommendationsStorage.deleteShortTermResourceRecommendations(new URI(USER_URI));
        } catch (RecommendationsStorageException e) {
            fail("failed deleting short term recs");
        }

        List<WebResourceEnhanced> expectedRecs = PersistenceDomainFixtures.getResources();
        try {
            recommendationsStorage.storeShortTermResourceRecommendations(
                    new URI(USER_URI),
                    expectedRecs);
        } catch (RecommendationsStorageException e) {
            fail("failed storing short term recs");
        }

        List<WebResourceEnhanced> actualRecs = new ArrayList<WebResourceEnhanced>();
        try {
            actualRecs.addAll(recommendationsStorage.getShortTermResourceRecommendations(new URI(USER_URI)));
        } catch (RecommendationsStorageException e) {
            fail("failed getting short term recs");
        }

        assertNotNull(actualRecs);
        assertEqualsNoOrder(actualRecs.toArray(), expectedRecs.toArray());

        try {
            recommendationsStorage.deleteShortTermResourceRecommendations(new URI(USER_URI));
        } catch (RecommendationsStorageException e) {
            fail("failed deleting short term recs");
        }
    }
}
