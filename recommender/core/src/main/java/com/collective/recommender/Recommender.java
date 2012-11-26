package com.collective.recommender;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.ProjectProfile;
import com.collective.model.profile.UserProfile;
import com.collective.permanentsearch.model.LabelledURI;

import java.net.URI;
import java.util.List;
import java.util.Set;

/**
 * @author Davide Palmisano
 */
public interface Recommender {

    public Set<WebResourceEnhanced> getResourceRecommendations(UserProfile profile)
        throws RecommenderException;

    public Set<ProjectProfile> getProjectRecommendations(UserProfile profile)
        throws RecommenderException;

    public List<UserProfile> getSimilarUsers(UserProfile profile)
        throws RecommenderException;

	public Set<WebResourceEnhanced> getResourceRecommendations(ProjectProfile profile)
            throws RecommenderException;

    public Set<UserProfile> getExpertUsersForProject(ProjectProfile projectProfile)
        throws RecommenderException;

    public Set<WebResourceEnhanced> getResourceRecommendations(
                                               List<LabelledURI> commonConcepts)
            throws RecommenderException;

    public Set<WebResourceEnhanced> getCustomConceptsResourceRecommendations(
            List<LabelledURI> customConcepts,
            Long userId,
            URI customAnnotationsGraphPrefix)
        throws RecommenderException;

}
