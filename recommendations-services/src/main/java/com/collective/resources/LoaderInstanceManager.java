package com.collective.resources;

import com.collective.activity.storage.ActivityStore;
import com.collective.activity.storage.DefaultActivityStoreImpl;
import com.collective.analyzer.enrichers.EnrichmentService;
import com.collective.analyzer.enrichers.dbpedia.DBPediaAPI;
import com.collective.concepts.core.DefaultUserDefinedConceptStoreImpl;
import com.collective.concepts.core.UserDefinedConceptStore;
import com.collective.permanentsearch.persistence.dao.SearchDao;
import com.collective.profiler.storage.ProfileStore;
import com.collective.profiler.storage.ProfileStoreConfiguration;
import com.collective.profiler.storage.SesameVirtuosoProfileStore;
import com.collective.resources.recommendations.RecommendationsStore;
import com.collective.resources.utilities.LimitChecker;
import com.collective.usertests.persistence.dao.ReasonDao;
import com.collective.usertests.persistence.dao.UserFeedbackDao;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.nnsoft.be3.Be3;
import org.nnsoft.be3.DefaultTypedBe3Impl;
import org.nnsoft.be3.typehandler.*;
import org.openrdf.model.vocabulary.XMLSchema;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.configuration.ConfigurationManager;
import tv.notube.commons.storage.kvs.configuration.KVStoreConfiguration;
import tv.notube.commons.storage.kvs.mybatis.MyBatisKVStore;
import tv.notube.commons.storage.model.fields.serialization.SerializationManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LoaderInstanceManager
{

    private static final String KVS_CONF = "kvs-configuration.xml";

    private static final String KVS_CONF_ACTIVITY_STORE = "kvs-activity-store-configuration.xml";

    private static final String GLOBAL_CONF = "recommendations-module-configuration.xml";

/* TODO (HIGH) refactor all this configuration to be read dynamically from a file,
	 to make testing phase easier */
    //reads data from gaia

    /* recommendations store */
//    private static final String host = "gaia.cybion.eu";
    private static final String host = "cibionte.cybion.eu";
    private static final String port = "3306";
    private static final String database = "kvs";
    private static final String username = "kvs";
    private static final String password = "kvs";

    //connection parameters for triple store
//    private static final String hostVirtuoso = "gaia.cybion.eu";
    private static final String hostVirtuoso = "cibionte.cybion.eu";
    private static final String portVirtuoso = "1111";
    private static final String usernameVirtuoso = "dba";
    private static final String passwordVirtuoso = "cybiondba";

    //connection parameters for searchDao repository
//    private static final String hostSearch = "gaia.cybion.eu";
    private static final String hostSearch = "cibionte.cybion.eu";
    private static final String portSearch = "3306";
    private static final String databaseSearch = "collective-permanent-search";
    private static final String usernameSearch = "collective";
    private static final String passwordSearch = "collective";

    //connection parameters for userfeedbacks repository
//    private static final String hostFeedback = "gaia.cybion.eu";
    private static final String hostFeedback = "cibionte.cybion.eu";
    private static final String portFeedback = "3306";
    private static final String databaseFeedback = "collective-test";
    private static final String usernameFeedback = "collective";
    private static final String passwordFeedback = "collective";

    private static LoaderInstanceManager instance;

    private RecommendationsStore recommendationsStore;

    private ProfileStore profileStore;

    private LimitChecker limitChecker;

    private SearchDao searchDao;

    private Gson gson;

    private UserFeedbackDao userFeedbackDao;

    private ReasonDao reasonDao;

    private UserDefinedConceptStore userDefinedConceptStore;

    private ActivityStore activityStore;

    private Logger logger = Logger.getLogger(LoaderInstanceManager.class);

    private EnrichmentService dbpediaEnrichmentService;

    public static LoaderInstanceManager getInstance()
    {
        if (instance == null)
            instance = new LoaderInstanceManager();
        return instance;
    }

    /* TODO: (high) refactor database and profileStore properties as an external xml configuration file */
    private LoaderInstanceManager()
    {

        logger.info("starting loader init");
        initRecommendationStore();

        limitChecker = new LimitChecker();
        logger.debug("built " + limitChecker.toString());

        //init profileStore
        try {
            initProfileStore();
        } catch (URISyntaxException e) {
            logger.error("error while initialising profileStore: " + e.getMessage());
        }

        //initialize searchDao
        initSearchDao();

        //initialize gson
        gson = new Gson();

        //init userFeedback dao
        initUserFeedbackDao();

        //init reasons dao
        initReasonDao();

        //init the User defined concept store
        initConceptStore();

        //init the user activityStore
        initActivityStore();

        //init dbpedia enrichment service
        initDbPediaEnrichmentService();
        logger.debug("completed loader init");
    }

    private void initDbPediaEnrichmentService()
    {
        logger.debug("init enrichment");
        dbpediaEnrichmentService = new DBPediaAPI();
        logger.debug("completed enrichment");
    }

    private void initActivityStore()
    {
        KVStoreConfiguration configuration = ConfigurationManager
                .getInstance(KVS_CONF_ACTIVITY_STORE)
                .getKVStoreConfiguration();

        logger.debug("kvs connection properties for activityStore: "
                             + configuration.getProperties().toString());

        KVStore kvs = new MyBatisKVStore(
                configuration.getProperties(),
                new SerializationManager()
        );
        activityStore = new DefaultActivityStoreImpl(kvs);
        logger.debug("activity store instantiated");
    }

    private void initRecommendationStore()
    {
        Properties properties = new Properties();
        properties.setProperty("url", "jdbc:mysql://" + host + ":" + port + "/" + database);
        properties.setProperty("username", username);
        properties.setProperty("password", password);
        recommendationsStore = new RecommendationsStore(properties);
    }

    private void initConceptStore()
    {
        KVStoreConfiguration configuration = ConfigurationManager
                .getInstance(KVS_CONF)
                .getKVStoreConfiguration();

        logger.debug("kvs connection properties for custom concepts store: "
                             + configuration.getProperties().toString());

        KVStore kvs = new MyBatisKVStore(
                configuration.getProperties(),
                new SerializationManager()
        );
        userDefinedConceptStore = new DefaultUserDefinedConceptStoreImpl(kvs);
    }

    private void initReasonDao()
    {
        /* TODO (high) remove as discontinued */
        Properties propertiesDao = new Properties();
        propertiesDao.setProperty("url",
                                  "jdbc:mysql://" + hostFeedback + ":" + portFeedback + "/" + databaseFeedback);
        propertiesDao.setProperty("username", usernameFeedback);
        propertiesDao.setProperty("password", passwordFeedback);
        reasonDao = new ReasonDao(propertiesDao);
    }

    private void initUserFeedbackDao()
    {
        /* TODO (high) remove as discontinued */
        Properties propertiesDao = new Properties();
        propertiesDao.setProperty("url",
                                  "jdbc:mysql://" + hostFeedback + ":" + portFeedback + "/" + databaseFeedback);
        propertiesDao.setProperty("username", usernameFeedback);
        propertiesDao.setProperty("password", passwordFeedback);
        userFeedbackDao = new UserFeedbackDao(propertiesDao);
    }

    private void initSearchDao()
    {
        //init searchDao
        Properties propertiesDao = new Properties();
        propertiesDao.setProperty("url",
                                  "jdbc:mysql://" + hostSearch + ":" + portSearch + "/" + databaseSearch);
        propertiesDao.setProperty("username", usernameSearch);
        propertiesDao.setProperty("password", passwordSearch);
        searchDao = new SearchDao(propertiesDao);
    }

    private void initProfileStore() throws URISyntaxException
    {
        Map<String, URI> nameSpacesConfiguration = new HashMap<String, URI>();

        String profileStoreName = "search";
        String profileStoreUri = "http://collective.com/profile/search/";

        if (profileStoreUri.charAt(profileStoreUri.length() - 1) != '/') {
            throw new RuntimeException(
                    "URIs should terminate with a slash. It seems it is missing ");
        }

        try {
            nameSpacesConfiguration.put(profileStoreName, new URI(profileStoreUri));
        } catch (URISyntaxException e) {
            final String errorMsg = "used wrong uri to define profileStoreUri: " + profileStoreUri;
            logger.error(errorMsg);
            throw new URISyntaxException(errorMsg, "");
        }
        ProfileStoreConfiguration profileStoreConfiguration =
                new ProfileStoreConfiguration(hostVirtuoso, portVirtuoso, usernameVirtuoso,
                                              passwordVirtuoso,
                                              nameSpacesConfiguration);
        Be3 rdFizer;
        try {
            rdFizer = getRDFizer();
        } catch (TypeHandlerRegistryException e) {
            throw new RuntimeException("Error while initializing the TypedRDFizer", e);
        }
        profileStore = new SesameVirtuosoProfileStore(profileStoreConfiguration, rdFizer);
    }

    private Be3 getRDFizer() throws TypeHandlerRegistryException
    {
        TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
        URIResourceTypeHandler uriResourceTypeHandler = new URIResourceTypeHandler();
        StringValueTypeHandler stringValueTypeHandler = new StringValueTypeHandler();
        IntegerValueTypeHandler integerValueTypeHandler = new IntegerValueTypeHandler();
        URLResourceTypeHandler urlResourceTypeHandler = new URLResourceTypeHandler();
        DateValueTypeHandler dateValueTypeHandler = new DateValueTypeHandler();
        LongValueTypeHandler longValueTypeHandler = new LongValueTypeHandler();
        typeHandlerRegistry.registerTypeHandler(uriResourceTypeHandler, java.net.URI.class,
                                                XMLSchema.ANYURI);
        typeHandlerRegistry.registerTypeHandler(stringValueTypeHandler, String.class,
                                                XMLSchema.STRING);
        typeHandlerRegistry.registerTypeHandler(integerValueTypeHandler, Integer.class,
                                                XMLSchema.INTEGER);
        typeHandlerRegistry.registerTypeHandler(integerValueTypeHandler, Integer.class,
                                                XMLSchema.INT);
        typeHandlerRegistry.registerTypeHandler(urlResourceTypeHandler, URL.class,
                                                XMLSchema.ANYURI);
        typeHandlerRegistry.registerTypeHandler(dateValueTypeHandler, Date.class, XMLSchema.DATE);
        typeHandlerRegistry.registerTypeHandler(longValueTypeHandler, Long.class, XMLSchema.LONG);
        return new DefaultTypedBe3Impl(null, typeHandlerRegistry);
    }

    RecommendationsStore getRecommendationsStore()
    {
        return recommendationsStore;
    }

    public LimitChecker getLimitChecker()
    {
        return limitChecker;
    }

    public ProfileStore getProfileStore()
    {
        return profileStore;
    }

    public SearchDao getSearchDao()
    {
        return searchDao;
    }

    public Gson getGson()
    {
        return gson;
    }

    public ReasonDao getReasonDao()
    {
        return reasonDao;
    }

    public UserFeedbackDao getUserFeedbackDao()
    {
        return userFeedbackDao;
    }

    public UserDefinedConceptStore getUserDefinedConceptStore()
    {
        return userDefinedConceptStore;
    }

    public ActivityStore getActivityStore()
    {
        return activityStore;
    }

    public EnrichmentService getDbPediaEnrichmentService()
    {
        return dbpediaEnrichmentService;
    }
}
