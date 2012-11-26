package com.collective.resources;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.UserProfile;
import com.sun.jersey.api.core.InjectParam;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
@Path("/personalizedprojectrecommendation")
public class PersonalizedProjectRecommendation implements AbstractRecommendation {

    private static Logger logger = Logger.getLogger(PersonalizedProjectRecommendation.class);

    private static String PROJECT_BASE_URI = "http://collective.com/profile/project/";

    @InjectParam
    private InstanceManager instanceManager;

    public PersonalizedProjectRecommendation() {}

    @GET
    @Produces("text/plain")
    @Path(WEB_MAGAZINES + "/{userId}/{projectId}/{maxItems}")
    public String getWebMagazineArticleRecommendations(@PathParam("userId") String userId,
                                                       @PathParam("projectId") String projectId,
                                                       @PathParam("maxItems") int maxItems) {
        logger.debug("got userId " + userId + ", projectId: " + projectId);

        instanceManager.getLimitChecker().checkRequestedItemsAmount(maxItems);
        throw new UnsupportedOperationException("not implemented yet");
    }

    @GET
    @Produces("text/plain")
    @Path(RUMOURS + "/{userId}/{projectId}/{maxItems}")
    public String getRumoursRecommendations(@PathParam("userId") String userId,
                                            @PathParam("projectId") String projectId,
                                            @PathParam("maxItems") int maxItems) {
        logger.debug("got userId " + userId + ", projectId: " + projectId);

        instanceManager.getLimitChecker().checkRequestedItemsAmount(maxItems);
        throw new UnsupportedOperationException("not implemented yet");
    }

    @GET
    @Produces("text/plain")
    @Path(USERS + "/{userId}/{projectId}/{maxItems}")
    public String getExpertsRecommendations(
            @PathParam("userId") String userId,
            @PathParam("projectId") String projectId,
            @PathParam("maxItems") int maxItems) {
        logger.debug("got userId " + userId + ", projectId: " + projectId);
        instanceManager.getLimitChecker().checkRequestedItemsAmount(maxItems);

        throw new UnsupportedOperationException("not implemented yet");
    }

    public List<WebResourceEnhanced> getWebMagazineArticleRecommendations(String id, int maxItems) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public List<WebResourceEnhanced> getRumoursRecommendations(String id, int maxItems) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @GET
    @Produces("application/json")
    @Path(PROJECTS + "/{projectId}/{maxItems}")
    public List<UserProfile> getExpertsRecommendations(
            @PathParam("projectId") String projectId,
            @PathParam("maxItems") int maxItems
    ) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
