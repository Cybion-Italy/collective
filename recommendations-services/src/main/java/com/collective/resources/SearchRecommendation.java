package com.collective.resources;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.UserProfile;
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
 * Answers to search profiles requests
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
@Path("/searchrecommendation")
@Produces(MediaType.APPLICATION_JSON)
public class SearchRecommendation implements AbstractRecommendation {

    private static Logger logger = Logger.getLogger(SearchRecommendation.class);

     // TODO (high): this should be plugged in a configuration,
     // being the same as the profiler-runner configuration
    private static String SEARCH_BASE_URI = "http://collective.com/profile/search/";

    @InjectParam
    private InstanceManager instanceManager;

    public SearchRecommendation() {}

    @GET
    @Path(WEB_MAGAZINES + "/{searchId}/{maxItems}")
    public List<WebResourceEnhanced> getWebMagazineArticleRecommendations(
            @PathParam("searchId")String searchId,
            @PathParam("maxItems")int maxItems) {

        logger.debug("got searchId: " + searchId);

        URI searchURI;
        try {
            searchURI = new URI(SEARCH_BASE_URI + searchId);
        } catch (URISyntaxException e) {
            final String errMsg = "SearchId: '" + searchId
                    + "' and its namespace are not well formed";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg);
        }

        List<WebResourceEnhanced> webResources = new ArrayList<WebResourceEnhanced>();
        try {
            webResources.addAll(instanceManager.getRecommendationsStore()
                    .getResourceRecommendationsForSearch(
                    searchURI,
                    ResourceRecommendationType.WEBMAGAZINE
            ));
        } catch (RecommendationsStoreException e) {
            final String errMsg = "Error while getting recommendations for search: '"
                    + searchURI + "'";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg);
        }
        return webResources;
    }

    public List<WebResourceEnhanced> getRumoursRecommendations(String id,
                                                               int maxItems) {
        throw new UnsupportedOperationException("not implemented yet");  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<UserProfile> getExpertsRecommendations(String id,
                                                       int maxItems) {
        throw new UnsupportedOperationException("not implemented yet");  //To change body of implemented methods use File | Settings | File Templates.
    }
}
