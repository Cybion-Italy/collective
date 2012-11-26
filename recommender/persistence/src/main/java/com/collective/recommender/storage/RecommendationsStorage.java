package com.collective.recommender.storage;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.ProjectProfile;
import com.collective.model.profile.UserProfile;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface RecommendationsStorage {

    public List<WebResourceEnhanced> getResourceRecommendations(URI userId)
            throws RecommendationsStorageException;
    
    void deleteResourceRecommendations(URI userId)
            throws RecommendationsStorageException;

    void storeResourceRecommendations(URI userId, List<WebResourceEnhanced> resourceRecommendations)
            throws RecommendationsStorageException;

    public List<ProjectProfile> getProjectRecommendations(URI userId)
            throws RecommendationsStorageException;

    void storeProjectProfileRecommendations(
            URI userId,
            List<ProjectProfile> projectProfileRecommendations
    ) throws RecommendationsStorageException;

    void deleteProjectProfileRecommendations(URI userId)
            throws RecommendationsStorageException;

    void deleteExpertsRecommandationsForProject(URI projectId)
            throws RecommendationsStorageException;

    void storeExpertsRecommendations(URI projectId, List<UserProfile> userProfiles)
            throws RecommendationsStorageException;

     public List<UserProfile> getExpertsRecommendations(URI projectId)
            throws RecommendationsStorageException;
}
