package com.collective.recommender;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.ProjectProfile;
import com.collective.model.profile.UserProfile;
import com.collective.permanentsearch.model.LabelledURI;
import com.collective.permanentsearch.model.Search;
import com.collective.rdfizer.TypedRDFizer;
import com.collective.rdfizer.typehandler.*;
import com.collective.recommender.configuration.ConfigurationManager;
import com.collective.recommender.configuration.RecommenderConfiguration;
import com.collective.recommender.proxy.ranking.Ranker;
import com.collective.recommender.proxy.ranking.RankerException;
import com.collective.recommender.proxy.ranking.WebResourceEnhancedRanker;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.Sail;
import org.openrdf.sail.memory.MemoryStore;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.commons.storage.alog.DefaultActivityLogImpl;
import tv.notube.commons.storage.model.Activity;
import tv.notube.commons.storage.model.ActivityLog;
import tv.notube.commons.storage.model.ActivityLogException;
import tv.notube.commons.storage.model.fields.Field;
import tv.notube.commons.storage.model.fields.IntegerField;
import tv.notube.commons.storage.model.fields.StringField;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * TODO: med remove dependency from repository data
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SesameVirtuosoRecommenderTestCase {

    private static Logger logger = Logger.getLogger(SesameVirtuosoRecommenderTestCase.class);

    private final static String CONFIG_FILE = "src/test/resources/recommender-configuration.xml";

    private Recommender recommender;

    @BeforeTest
    public void setUp() throws RepositoryException, URISyntaxException, TypeHandlerRegistryException {
        RecommenderConfiguration recommenderConfiguration
                = ConfigurationManager.getInstance(CONFIG_FILE).getRecommenderConfiguration();

        //the rdfizer is configured in memory, not the sparql proxy
        Sail sailStack = new MemoryStore();
        Repository repository = new SailRepository(sailStack);
        repository.initialize();
        TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
        URIResourceTypeHandler uriResourceTypeHandler = new URIResourceTypeHandler();
        StringValueTypeHandler stringValueTypeHandler = new StringValueTypeHandler();
        IntegerValueTypeHandler integerValueTypeHandler = new IntegerValueTypeHandler();
        URLResourceTypeHandler urlResourceTypeHandler = new URLResourceTypeHandler();
        DateValueTypeHandler dateValueTypeHandler = new DateValueTypeHandler();
        LongValueTypeHandler longValueTypeHandler = new LongValueTypeHandler();
        typeHandlerRegistry.registerTypeHandler(uriResourceTypeHandler, java.net.URI.class, XMLSchema.ANYURI);
        typeHandlerRegistry.registerTypeHandler(stringValueTypeHandler, String.class, XMLSchema.STRING);
        typeHandlerRegistry.registerTypeHandler(integerValueTypeHandler, Integer.class, XMLSchema.INTEGER);
        typeHandlerRegistry.registerTypeHandler(integerValueTypeHandler, Integer.class, XMLSchema.INT);
        typeHandlerRegistry.registerTypeHandler(urlResourceTypeHandler, URL.class, XMLSchema.ANYURI);
        typeHandlerRegistry.registerTypeHandler(dateValueTypeHandler, Date.class, XMLSchema.DATE);
        typeHandlerRegistry.registerTypeHandler(longValueTypeHandler, Long.class, XMLSchema.LONG);
        TypedRDFizer typedRdfizer = new TypedRDFizer(repository, typeHandlerRegistry);

        recommender = new SesameVirtuosoRecommender(recommenderConfiguration, typedRdfizer);
    }

    @AfterTest
    public void tearDown() {
        recommender = null;
    }

    @Test
    public void testGetResourceRecommendationsForProjects()
            throws URISyntaxException, RecommenderException {
        ProjectProfile profile = new ProjectProfile();
        profile.setId(Long.parseLong("5325426246"));
        profile.addManifestoConcept(new URI("http://dbpedia.org/resource/Operating_system"));
        profile.addManifestoConcept(new URI("http://dbpedia.org/resource/Software_engineering"));

        Set<WebResourceEnhanced> resources = recommender.getResourceRecommendations(profile);
        logger.info("Number of resources: '"  + resources.size() + "'");
        Assert.assertNotNull(resources);
        Assert.assertTrue(resources.size() > 0);
        for(WebResourceEnhanced resource : resources) {
            logger.info("Retrieved resource: '"  + resource.getTitolo()
                    + " - " + resource.getTopics() + "'");
            Assert.assertTrue(resource.getTopics().contains(
                    new URI("http://dbpedia.org/resource/Operating_system"))
                    || (resource.getTopics().contains(new URI("http://dbpedia.org/resource/Software_engineering"))));
        }
    }

    @Test
    public void testGetResourceRecommandations()
            throws URISyntaxException, RecommenderException {
        UserProfile profile = new UserProfile();
        profile.setId(Long.parseLong("542624727272642"));
        profile.addInterest(new URI("http://dbpedia.org/resource/Floor_cleaning"));
        profile.addInterest(new URI("http://dbpedia.org/resource/Design"));
        // profile.addInterest(new URI("http://dbpedia.org/resource/Software_engineering"));
        
        Set<WebResourceEnhanced> resources = recommender.getResourceRecommendations(profile);

        Ranker ranker = new WebResourceEnhancedRanker();
        List<WebResourceEnhanced> recommendationsList = new ArrayList<WebResourceEnhanced>(resources);

        try {
            ranker.rank(recommendationsList);
        } catch (RankerException e) {
            final String errMsg = "Error while sorting Recommendations";
            logger.error(errMsg, e);
        }

        Assert.assertNotNull(recommendationsList);
        Assert.assertTrue(recommendationsList.size() >= 0);
        for(WebResourceEnhanced resource : recommendationsList) {
            logger.info("Retrieved resource: '"  + resource.getId() + "'");
        }
    }

    @Test
    public void testGetProjectRecommendationsForUser()
            throws URISyntaxException, RecommenderException {
        UserProfile profile = new UserProfile();
        profile.setId(Long.parseLong("542624727272642"));
        profile.addInterest(new URI("http://dbpedia.org/resource/Semantic_Web"));
        profile.addInterest(new URI("http://dbpedia.org/resource/Google_Web_Toolkit"));

        Set<ProjectProfile> projectProfiles = recommender.getProjectRecommendations(profile);
        Assert.assertNotNull(projectProfiles);
        Assert.assertTrue(projectProfiles.size() > 0);
        for(ProjectProfile projectProfile : projectProfiles) {
            logger.info("Retrieved resource: '"  + projectProfile + "'");
        }
    }

    @Test
    public void testGetExpertUsersForProject()
            throws URISyntaxException, RecommenderException {
        ProjectProfile projectProfile = new ProjectProfile();
        projectProfile.setId(11L);
        projectProfile.addManifestoConcept(new URI("http://dbpedia.org/resource/SPARQL"));
        projectProfile.addManifestoConcept(new URI("http://dbpedia.org/resource/Resource_Description_Framework"));
        projectProfile.addManifestoConcept(new URI("http://dbpedia.org/resource/Semantic_Web"));

        Set<UserProfile> usersProfiles = recommender.getExpertUsersForProject(projectProfile);
        Assert.assertNotNull(usersProfiles);
        Assert.assertTrue(usersProfiles.size() > 0);
        for (UserProfile userProfile : usersProfiles) {
            logger.info("Retrieved user: '" + userProfile + "'");
        }
    }

    @Test
    public void testWithExistingProfile() throws URISyntaxException, RecommenderException {
        final Long userId = 73456832L;
        UserProfile profile = new UserProfile();
        profile.setId(userId);
        profile.addInterest(new URI("http://dbpedia.org/resource/Video_game_developer"));
        profile.addInterest(new URI("http://dbpedia.org/resource/Lean_software_development"));
        profile.addInterest(new URI("http://dbpedia.org/resource/Product_%28chemistry%29"));
        profile.addInterest(new URI("http://dbpedia.org/resource/Agile_software_development"));
        profile.addInterest(new URI("http://dbpedia.org/resource/Entrepreneur"));
        profile.addInterest(new URI("http://dbpedia.org/resource/Software_development_process"));
        profile.addInterest(new URI("http://dbpedia.org/resource/Speaker_%28politics%29"));
        profile.addInterest(new URI("http://dbpedia.org/resource/Management"));
        profile.addInterest(new URI("http://dbpedia.org/resource/Author"));
        profile.addSkill(new URI("http://dbpedia.org/resource/Sears,_Roebuck_and_Company_Administration_Building"));
        profile.addSkill(new URI("http://dbpedia.org/resource/Lean_software_development"));
        profile.addSkill(new URI("http://dbpedia.org/resource/Extreme_Programming"));

        Set<WebResourceEnhanced> resources = recommender.getResourceRecommendations(profile);
        Assert.assertNotNull(resources);
        Assert.assertTrue(resources.size() > 0);
    }

    @Test
    public void testGetResourceRecommendationsForSearches()
            throws URISyntaxException, RecommenderException {
        Search search = new Search();
        search.setId(Long.parseLong("5325426246"));
        LabelledURI labelledURI = new LabelledURI();
        labelledURI.setLabel("operating system");
        labelledURI.setUrl(new URI("http://dbpedia.org/resource/Operating_system"));
        search.addCommonUri(labelledURI);

        Set<WebResourceEnhanced> resources =
                recommender.getResourceRecommendations(search.getCommonUris());
        logger.info("Number of resources: '"  + resources.size() + "'");
        Assert.assertNotNull(resources);
        Assert.assertTrue(resources.size() > 0);
        for(WebResourceEnhanced resource : resources) {
            logger.info("Retrieved resource: '"  + resource.getTitolo()
                    + " - " + resource.getTopics() + "'");
            Assert.assertTrue(resource.getTopics().contains(
                    new URI("http://dbpedia.org/resource/Operating_system")));
        }
    }
}
