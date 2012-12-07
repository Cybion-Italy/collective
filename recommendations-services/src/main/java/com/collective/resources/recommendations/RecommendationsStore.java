package com.collective.resources.recommendations;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.ProjectProfile;
import com.collective.model.profile.UserProfile;
import com.collective.recommender.storage.KVSRecommendationsStorage;
import com.collective.recommender.storage.RecommendationsStorage;
import com.collective.recommender.storage.RecommendationsStorageException;

import java.net.URI;
import java.util.List;
import java.util.Properties;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class RecommendationsStore {

    private RecommendationsStorage recommendationsStorage;

    public RecommendationsStore(Properties properties) {
        recommendationsStorage = new KVSRecommendationsStorage(properties);
    }

    public List<WebResourceEnhanced> getResourceRecommendationsForUser(
            URI userId,
            ResourceRecommendationType type)
            throws RecommendationsStoreException {
        // TODO (high) should discriminate on the rec types
        try {
            return recommendationsStorage.getResourceRecommendations(userId);
        } catch (RecommendationsStorageException e) {
            final String errMsg = "Error while getting resource recommendations for user '" + userId + "'";
            throw new RecommendationsStoreException(errMsg, e);
        }
    }

    public List<ProjectProfile> getProjectRecommendationsForUser(URI userId)
            throws RecommendationsStoreException {
        try {
            return recommendationsStorage.getProjectRecommendations(userId);
        } catch (RecommendationsStorageException e) {
            final String errMsg = "Error while getting projects recommendations for user '" + userId + "'";
            throw new RecommendationsStoreException(errMsg, e);
        }
    }

    public List<WebResourceEnhanced> getResourceRecommendationsForProject(URI projectURI,
                                                                          ResourceRecommendationType webmagazine)
        throws RecommendationsStoreException {
        // TODO (high) should discriminate on the rec types
        try {
            return recommendationsStorage.getResourceRecommendations(projectURI);
        } catch (RecommendationsStorageException e) {
            final String errMsg = "Error while getting resource recommendations for project '" + projectURI + "'";
            throw new RecommendationsStoreException(errMsg, e);
        }
    }

    public List<UserProfile> getExpertsRecommandationsForProject(URI projectURI)
            throws RecommendationsStoreException {
        try {
            return recommendationsStorage.getExpertsRecommendations(projectURI);
        } catch (RecommendationsStorageException e) {
            final String errMsg = "Error while getting resource recommendations for project '" 
                    + projectURI + "'";
            throw new RecommendationsStoreException(errMsg, e);
        }
    }

    public List<WebResourceEnhanced> getResourceRecommendationsForSearch(URI searchURI,
                                                                         ResourceRecommendationType webmagazine)
            throws RecommendationsStoreException {
        try {
            return recommendationsStorage.getResourceRecommendations(searchURI);
        }   catch (RecommendationsStorageException e) {
            final String errMsg = "Error while getting resource recommendations " +
                    "for search '" + searchURI + "'";
            throw new RecommendationsStoreException(errMsg, e);
        }
    }

    public List<WebResourceEnhanced> getResourceRecommendationsForShortTermProfile(URI userURI) throws RecommendationsStoreException {

        try {
            return recommendationsStorage.getShortTermResourceRecommendations(userURI);
        } catch (RecommendationsStorageException e) {
            final String errMsg = "Error while getting short term profile resource recommendations " +
                    "for user '" + userURI + "'";
            throw new RecommendationsStoreException(errMsg, e);
        }
    }
}
