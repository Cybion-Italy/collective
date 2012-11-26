package com.collective.resources;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.UserProfile;

import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public interface AbstractRecommendation {

    public static String WEB_MAGAZINES = "webmagazines";

    public static String RUMOURS = "rumours";

    public static String USERS = "users";

    public static final String PROJECTS = "projects";    

    public static int MAX_RECOMMENDATIONS = 50;

    /* TODO: add pagination of results? */
    /* TODO: can we annotate the interface instead of each implementing method?
     * and annotate just the "new" methods not in the interface? */
    /* TODO: refactor parameters to allow more complex inputs? */
    public List<WebResourceEnhanced> getWebMagazineArticleRecommendations(String id, int maxItems);

    public List<WebResourceEnhanced> getRumoursRecommendations(String id, int maxItems);

    public List<UserProfile> getExpertsRecommendations(String id, int maxItems);

}
