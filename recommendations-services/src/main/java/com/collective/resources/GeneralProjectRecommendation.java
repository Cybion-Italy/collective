package com.collective.resources;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.UserProfile;
import com.collective.resources.recommendations.RecommendationsStore;
import com.collective.resources.recommendations.RecommendationsStoreException;
import com.collective.resources.recommendations.ResourceRecommendationType;
import com.sun.jersey.api.core.InjectParam;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
@Path("/generalprojectrecommendation")
@Produces(MediaType.APPLICATION_JSON)
public class GeneralProjectRecommendation implements AbstractRecommendation {

    private static Logger logger = Logger.getLogger(GeneralProjectRecommendation.class);

     // TODO: this should be plugged via configuration, being the same as the profiler-runner configuration
    private static String PROJECT_BASE_URI = "http://collective.com/profile/project/";

    @InjectParam
    private InstanceManager instanceManager;

    public GeneralProjectRecommendation() {}

    public List<WebResourceEnhanced> getWebMagazineArticleRecommendationsNew(String id, int maxItems) {
        throw new UnsupportedOperationException("NIY");
    }

    @GET
    @Path(WEB_MAGAZINES + "/{projectId}/{maxItems}")
    public List<WebResourceEnhanced> getWebMagazineArticleRecommendations(
            @PathParam("projectId") String projectId,
            @PathParam("maxItems") int maxItems) {
        logger.debug("got projectId: " + projectId);

        instanceManager.getLimitChecker().checkRequestedItemsAmount(maxItems);

        throw new UnsupportedOperationException("niy");
    }

    @GET
    @Path(RUMOURS + "/{projectId}/{maxItems}")
    public List<WebResourceEnhanced> getRumoursRecommendations(
            @PathParam("projectId") String projectId,
            @PathParam("maxItems") int maxItems) {

        instanceManager.getLimitChecker().checkRequestedItemsAmount(maxItems);

        URI projectURI;
        try {
            projectURI = new URI(PROJECT_BASE_URI + projectId);
        } catch (URISyntaxException e) {
            final String errMsg = "ProjectId: '" + projectId + "' and its namespace are not well formed";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg);
        }

        List<WebResourceEnhanced> webResources = new ArrayList<WebResourceEnhanced>();
        try {
            webResources.addAll(instanceManager.getRecommendationsStore().getResourceRecommendationsForProject(
                    projectURI,
                    ResourceRecommendationType.WEBMAGAZINE
            ));
        } catch (RecommendationsStoreException e) {
            final String errMsg = "Error while getting recommendations for project: '" + projectURI + "'";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg);
        }

        if (maxItems < webResources.size()) {
            List<WebResourceEnhanced> cutResources = new ArrayList<WebResourceEnhanced>();
            cutResources.addAll(webResources.subList(0, maxItems));
            return cutResources;
        }

        return webResources;
    }


    @GET
    @Path(USERS + "/{projectId}/{maxItems}")
    public List<UserProfile> getExpertsRecommendations(@PathParam("projectId") String projectId,
                                            @PathParam("maxItems") int maxItems) {

        instanceManager.getLimitChecker().checkRequestedItemsAmount(maxItems);
        URI projectIdUri;
        try {
            projectIdUri = new URI(PROJECT_BASE_URI + projectId);
        } catch (URISyntaxException e) {
            final String errMsg = "ProjectId: '" + projectId + "' is not well formed";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg);
        }
        RecommendationsStore recommendationsStore = instanceManager.getRecommendationsStore();
        List<UserProfile> experts = new ArrayList<UserProfile>();
        try {
            experts.addAll(recommendationsStore.getExpertsRecommandationsForProject(projectIdUri));
        } catch (RecommendationsStoreException e) {
            final String errMsg = "ProjectId: '" + projectId + "' is not well formed";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg);
        }

        if (maxItems < experts.size()) {
            List<UserProfile> cutExperts = new ArrayList<UserProfile>();
            cutExperts.addAll(experts.subList(0, maxItems));
            return cutExperts;
        }

        return experts;
    }
}
