package com.collective.recommender;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.ProjectProfile;
import com.collective.model.profile.UserProfile;
import com.collective.permanentsearch.model.LabelledURI;
import com.collective.recommender.configuration.RecommenderConfiguration;
import com.collective.recommender.proxy.*;
import com.collective.recommender.proxyimpl.SesameVirtuosoSparqlProxy;
import com.collective.recommender.proxyimpl.filtering.ProjectProfileFilter;
import com.collective.recommender.proxyimpl.filtering.UserProfileFilter;
import com.collective.recommender.proxyimpl.filtering.WebResourceEnhancedFilter;
import com.collective.recommender.proxyimpl.ranking.ProjectProfileRanker;
import com.collective.recommender.proxyimpl.ranking.UserProfileRanker;
import com.collective.recommender.proxyimpl.ranking.WebResourceEnhancedRanker;
import org.apache.log4j.Logger;
import org.nnsoft.be3.Be3;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

//TODO: (high) refactor APIs to return Lists instead of Sets, to maintain sorting order
public class SesameVirtuosoRecommender implements Recommender {

    private static final Logger LOGGER = Logger.getLogger(SesameVirtuosoRecommender.class);

    private RecommenderConfiguration recommenderConfiguration;

    private SparqlProxy sparqlProxy;

    private static String RESOURCES_TO_USERS = "CONSTRUCT { ?s ?p ?o. ?q ?r ?t. } \n" +
            "FROM <%s>\n" +
            "WHERE {\n" +
            "    ?s a <http://collective.com/resources/web>.\n" +
            "    ?s <http://purl.org/dc/terms/subject> <%s>.\n" +
            "    ?s ?p ?o.\n" +
            "    ?s <http://collective.com/resources/web/hasSourceRSS> ?q.\n" +
            "    ?q ?r ?t.\n" +
            "}";

    private static String PROJECTS_TO_USERS = "CONSTRUCT { ?s ?p ?o. ?q ?r ?t. } \n" +
            "WHERE { \n" +
            "    ?s a <http://collective.com/profile/project>. \n" +
            "    ?s <http://collective.com/ProjectProfile#manifestoConcepts> <%s>. \n" +
            "    ?user a <http://xmlns.com/foaf/0.1/Person> .\n" +
            "    ?user <http://purl.org/ontology/cco/core#skill> <%s>.\n" +
            "    ?s ?p ?o. \n" +
            "    ?s <http://collective.com/ProjectProfile#projectInvolvements> ?q. \n" +
            "    ?q <http://collective.com/ProjectInvolvement/user> ?involvedUser .\n" +
            "    ?q ?r ?t. \n" +
            "    FILTER(str(?involvedUser) != str(?user)) .\n" +
            "}";

    private static String USERS_TO_PROJECT = "SELECT ?s ?p ?o\n" +
            "WHERE { \n" +
            "    ?s a <http://xmlns.com/foaf/0.1/Person> .\n" +
            "     ?s <http://purl.org/ontology/cco/core#skill> ?skill .\n" +
            "     <%s> <http://collective.com/ProjectProfile#manifestoConcepts> ?skill .\n" +
            "     ?s ?p ?o .\n" +
            "     <%s> <http://collective.com/ProjectProfile#projectInvolvements> ?i .\n" +
            "     ?i <http://collective.com/ProjectInvolvement/user> ?user.\n" +
            "     GRAPH ?user {\n" +
            "         ?s1 a <http://xmlns.com/foaf/0.1/Person> .\n" +
            "     }\n" +
            "     FILTER (?s1 != ?s)\n" +
            "}";

    //for testing
    public static String RESOURCES_CUSTOM_CONCEPTS_PERMANENT_SEARCH =
            "CONSTRUCT { ?s ?p ?o. ?q ?r ?t. } \n" +
            "FROM <%s>\n" +
            "WHERE {\n" +
            "    ?s a <http://collective.com/resources/web>.\n" +
            "    ?s <http://purl.org/dc/terms/subject> <%s>.\n" +
            "    ?s ?p ?o.\n" +
            "    ?s <http://collective.com/resources/web/hasSourceRSS> ?q.\n" +
            "    ?q ?r ?t.\n" +
            "}";

    public SesameVirtuosoRecommender(
            RecommenderConfiguration recommenderConfiguration,
            Be3 rdfizer
    ) {
        this.recommenderConfiguration = recommenderConfiguration;
        // TODO: take the query patterns from the configuration
        // instead of having them static
        this.sparqlProxy = new SesameVirtuosoSparqlProxy(recommenderConfiguration, rdfizer);
        // here configure the sparql proxy
        try {
            this.sparqlProxy.registerQuery(
                    "resources-to-users",
                    RESOURCES_TO_USERS, 
                    SparqlQuery.TYPE.GRAPH,
                    WebResourceEnhancedFilter.class,
                    WebResourceEnhancedRanker.class
            );
        } catch (SparqlProxyException e) {
            throw new RuntimeException("Error while adding '" + RESOURCES_TO_USERS + "' query");
        }
        try {
            this.sparqlProxy.registerQuery(
                    "projects-to-users",
                    PROJECTS_TO_USERS,
                    SparqlQuery.TYPE.GRAPH,
                    ProjectProfileFilter.class,
                    ProjectProfileRanker.class
            );
        } catch (SparqlProxyException e) {
            throw new RuntimeException("Error while adding '" + PROJECTS_TO_USERS + "' query");
        }
        try {
            // we use the same query template of users
            this.sparqlProxy.registerQuery(
                    "resources-to-projects",
                    RESOURCES_TO_USERS,
                    SparqlQuery.TYPE.GRAPH,
                    WebResourceEnhancedFilter.class,
                    WebResourceEnhancedRanker.class
            );
        } catch (SparqlProxyException e) {
            throw new RuntimeException("Error while adding '" + RESOURCES_TO_USERS + "' query");
        }
        try {
            this.sparqlProxy.registerQuery(
                    "users-to-project",
                    USERS_TO_PROJECT,
                    SparqlQuery.TYPE.TUPLE,
                    UserProfileFilter.class,
                    UserProfileRanker.class
            );
        } catch (SparqlProxyException e) {
            throw new RuntimeException("Error while adding '" + RESOURCES_TO_USERS + "' query");
        }
        try {
            this.sparqlProxy.registerQuery(
                    "resources-custom-concepts-to-permanent-search",
                    RESOURCES_CUSTOM_CONCEPTS_PERMANENT_SEARCH,
                    SparqlQuery.TYPE.GRAPH,
                    WebResourceEnhancedFilter.class,
                    WebResourceEnhancedRanker.class
            );
        } catch (SparqlProxyException e) {
            throw new RuntimeException("Error while adding '"
                    + RESOURCES_CUSTOM_CONCEPTS_PERMANENT_SEARCH + "' query");
        }

    }

    public RecommenderConfiguration getConfiguration() {
        if(recommenderConfiguration == null)
            throw new IllegalStateException("uhm it seems the ProfileStore has not been configured yet");
        return recommenderConfiguration;
    }

    @Override
    public Set getResourceRecommendations(UserProfile profile)
            throws RecommenderException {
        if( profile == null )
            throw new IllegalArgumentException("parameter Profile cannot be null");
        Set<WebResourceEnhanced> heap = new HashSet<WebResourceEnhanced>();
        for(URI uri : profile.getInterests()) {
            try {
                List resources = this.sparqlProxy.getList(
                        "resources-to-users",
                        WebResourceEnhanced.class,
                        this.recommenderConfiguration.getDataGraph().toString(),
                        uri.toString()
                );
                //TODO: (high) using a set removes duplicates but loses sort.
                // refactor APIs to return Lists
                heap.addAll(resources);
            } catch (SparqlProxyException e) {
                throw new RecommenderException("", e);
            }
        }
        return heap;
    }

    @Override
    public Set getProjectRecommendations(UserProfile profile)
            throws RecommenderException {
        if( profile == null )
            throw new IllegalArgumentException("parameter Profile cannot be null");
        Set<ProjectProfile> heap = new HashSet<ProjectProfile>();
        for(URI uri : profile.getInterests()) {
            try {
                List resources = this.sparqlProxy.getList(
                        "projects-to-users",
                        ProjectProfile.class,
                        uri.toString(),
                        uri.toString()
                );
                heap.addAll(resources);
            } catch (SparqlProxyException e) {
                throw new RecommenderException("", e);
            }
        }
        return heap;
    }

    @Override
    public Set getResourceRecommendations(ProjectProfile profile)
            throws RecommenderException {
        if (profile == null)
            throw new IllegalArgumentException("parameter Profile cannot be null");
        Set<WebResourceEnhanced> heap = new HashSet<WebResourceEnhanced>();
        for(URI uri : profile.getManifestoConcepts()) {
            try {
                List resources = this.sparqlProxy.getList(
                        "resources-to-projects",
                        WebResourceEnhanced.class,
                        this.recommenderConfiguration.getDataGraph().toString(),
                        uri.toString()
                );
                heap.addAll(resources);
            } catch (SparqlProxyException e) {
                throw new RecommenderException("", e);
            }
        }
        return heap;
    }

    @Override
    public Set<WebResourceEnhanced> getResourceRecommendations(
            List<LabelledURI> commonConcepts)
            throws RecommenderException {
        if (commonConcepts == null)
            throw new IllegalArgumentException("parameter commonConcepts cannot be null");
        Set<WebResourceEnhanced> heap = new HashSet<WebResourceEnhanced>();
        for(LabelledURI labelledURI : commonConcepts) {
            try {
                List resources = this.sparqlProxy.getList(
                        "resources-to-projects",
                        WebResourceEnhanced.class,
                        this.recommenderConfiguration.getDataGraph().toString(),
                        labelledURI.getUrl().toString()
                );
                heap.addAll(resources);
            } catch (SparqlProxyException e) {
                throw new RecommenderException("", e);
            }
        }
        return heap;
    }

    @Override
    public Set<WebResourceEnhanced>
                       getCustomConceptsResourceRecommendations(
                                               List<LabelledURI> customConcepts,
                                               Long userId,
                                               URI customAnnotationsGraphPrefix)
            throws RecommenderException {
        if (customConcepts == null)
            throw new IllegalArgumentException("parameter customConcepts cannot be null");

        Set<WebResourceEnhanced> heap = new HashSet<WebResourceEnhanced>();
        for(LabelledURI customConceptUri : customConcepts) {
            try {
                List resources = this.sparqlProxy.getList(
                        "resources-custom-concepts-to-permanent-search",
                        WebResourceEnhanced.class,
                        customAnnotationsGraphPrefix.toString() + userId,
                        customConceptUri.getUrl().toString()
                );
                heap.addAll(resources);
            } catch (SparqlProxyException e) {
                throw new RecommenderException("", e);
            }
        }
        return heap;
    }

    @Override
    public Set<UserProfile> getExpertUsersForProject(ProjectProfile projectProfile)
            throws RecommenderException {
        if (projectProfile == null)
            throw new IllegalArgumentException("parameter Profile cannot be null");
        Set<UserProfile> heap = new HashSet<UserProfile>();
        Long projectId = projectProfile.getId();
        try {
            List users = this.sparqlProxy.getList(
                    "users-to-project",
                    UserProfile.class,
                    (new URI("http://collective.com/profile/project/" + projectId)).toString(),
                    (new URI("http://collective.com/profile/project/" + projectId)).toString()
            );
            heap.addAll(users);
        } catch (SparqlProxyException e) {
            throw new RecommenderException("", e);
        } catch (URISyntaxException e) {
            throw new RecommenderException("", e);            
        }
        return heap;
    }

    @Override
    public List<UserProfile> getSimilarUsers(UserProfile profile)
            throws RecommenderException {
        throw new UnsupportedOperationException("niy");
    }
    
}
