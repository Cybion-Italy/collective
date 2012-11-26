package com.collective.resources;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.ProjectProfile;
import com.collective.model.profile.UserProfile;
import com.collective.resources.recommendations.RecommendationsStoreException;
import com.collective.resources.recommendations.ResourceRecommendationType;
import com.sun.jersey.api.core.InjectParam;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
//TODO: refactor these string paths and move it as a constant into AbstractRecommendation?
@Path("/userrecommendation")
@Produces(MediaType.APPLICATION_JSON)
public class UserRecommendation implements AbstractRecommendation {

    public UserRecommendation() {}

    //TODO: check tomcat behaviour with static loggers
    private static Logger logger = Logger.getLogger(UserRecommendation.class);

    // TODO: this should be plugged via configuration, being the same as the profiler-runner configuration
    public static String USER_BASE_URI = "http://collective.com/profile/user/";

    /* Dynamic profile base URIs */
    public static String SHORT_TERM_USER_PROFILE_BASE_URI =
            "http://collective.com/profile/user/short-term/";

    public static String LONG_TERM_USER_PROFILE_BASE_URI =
            "http://collective.com/profile/user/long-term/";


    @InjectParam
    private InstanceManager instanceManager;

    /* used by iCommunity PHP client */
    @GET
    @Path(WEB_MAGAZINES + "/{userId}/{maxItems}")
    public List<WebResourceEnhanced> getWebMagazineArticleRecommendations(
            @PathParam("userId") String userId,
            @PathParam("maxItems") int maxItems
    ) {
        /* should return serializations of URLArticleResource */
        logger.debug("got userId: " + userId);

        instanceManager.getLimitChecker().checkRequestedItemsAmount(maxItems);

        // build the URI identifier for user
        URI uriUserId;
        try {
            uriUserId = new URI(USER_BASE_URI + userId);
        } catch (URISyntaxException e) {
            final String errMsg = "UserId: '" + userId + "' is not well formed";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg);
        }

        List<WebResourceEnhanced> webResources = new ArrayList<WebResourceEnhanced>();
        try {
            webResources.addAll(instanceManager.getRecommendationsStore().getResourceRecommendationsForUser(
                    uriUserId,
                    ResourceRecommendationType.WEBMAGAZINE
            ));
        } catch (RecommendationsStoreException e) {
            final String errMsg = "Error while getting recommendations for user: '" + uriUserId + "'";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg);
        }

        if (maxItems < webResources.size()) {
            List<WebResourceEnhanced> cutWebResources = new ArrayList<WebResourceEnhanced>();
            cutWebResources.addAll(webResources.subList(0, maxItems));
            return cutWebResources;
        }

        return webResources;
    }

    @GET
    @Path(RUMOURS + "/{userId}/{maxItems}")
    public List<WebResourceEnhanced> getRumoursRecommendations(
            @PathParam("userId") String userId,
            @PathParam("maxItems") int maxItems
    ) {
        /* should return serializations of URLArticleResource */
        logger.debug("got userId: " + userId);

        instanceManager.getLimitChecker().checkRequestedItemsAmount(maxItems);

        // build the URI identifier for user
        URI uriUserId;
        try {
            uriUserId = new URI(USER_BASE_URI + userId);
        } catch (URISyntaxException e) {
            final String errMsg = "UserId: '" + userId + "' is not well formed";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg);
        }

        List<WebResourceEnhanced> webResources = new ArrayList<WebResourceEnhanced>();
        try {
            webResources.addAll(instanceManager.getRecommendationsStore().getResourceRecommendationsForUser(
                    uriUserId,
                    ResourceRecommendationType.RUMOURS
            ));
        } catch (RecommendationsStoreException e) {
            final String errMsg = "Error while getting recommendations for user: '" + uriUserId + "'";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg);
        }

        if (maxItems < webResources.size()) {
            List<WebResourceEnhanced> cutWebResources = new ArrayList<WebResourceEnhanced>();
            cutWebResources.addAll(webResources.subList(0, maxItems));
            return cutWebResources;
        }

        return webResources;
    }

    @GET
    @Path(USERS + "/{userId}/{maxItems}")
    public List<UserProfile> getExpertsRecommendations(
            @PathParam("userId") String userId,
            @PathParam("maxItems") int maxItems) {
        /* should return serializations of URLArticleResource */

        logger.debug("got userId: " + userId);

        instanceManager.getLimitChecker().checkRequestedItemsAmount(maxItems);

        throw new UnsupportedOperationException("not implemented yet");
    }

    @GET
    @Path(PROJECTS + "/{userId}/{maxItems}")
    public List<ProjectProfile> getSuitableProjects(
            @PathParam("userId") String userId,
            @PathParam("maxItems") int maxItems
    ) {
        /* should return serializations of URLArticleResource */
        logger.debug("got userId: " + userId);

        instanceManager.getLimitChecker().checkRequestedItemsAmount(maxItems);

        // build the URI identifier for user
        URI uriUserId;
        try {
            uriUserId = new URI(USER_BASE_URI + userId);
        } catch (URISyntaxException e) {
            final String errMsg = "UserId: '" + userId + "' is not well formed";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg);
        }
        logger.debug("getting projects for user: " + uriUserId);

        List<ProjectProfile> projects = new ArrayList<ProjectProfile>();
        try {
            projects.addAll(instanceManager.getRecommendationsStore()
                    .getProjectRecommendationsForUser(uriUserId));
        } catch (RecommendationsStoreException e) {
            final String errMsg = "Error while getting recommendations for user: '" + uriUserId + "'";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg);
        }
        if (maxItems < projects.size()) {
            List<ProjectProfile> cutProjects = new ArrayList<ProjectProfile>();
            cutProjects.addAll(projects.subList(0, maxItems));
            return cutProjects;
        }
        logger.debug("retrieved '" + projects.size() + "' projects for user '" + uriUserId  + "'");
        for(ProjectProfile projectProfile : projects) {
            logger.debug(projectProfile);
        }
        return projects;
    }
}
