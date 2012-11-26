package com.collective.profiler.storage;

import com.collective.model.profile.UserProfile;
import com.collective.rdfizer.TypedRDFizer;
import com.collective.rdfizer.typehandler.*;
import org.apache.log4j.Logger;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.Sail;
import org.openrdf.sail.memory.MemoryStore;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Reference test case for {@link SesameVirtuosoProfileStore}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ImportIntoSesameVirtuosoProfileStoreTestCase {

    private static final String COLLECTIVE_BASE_USER_NS = "http://collective.com/profile/user/";

    private static final String PROFILE_TESTBED_INDEX = "http://collective.com/testbed/profile/user/" ;

    private static List<UserProfile> userProfiles = new ArrayList<UserProfile>();

    private static Logger logger = Logger.getLogger(ImportIntoSesameVirtuosoProfileStoreTestCase.class);

    static {
        try {
            UserProfile userProfile1 = new UserProfile();
            userProfile1.setId(Long.parseLong("1501"));
            userProfile1.addInterest(new URI("http://dbpedia.org/resource/Entrepreneurship"));
            userProfile1.addInterest(new URI("http://dbpedia.org/resource/Entrepreneur"));
            userProfile1.addInterest(new URI("http://dbpedia.org/resource/Creativity"));
            userProfile1.addInterest(new URI("http://dbpedia.org/resource/Innovation"));
            userProfile1.addInterest(new URI("http://dbpedia.org/resource/Open_innovation"));
            userProfile1.addInterest(new URI("http://dbpedia.org/resource/Henry_Chesbrough"));
            userProfiles.add(userProfile1);

            UserProfile userProfile2 = new UserProfile();
            userProfile2.setId(Long.parseLong("1502"));
            userProfile2.addInterest(new URI("http://dbpedia.org/resource/Text_mining"));
            userProfile2.addInterest(new URI("http://dbpedia.org/resource/Semantics"));
            userProfile2.addInterest(new URI("http://dbpedia.org/resource/Linguistics"));
            userProfile2.addInterest(new URI("http://dbpedia.org/resource/Natural_language_processing"));
            userProfiles.add(userProfile2);

            UserProfile userProfile3 = new UserProfile();
            userProfile3.setId(Long.parseLong("1503"));
            userProfile3.addInterest(new URI("http://dbpedia.org/resource/Social_network"));
            userProfile3.addInterest(new URI("http://dbpedia.org/resource/Graphical_user_interface"));
            userProfile3.addInterest(new URI("http://dbpedia.org/resource/Twitter"));
            userProfile3.addInterest(new URI("http://dbpedia.org/resource/Facebook"));
            userProfile3.addInterest(new URI("http://dbpedia.org/resource/Usability"));
            userProfiles.add(userProfile3);
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
        typeHandlerRegistry.registerTypeHandler(uriResourceTypeHandler, URI.class, XMLSchema.ANYURI);
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

    @Test(enabled = false)
    public void testImport() throws URISyntaxException, ProfileStoreException {
        for (UserProfile userProfile : userProfiles) {
            Long profileId = userProfile.getId();
            profileStore.storeProfile(
                    userProfile,
                    new URI(COLLECTIVE_BASE_USER_NS + profileId)
            );
            profileStore.storeProfileToGraphIndex(
                    new URI(PROFILE_TESTBED_INDEX),
                    new URI(COLLECTIVE_BASE_USER_NS + profileId)
            );
        }
    }

}
