package com.collective.resources;

import com.collective.activity.storage.ActivityStore;
import com.collective.analyzer.enrichers.EnrichmentService;
import com.collective.concepts.core.UserDefinedConceptStore;
import com.collective.permanentsearch.persistence.dao.SearchDao;
import com.collective.profiler.storage.ProfileStore;
import com.collective.resources.recommendations.RecommendationsStore;
import com.collective.resources.utilities.LimitChecker;
import com.collective.usertests.persistence.dao.ReasonDao;
import com.collective.usertests.persistence.dao.UserFeedbackDao;
import com.google.gson.Gson;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com ) 
 */
public class InstanceManager {

    private RecommendationsStore recommendationsStore;
    
    private LimitChecker limitChecker;

    private ProfileStore profileStore;

    private SearchDao searchDao;

    private Gson gson;

    private ReasonDao reasonDao;

    private UserFeedbackDao userFeedbackDao;

    private UserDefinedConceptStore conceptStore;

    private ActivityStore activityStore;

    private EnrichmentService dbPediaEnrichmentService;

    public InstanceManager() {
        recommendationsStore = LoaderInstanceManager.getInstance().getRecommendationsStore();
        profileStore = LoaderInstanceManager.getInstance().getProfileStore();
        limitChecker = LoaderInstanceManager.getInstance().getLimitChecker();
        searchDao = LoaderInstanceManager.getInstance().getSearchDao();
        gson = LoaderInstanceManager.getInstance().getGson();
        reasonDao = LoaderInstanceManager.getInstance().getReasonDao();
        userFeedbackDao = LoaderInstanceManager.getInstance().getUserFeedbackDao();
        conceptStore = LoaderInstanceManager.getInstance().getUserDefinedConceptStore();
        activityStore = LoaderInstanceManager.getInstance().getActivityStore();
        dbPediaEnrichmentService = LoaderInstanceManager.getInstance().getDbPediaEnrichmentService();
    }

    RecommendationsStore getRecommendationsStore() {
        return recommendationsStore;
    }

    LimitChecker getLimitChecker() {
        return limitChecker;
    }

    public ProfileStore getProfileStore() {
        return profileStore;
    }

    public SearchDao getSearchDao() {
        return searchDao;
    }

    public Gson getGson() {
        return gson;
    }

    public UserFeedbackDao getUserFeedbackDao() {
        return userFeedbackDao;
    }

    public ReasonDao getReasonDao() {
        return reasonDao;
    }

    public UserDefinedConceptStore getConceptStore() {
        return conceptStore;
    }

    public ActivityStore getActivityStore() {
        return activityStore;
    }

    public EnrichmentService getDBpediaEnrichmentService()
    {
        return dbPediaEnrichmentService;
    }
}
