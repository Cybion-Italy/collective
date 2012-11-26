package com.collective.profiler.storage;

import com.collective.model.profile.Profile;
import com.collective.model.profile.ProjectProfile;
import com.collective.model.profile.SearchProfile;
import com.collective.model.profile.UserProfile;
import com.collective.rdfizer.TypedRDFizer;
import com.collective.rdfizer.typehandler.*;
import org.apache.log4j.Logger;
import org.openrdf.model.Resource;
import org.openrdf.model.impl.URIImpl;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Reference test case for {@link com.collective.profiler.storage.SesameVirtuosoProfileStore}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class SesameVirtuosoProfileStoreTestCase {

    private static final String COLLECTIVE_BASE_USER_NS = "http://collective.com/profile/user/";

    private static final String COLLECTIVE_BASE_PROJECT_NS = "http://collective.com/profile/project/";

    private static final String COLLECTIVE_BASE_SEARCH_NS = "http://collective.com/profile/search/";

    private static UserProfile userProfile;

    private static ProjectProfile projectProfile;

    private static SearchProfile searchProfile;

    private static Logger logger = Logger.getLogger(SesameVirtuosoProfileStoreTestCase.class);

    static {
        try {
            userProfile = new UserProfile();
            userProfile.setId(Long.parseLong("5264262472426"));
            userProfile.addInterest(new URI("http://dbpedia.org/resource/Software_Development/"));
            userProfile.addInterest(new URI("http://dbpedia.org/resource/Semantic_Web"));
            userProfile.addInterest(new URI("http://dbpedia.org/resource/Apache_Maven/"));
            userProfile.addSkill(new URI("http://dbpedia.org/resource/Java/"));

            projectProfile = new ProjectProfile();
            projectProfile.setId(Long.parseLong("635735757573"));
            projectProfile.addManifestoConcept(new URI("http://dbpedia.org/resource/Semantic_Web"));
            projectProfile.addManifestoConcept(new URI("http://dbpedia.org/resource/Linked_Data"));

            searchProfile = new SearchProfile();
            searchProfile.setId(Long.parseLong("4522454254"));
            searchProfile.setTitle("title of search");
            searchProfile.addConcept(new URI("http://dbpedia.org/resource/Semantic_Web"));
            searchProfile.addConcept(new URI("http://dbpedia.org/resource/Linked_Data"));
        } catch (URISyntaxException e) {
            // never occurs
        }
    }

    private ProfileStore profileStore;

    @BeforeTest
    public void setUp() throws RepositoryException, URISyntaxException, TypeHandlerRegistryException {

        Map<String, URI> nameSpacesMap = new HashMap<String, URI>();
        nameSpacesMap.put("first-dataSource", new URI("http://cybion.it/profile/dataSource/"));
        nameSpacesMap.put("second-dataSource", new URI("http://cybion.it/profile/secondDataSource/"));
        ProfileStoreConfiguration profileStoreConfiguration = new ProfileStoreConfiguration(
                "cibionte.cybion.eu",
                "1111",
                "dba",
                "cybiondba",
                nameSpacesMap
        );
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

        profileStore = new SesameVirtuosoProfileStore(
                profileStoreConfiguration,
                typedRdfizer
        );
    }

    @AfterTest
    public void tearDown() throws ProfileStoreException {
        profileStore.close();
        profileStore = null;
    }

    @Test(enabled = true)
    public void testUserProfileMinimalCRUD() throws ProfileStoreException, URISyntaxException {
        Long profileId = userProfile.getId();
        logger.debug("userProfile: " + userProfile.toString());
        profileStore.storeProfile(userProfile, new URI(COLLECTIVE_BASE_USER_NS + profileId));
        Profile retrievedProfile = profileStore.getUserProfile(new URI(COLLECTIVE_BASE_USER_NS + profileId));
        Assert.assertNotNull(retrievedProfile);
        Assert.assertEquals(userProfile, retrievedProfile);
        profileStore.deleteGraph(new URI(COLLECTIVE_BASE_USER_NS + profileId));
    }

    @Test(enabled = true)
    public void testProjectProfileMinimalCRUD() throws ProfileStoreException, URISyntaxException {
        Long projectProfileId = projectProfile.getId();
        logger.debug("projectProfile: " + projectProfile.toString());
        profileStore.storeProfile(projectProfile, new URI(COLLECTIVE_BASE_PROJECT_NS + projectProfileId));
        ProjectProfile retrievedProjectProfile = profileStore.getProjectProfile(new URI(COLLECTIVE_BASE_PROJECT_NS + projectProfileId));
        Assert.assertNotNull(retrievedProjectProfile);
        Assert.assertEquals(projectProfile, retrievedProjectProfile);
        profileStore.deleteGraph(new URI(COLLECTIVE_BASE_PROJECT_NS + projectProfileId));
    }

    @Test(enabled = true)
    public void testSearchProfileMinimalCRUD() throws ProfileStoreException, URISyntaxException {
        Long searchProfileId = searchProfile.getId();
        logger.debug("searchProfile: " + searchProfile.toString());
        profileStore.storeProfile(searchProfile, new URI(COLLECTIVE_BASE_SEARCH_NS + searchProfileId));
        SearchProfile retrievedSearchProfile = profileStore.getSearchProfile(new URI(COLLECTIVE_BASE_SEARCH_NS + searchProfileId));
        Assert.assertNotNull(retrievedSearchProfile);
        Assert.assertEquals(searchProfile, retrievedSearchProfile);
        profileStore.deleteGraph(new URI(COLLECTIVE_BASE_SEARCH_NS + searchProfileId));
    }


    @Test(enabled = true)
    public void testProfileCRDIndex() throws ProfileStoreException, URISyntaxException {
        URI indexGraphName = new URI("http://collective.com/test/fake/profile/indexStorage/");
        URI profileGraphName = new URI("http://collective.com/test/fake/profile/12345");
        List<Resource> formerProfileResources = profileStore.getAllTriplesSubjectsFromGraphIndex(indexGraphName);
        profileStore.storeProfileToGraphIndex(indexGraphName, profileGraphName);
        List<Resource> profileResources = profileStore.getAllTriplesSubjectsFromGraphIndex(indexGraphName);
        profileStore.deleteProfileFromGraphIndex(indexGraphName, profileGraphName);
        Resource expected = new URIImpl("http://collective.com/test/fake/profile/12345");
        Assert.assertTrue(profileResources.contains(expected));
        Assert.assertTrue(profileResources.size() == formerProfileResources.size() + 1);
    }

}
