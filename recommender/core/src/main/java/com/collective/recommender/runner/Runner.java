package com.collective.recommender.runner;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.ProjectProfile;
import com.collective.model.profile.UserProfile;
import com.collective.permanentsearch.model.Search;
import com.collective.profiler.storage.ProfileStore;
import com.collective.profiler.storage.ProfileStoreConfiguration;
import com.collective.profiler.storage.ProfileStoreException;
import com.collective.profiler.storage.SesameVirtuosoProfileStore;
import com.collective.rdfizer.RDFizer;
import com.collective.rdfizer.TypedRDFizer;
import com.collective.rdfizer.typehandler.*;
import com.collective.recommender.Recommender;
import com.collective.recommender.RecommenderException;
import com.collective.recommender.SesameVirtuosoRecommender;
import com.collective.recommender.configuration.ConfigurationManager;
import com.collective.recommender.configuration.PermanentSearchStorageConfiguration;
import com.collective.recommender.configuration.RecommendationsStorageConfiguration;
import com.collective.recommender.configuration.RecommenderConfiguration;
import com.collective.recommender.proxy.ranking.Ranker;
import com.collective.recommender.proxy.ranking.RankerException;
import com.collective.recommender.proxy.ranking.WebResourceEnhancedRanker;
import com.collective.recommender.storage.KVSRecommendationsStorage;
import com.collective.recommender.storage.RecommendationsStorage;
import com.collective.recommender.storage.RecommendationsStorageException;
import com.collective.recommender.utils.UserIdParser;
import com.collective.recommender.utils.UserIdParserException;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import org.openrdf.model.vocabulary.XMLSchema;
import tv.notube.commons.storage.alog.DefaultActivityLogImpl;
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
 *
 */
/*TODO: (high) refactor xml nodes names since they are unclear and there could be a duplicate section */
public class Runner {

    private static final Logger logger = Logger.getLogger(Runner.class);

    private static ProfileStore profileStore;

    private static Recommender recommender;

    private static RecommendationsStorage recommendationsStorage;

    private static ConfigurationManager configurationManager;

    private static MyBatisPermanentSearchStorage permanentSearchStorage;

    private static Long permanentSearchesLimit = 10L;

    private static ActivityLog activityLog;

    private static final String RECOMMENDER_NAME = "recommender";

    private static HashMap<String, String> exceptions = new HashMap<String, String>();

    public static void main(String[] args) {
        final String CONFIGURATION = "configuration";

        Options options = new Options();
        options.addOption(CONFIGURATION, true, "XML Configuration file.");
        CommandLineParser commandLineParser = new PosixParser();
        CommandLine commandLine = null;
        if(args.length != 2) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Runner", options);
            System.exit(-1);
        }
        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            logger.error("Error while parsing arguments", e);
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Runner", options);
            System.exit(-1);
        }
        String confFilePath = commandLine.getOptionValue(CONFIGURATION);

        /**
         * Parse the configuration file and instantiates all the needed dependencies
         */
        logger.info("Loading configuration from: '" + confFilePath + "'");
        configurationManager =
                ConfigurationManager.getInstance(confFilePath);

        ProfileStoreConfiguration profileStoreConfiguration 
                = configurationManager.getProfileStoreConfiguration();
        RecommenderConfiguration recommenderConfiguration
                = configurationManager.getRecommenderConfiguration();
        RecommendationsStorageConfiguration recommendationsStorageConfiguration
                = configurationManager.getRecommendationsStorageConfiguration();
        PermanentSearchStorageConfiguration permanentSearchStorageConfiguration
                = configurationManager.getPermanentSearchStorageConfiguration();

        RDFizer rdFizer;
        try {
            rdFizer = getRDFizer();
        } catch (TypeHandlerRegistryException e) {
            final String errMsg = "Error while instantiating the rdf-izer";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }

        profileStore = new SesameVirtuosoProfileStore(profileStoreConfiguration, rdFizer);
        recommender = new SesameVirtuosoRecommender(recommenderConfiguration, rdFizer);
        recommendationsStorage = new KVSRecommendationsStorage(
                recommendationsStorageConfiguration.getProperties()
        );
        permanentSearchStorage = new MyBatisPermanentSearchStorage(permanentSearchStorageConfiguration.getProperties());
        activityLog = new DefaultActivityLogImpl(configurationManager.getActivityLogConfiguration());

        logger.info("ProfileStore, Recommender and RecommendationsStorage, permanentSearchStorage and activityLog correctly instantiated");
        calculateRecommendationsForUsers();
        calculateRecommendationsForProjects();
        calculateRecommendationsForSearches();

        String exceptionsString = stringifyMap(exceptions);
        logger.info("exceptions occurred: \n");        
        logger.info(exceptionsString);
    }

    private static String stringifyMap(HashMap<String, String> exceptions) {
        StringBuffer sb = new StringBuffer();
        for (String key : exceptions.keySet()) {
            String value = exceptions.get(key);
            String concat = "K: '" + key + "' V: '" + value + "'\n";
            sb.append(concat);
        }
        return sb.toString();
    }

    private static void calculateRecommendationsForSearches() {
        List<Search> searches = permanentSearchStorage
                .selectAllPermanentSearches();

        /* for each search, calculate the recommendations */

        URI searchGraph;
        searchGraph = configurationManager.getRecommenderConfiguration()
                        .getIndexes()
                        .get("search");

        URI customAnnotationGraphPrefix =
                configurationManager.getRecommenderConfiguration()
                        .getIndexes()
                        .get("custom-annotations");

        for (Search search : searches) {
            URI searchIdURI = null;
            String searchId = Long.toString(search.getId());

            try {
                searchIdURI = new URI(searchGraph + searchId);
            } catch (URISyntaxException e) {
                logger.error("error when building searchURI: " + e.getMessage());
            }


            calculateResourceRecommendationsForSearch(searchIdURI, search,
                    customAnnotationGraphPrefix);
        }
    }

    private static RDFizer getRDFizer() throws TypeHandlerRegistryException {
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
        return new TypedRDFizer(null, typeHandlerRegistry);
    }

    private static void calculateRecommendationsForProjects() {
		/* use the virtuoso profile store graph index to retrieve all users */

        URI projectGraph;
        projectGraph = configurationManager.getRecommenderConfiguration()
                .getIndexes()
                .get("project");
        List<org.openrdf.model.Resource> projectIdList;

        try {
        	projectIdList = profileStore.getAllTriplesSubjectsFromGraphIndex(projectGraph);
        } catch (ProfileStoreException e) {
            final String errMsg = "Error while getting all Projects from ProfileStorage using graph: '" + projectGraph.toString() + "'";
            logger.error(errMsg, e);
            System.exit(-1);
            return;
        }

        // for each project found
        for (org.openrdf.model.Resource projectIdRes : projectIdList) {
            URI projectId = null;
            try {
                projectId = new URI(projectIdRes.toString());
            } catch (URISyntaxException e) {
                //should never happen
            }

            ProjectProfile profile;

            try {
                profile = profileStore.getProjectProfile(projectId);
            } catch (ProfileStoreException e) {
                final String errMsg = "Error while getting Profile for Project: '" + projectId + "'";
                logger.error(errMsg, e);
                System.exit(-1);
                return;
            }
            calculateResourceRecommandationsForProject(projectId, profile);
            calculateExpertsRecommandations(projectId, profile);
        }		
	}

    private static void calculateExpertsRecommandations(
            URI projectId,
            ProjectProfile projectProfile
    ) {
        logger.info("Calculating Expert users for project: '" + projectId + "'");
        Set<UserProfile> userProfiles;
        try {
            userProfiles = recommender.getExpertUsersForProject(projectProfile);
        } catch (RecommenderException e) {
            final String errMsg = "Error while getting Recommendations for Project: '" + projectId + "'";
            logger.error(errMsg, e);
            System.exit(-1);
            return;
        }
        logger.info("Removing old recommendations for project: '" + projectId + "'");
        try {
            recommendationsStorage.deleteExpertsRecommandationsForProject(projectId);
        } catch (RecommendationsStorageException e) {
            final String errMsg = "Error while removing Reccomendations for Project: '" + projectId + "'";
            logger.error(errMsg, e);
            System.exit(-1);
            return;
        }
        logger.info("Got " + userProfiles.size() + " recommendations");
        logger.info("--- START: PRINTING RECS --- ");
        for (UserProfile userProfile : userProfiles) {
            logger.info(userProfile.getId());
            logger.info(userProfile.getSkills());
        }
        logger.info("--- END: PRINTING RECS --- ");
        try {
            recommendationsStorage.storeExpertsRecommendations(
                    projectId,
                    new ArrayList<UserProfile>(userProfiles)
            );
        } catch (RecommendationsStorageException e) {
            final String errMsg = "Error while storing Recommendations for Project: '" + projectId + "'";
            logger.error(errMsg, e);
            System.exit(-1);
            return;
        }
    }

    private static void calculateResourceRecommandationsForProject(
			URI projectId, ProjectProfile profile) {
		 logger.info("Calculating Resource recommendations for project: '" + projectId + "'");
	        Set<WebResourceEnhanced> resourceRecommendations;
	        try {
	            resourceRecommendations = recommender.getResourceRecommendations(
                        profile
                );
	        } catch (RecommenderException e) {
	            final String errMsg = "Error while getting Recommendations for Project: '" + projectId + "'";
	            logger.error(errMsg, e);
	            System.exit(-1);
	            return;
	        }
	        logger.info("Removing old recommendations for project: '" + projectId + "'");
	        try {
	            recommendationsStorage.deleteResourceRecommendations(projectId);
	        } catch (RecommendationsStorageException e) {
	            final String errMsg = "Error while removing Reccomendations for Project: '" + projectId + "'";
	            logger.error(errMsg, e);
	            System.exit(-1);
	            return;
	        }
	        logger.info("Got " + resourceRecommendations.size() + " recommendations");
	        logger.info("--- START: PRINTING RECS --- ");
	        for (WebResourceEnhanced resource : resourceRecommendations) {
	            logger.info(resource.getUrl());
	            logger.info(resource.getTopics());
	        }
	        logger.info("--- END: PRINTING RECS --- ");

            //TODO: (high) move to proper class!!
            //sort recommendations
            Ranker ranker = new WebResourceEnhancedRanker();
            List<WebResourceEnhanced> recommendationsList = new ArrayList<WebResourceEnhanced>(resourceRecommendations);

            try {
                ranker.rank(recommendationsList);
            } catch (RankerException e) {
                final String errMsg = "Error while sorting Recommendations for Project: '" + projectId + "'";
                logger.error(errMsg, e);
                System.exit(-1);
                return;
            }

	        try {
	            recommendationsStorage.storeResourceRecommendations(
                        projectId,
                        recommendationsList
                );
	        } catch (RecommendationsStorageException e) {
	            final String errMsg = "Error while storing Reccomendations for Project: '" + projectId + "'";
	            logger.error(errMsg, e);
	            System.exit(-1);
	            return;
	        }		
	}

	private static void calculateRecommendationsForUsers() {
		/* use the virtuoso profile store graph index to retrieve all users */

        URI usersGraph;
        usersGraph = configurationManager.getRecommenderConfiguration()
                .getIndexes()
                .get("user");

        List<org.openrdf.model.Resource> userIdList;
        try {
            userIdList = profileStore.getAllTriplesSubjectsFromGraphIndex(usersGraph);
        } catch (ProfileStoreException e) {
            final String errMsg = "Error while getting all Users from ProfileStorage using graph: '" + usersGraph.toString() + "'";
            logger.error(errMsg, e);
            System.exit(-1);
            return;
        }

        // for each user found
        for (org.openrdf.model.Resource userIdRes : userIdList) {

            URI userId = null;
            try {
                userId = new URI(userIdRes.toString());
            } catch (URISyntaxException e) {
                //should never happen
            }

            UserProfile profile;

            try {
                profile = profileStore.getUserProfile(userId);
            } catch (ProfileStoreException e) {
                final String errMsg = "Error while getting Profile for User: '" + userId + "'";
                logger.error(errMsg, e);
                System.exit(-1);
                return;
            }
            calculateResourceRecommendations(userId, profile);
            calculateProjectsRecommendations(userId, profile);
        }
	}

    private static void calculateProjectsRecommendations(URI userId, UserProfile profile) {
        logger.info("Calculating Projects recommendations for user: '" + userId + "'");
        Set<ProjectProfile> projectProfileRecommendations;
        try {
            projectProfileRecommendations = recommender.getProjectRecommendations(
                    profile
            );
        } catch (RecommenderException e) {
            final String errMsg = "Error while getting Recommendations for User: '" + userId + "'";
            logger.error(errMsg, e);
            System.exit(-1);
            return;
        }

        logger.info("Getting old project recommendations for userId: '" + userId + "'");

        Set<ProjectProfile> oldRecommendations = new HashSet<ProjectProfile>();
        List<ProjectProfile> oldRecommendationsList = null;

        try {
            oldRecommendationsList = recommendationsStorage.getProjectRecommendations(userId);
        } catch (RecommendationsStorageException e) {
            final String errMsg = "Error while getting old project Recommendations for User: '" + userId + "'";
            logger.error(errMsg, e);
        }

        if (oldRecommendationsList != null) {
            oldRecommendations.addAll(oldRecommendationsList);
        }

        logger.info("Removing old recommendations for user: '" + userId + "'");
        try {
            recommendationsStorage.deleteProjectProfileRecommendations(userId);
        } catch (RecommendationsStorageException e) {
            final String errMsg = "Error while removing Reccomendations for User: '" + userId + "'";
            logger.error(errMsg, e);
            System.exit(-1);
            return;
        }
        logger.info("Got " + projectProfileRecommendations.size() + " recommendations");
        logger.info("--- START: PRINTING RECS --- ");
        for (ProjectProfile projectProfile : projectProfileRecommendations) {
            logger.info(projectProfile.getId());
            logger.info(projectProfile.getManifestoConcepts());
        }
        logger.info("--- END: PRINTING RECS --- ");

        /* filtering old recommendations from new ones */
        Set<ProjectProfile> newProjectProfileRecommendations =
                new HashSet<ProjectProfile>(projectProfileRecommendations);
        newProjectProfileRecommendations.removeAll(oldRecommendations);

        int newRecommendations = newProjectProfileRecommendations.size();

        if (newRecommendations > 0) {
            /* build informations to log */
            IntegerField newRecommendationsField =
                    new IntegerField("newProjectRecommendations", newRecommendations);
            StringField userIdField =
                    new StringField("userId", userId.toString());
            /* log the list of projects that have been recommended */
            String commaSeparatedProjects = buildJSON(projectProfileRecommendations);
            StringField projectRecommendationsListField =
                    new StringField("projectRecommendationsListJson", commaSeparatedProjects);

            Field[] fields = new Field[3];
            fields[0] = newRecommendationsField;
            fields[1] = userIdField;
            fields[2] = projectRecommendationsListField;

            try {
                activityLog.log(RECOMMENDER_NAME, "new project recommendations for user", fields);
            } catch (ActivityLogException e) {
                logger.error("can't log to activityLogger: " + e.getMessage());
            }
        }

        try {
            recommendationsStorage.storeProjectProfileRecommendations(
                    userId, 
                    new ArrayList<ProjectProfile>(projectProfileRecommendations)
            );
        } catch (RecommendationsStorageException e) {
            final String errMsg = "Error while storing Reccomendations for User: '" + userId + "'";
            logger.error(errMsg, e);
            System.exit(-1);
            return;
        }
    }

    private static void calculateResourceRecommendations(URI userId, UserProfile profile) {
        logger.info("Calculating Resource recommendations for user: '" + userId + "'");
        Set<WebResourceEnhanced> resourceRecommendations;
        try {
            resourceRecommendations = recommender.getResourceRecommendations(
                    profile
            );
        } catch (RecommenderException e) {
            final String errMsg = "Error while getting Recommendations for User: '" + userId + "'";
            logger.error(errMsg, e);
            System.exit(-1);
            return;
        }
        logger.info("Getting old recommendations for user: '" + userId + "'");

        Set<WebResourceEnhanced> oldRecommendations = new HashSet<WebResourceEnhanced>();
        List<WebResourceEnhanced> oldRecommendationsList = null;

        try {
            oldRecommendationsList =
                    recommendationsStorage.getResourceRecommendations(userId);
        } catch (RecommendationsStorageException e) {
            final String errMsg = "Error while getting old Recommendations for User: '" + userId + "'";
            logger.error(errMsg, e);
        }

        if (oldRecommendationsList != null) {
            oldRecommendations.addAll(oldRecommendationsList);
        }

        logger.info("Removing old recommendations for user: '" + userId + "'");
        try {
            recommendationsStorage.deleteResourceRecommendations(userId);
        } catch (RecommendationsStorageException e) {
            final String errMsg = "Error while removing Reccomendations for User: '" + userId + "'";
            logger.error(errMsg, e);
            System.exit(-1);
            return;
        }
        logger.info("Got " + resourceRecommendations.size() + " recommendations for user " + userId);
        logger.info("--- START: PRINTING RECS --- ");
        for (WebResourceEnhanced resource : resourceRecommendations) {
            logger.info(resource.getUrl());
            logger.info(resource.getTopics());
        }
        logger.info("--- END: PRINTING RECS --- ");
        //TODO: (high) move ranking to proper class!!
        //sort list
        Ranker ranker = new WebResourceEnhancedRanker();
        List<WebResourceEnhanced> recommendationsList = new ArrayList<WebResourceEnhanced>(resourceRecommendations);

        try {
            ranker.rank(recommendationsList);
        } catch (RankerException e) {
            final String errMsg = "Error while sorting Recommendations for User: '" + userId + "'";
            logger.error(errMsg, e);
            System.exit(-1);
            return;
        }

        /* filtering old recommendations from new ones */
        Set<WebResourceEnhanced> newResourceRecommendations =
                new HashSet<WebResourceEnhanced>(resourceRecommendations);
        newResourceRecommendations.removeAll(oldRecommendations);

        int newRecommendations = newResourceRecommendations.size();

        if (newRecommendations > 0)
        {
            /* build informations to log */
            IntegerField newRecommendationsField =
                    new IntegerField("newRecommendations", newRecommendations);
            StringField userIdField =
                    new StringField("userId", userId.toString());
            /* log the list of links that have been recommended */
            String commaSeparatedResources = buildJSON(recommendationsList);
            StringField recommendationsListField =
                    new StringField("resourcesRecommendationsListJson", commaSeparatedResources);

            Field[] fields = new Field[3];
            fields[0] = newRecommendationsField;
            fields[1] = userIdField;
            fields[2] = recommendationsListField;

            try {
                activityLog.log(RECOMMENDER_NAME, "new resource recommendations for user", fields);
            } catch (ActivityLogException e) {
                logger.error("can't log to activityLogger: " + e.getMessage());
            }
        }

        try {
            recommendationsStorage.storeResourceRecommendations(
                    userId,
                    recommendationsList
            );
        } catch (RecommendationsStorageException e) {
            final String errMsg = "Error while storing Reccomendations for User: '" + userId + "'";
            logger.error(errMsg, e);
            System.exit(-1);
            return;
        }
    }

    private static void calculateResourceRecommendationsForSearch(URI searchId,
                                                                  Search search,
                                                                  URI customAnnotationsGraphPrefix) {

        Set<WebResourceEnhanced> resourceRecommendations =
                                             new HashSet<WebResourceEnhanced>();

        /* if search contains some common concepts,
         * calculate its recommendations */

        if (search.getCommonUris().size() > 0) {

            logger.info("Calculating Resource recommendations for " +
                    "common concepts of search: '" + searchId + "'");

            try {
                resourceRecommendations = recommender.getResourceRecommendations(
                        search.getCommonUris()
                );
            } catch (RecommenderException e) {
                final String errMsg = "Error while getting Recommendations for Search: '" + searchId + "'";
                logger.error(errMsg, e);
                System.exit(-1);
                return;
            }
        }

        /* if search contains some custom defined concepts,
         * calculate its recommendations */

        if (search.getCustomUris().size() > 0) {
            logger.info("Calculating custom concepts resource " +
                        "recommendations for search: '" + searchId + "'");

        /* get the userId in order for the runner to be able to search only the
         * user's graph with its custom concepts resources' annotations.
         * TODO: med embedding information about the resource in its name is really BAD */
            Long userId = 0L;
            UserIdParser userIdParser = new UserIdParser();
            URI customConceptUrl = search.getCustomUris().get(0).getUrl();

            try {
                userId = userIdParser.getUserId(customConceptUrl);
            } catch (UserIdParserException e) {
                final String errMsg = "Error while parsing " +
                        "userId owner from url: '"
                        + customConceptUrl + "'";
            logger.error(errMsg, e);
            System.exit(-1);
            }

            logger.debug("customConceptUrl: " + customConceptUrl);
            logger.debug("parsed owner of custom concept - userId: " + userId);

            try {
                resourceRecommendations.addAll(
                        recommender.getCustomConceptsResourceRecommendations(
                                search.getCustomUris(),
                                userId,
                                customAnnotationsGraphPrefix));
            } catch (RecommenderException e) {
            //TODO what if the graph with custom annotations doesnt exist?
                final String errMsg = "Error while getting custom concepts " +
                        "Recommendations for Search: '" + searchId + "' in " +
                        "annotations graph '"
                        + customAnnotationsGraphPrefix.toString() + "'";
                logger.warn(errMsg, e);
                //store exceptions messages
                final String graphId = customAnnotationsGraphPrefix.toString() + userId;
                exceptions.put(graphId, e.toString());
            }

            logger.info("Calculated '" + resourceRecommendations.size()
                    + "' recommendations for search '" + searchId + "' " +
                    "from annotations graph '" + customAnnotationsGraphPrefix.toString()
                    + userId + "'");
        }

        logger.info("Getting old recommendations for search: '" + searchId + "'");

        Set<WebResourceEnhanced> oldRecommendations = new HashSet<WebResourceEnhanced>();
        List<WebResourceEnhanced> oldRecommendationsList = null;

        try {
            oldRecommendationsList = recommendationsStorage.getResourceRecommendations(searchId);
        } catch (RecommendationsStorageException e) {
            final String errMsg = "Error while getting old Recommendations for Search: '" + searchId + "'";
            logger.error(errMsg, e);
        }

        if (oldRecommendationsList != null) {
            oldRecommendations.addAll(oldRecommendationsList);
        }

        logger.info("Removing old recommendations for search: '" + searchId + "'");
        try {
            recommendationsStorage.deleteResourceRecommendations(searchId);
        } catch (RecommendationsStorageException e) {
            final String errMsg = "Error while removing " +
                    "Recommendations for search: '" + searchId + "'";
            logger.error(errMsg, e);
            System.exit(-1);
            return;
        }
        logger.info("Got " + resourceRecommendations.size() + " recommendations");
        logger.info("--- START: PRINTING RECS --- ");
        for (WebResourceEnhanced resource : resourceRecommendations) {
            logger.info(resource.getUrl());
            logger.info(resource.getTopics());
        }
        logger.info("--- END: PRINTING RECS --- ");
        //TODO: (high) move ranking to proper class!!
        //sort list
        Ranker ranker = new WebResourceEnhancedRanker();
        List<WebResourceEnhanced> recommendationsList =
                new ArrayList<WebResourceEnhanced>(resourceRecommendations);

        try {
            ranker.rank(recommendationsList);
        } catch (RankerException e) {
            final String errMsg = "Error while sorting Recommendations for Search: '" + searchId + "'";
            logger.error(errMsg, e);
            System.exit(-1);
            return;
        }

        /* filtering old recommendations from new ones */
        Set<WebResourceEnhanced> newResourceRecommendations =
                new HashSet<WebResourceEnhanced>(resourceRecommendations);
        newResourceRecommendations.removeAll(oldRecommendations);

        int newRecommendations = newResourceRecommendations.size();

        //log activity
        if (newRecommendations > 0) {
            logRecommendationActivity(searchId, recommendationsList, newRecommendations);
        }

        try {
            recommendationsStorage.storeResourceRecommendations(
                    searchId,
                    recommendationsList
            );
        } catch (RecommendationsStorageException e) {
            final String errMsg = "Error while storing " +
                    "Recomendations for Search: '" + searchId + "'";
            logger.error(errMsg, e);
            System.exit(-1);
            return;
        }
        logger.info("stored " + recommendationsList.size() + " recommendations to '" + searchId.toString() + "' search Id");
    }

    private static void logRecommendationActivity(
                            URI searchId,
                            List<WebResourceEnhanced> recommendationsList,
                            int newRecommendations) {
        /* build informations to log */
        IntegerField newRecommendationsField =
                new IntegerField("newRecommendations", newRecommendations);
        StringField searchIdField =
                new StringField("searchId", searchId.toString());
        /* log the list of links that have been recommended */
        String commaSeparatedResources = buildJSON(recommendationsList);
        StringField recommendationsListField =
                new StringField("searchRecommendationsListJson", commaSeparatedResources);

        Field[] recAmountFields = new Field[3];
        recAmountFields[0] = newRecommendationsField;
        recAmountFields[1] = searchIdField;
        recAmountFields[2] = recommendationsListField;

        try {
            activityLog.log(RECOMMENDER_NAME, "new resource recommendations for search",
                    recAmountFields);
        } catch (ActivityLogException e) {
            logger.error("can't log to activityLogger: " + e.getMessage());
        }
    }

    /* encodes a list of links in JSON */
    private static String buildJSON(List<WebResourceEnhanced> recommendationsList) {

        StringBuilder builder = new StringBuilder();
        builder.append("[");

        String prefix = "";

        for (WebResourceEnhanced webResource : recommendationsList) {
            builder.append(prefix);
            prefix = ",";
            builder.append("\"" + webResource.getUrl().toString() + "\"");
        }
        builder.append("]");

        String webResourcesList = builder.toString();

        return webResourcesList;
    }

    /* encodes a list of project ids in JSON */
    private static String buildJSON(Set<ProjectProfile> projectProfileRecommendations) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        String prefix = "";

        for (ProjectProfile projectProfile : projectProfileRecommendations) {
            builder.append(prefix);
            prefix = ",";
            builder.append("\"" + projectProfile.getId().toString() + "\"");
        }

        builder.append("]");

        String projectProfileList = builder.toString();

        return projectProfileList;
    }
}
