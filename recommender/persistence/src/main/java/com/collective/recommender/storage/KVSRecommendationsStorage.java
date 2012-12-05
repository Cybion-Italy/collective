package com.collective.recommender.storage;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.ProjectProfile;
import com.collective.model.profile.UserProfile;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.KVStoreException;
import tv.notube.commons.storage.kvs.mybatis.MyBatisKVStore;
import tv.notube.commons.storage.model.fields.serialization.SerializationManager;

import java.net.URI;
import java.util.List;
import java.util.Properties;

/**
 * {@link KVStore}-based implementation of {@link RecommendationsStorage}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class KVSRecommendationsStorage implements RecommendationsStorage {

    private static final String RESOURCES = "resources-recs";

    private static final String PROJECTS = "projects-recs";

    private static final String EXPERTS = "experts-recs";

    private static final String SHORT_TERM_RESOURCES = "short-term-resources-recs";

    private KVStore kvs;

    public KVSRecommendationsStorage(Properties properties) {
        kvs = new MyBatisKVStore(properties, new SerializationManager());
    }

    @Override
    public List<WebResourceEnhanced> getResourceRecommendations(URI userId)
            throws RecommendationsStorageException {
        try {
            return (List<WebResourceEnhanced>) kvs.getValue(RESOURCES,
                    userId.toString());
        } catch (KVStoreException e) {
            throw new RecommendationsStorageException("Error while getting " +
                    "resources recs for user '" + userId + "'", e);
        }
    }

    @Override
    public void deleteResourceRecommendations(URI userId)
            throws RecommendationsStorageException {
        try {
            kvs.deleteValue(RESOURCES, userId.toString());
        } catch (KVStoreException e) {
            throw new RecommendationsStorageException("Error while deleting " +
                    "resources recs for user '" + userId + "'", e);
        }
    }

    @Override
    public void storeResourceRecommendations(
            URI userId,
            List<WebResourceEnhanced> resourceRecommendations
    ) throws RecommendationsStorageException {
        try {
            kvs.setValue(RESOURCES, userId.toString(), resourceRecommendations);
        } catch (KVStoreException e) {
            throw new RecommendationsStorageException("Error while storing " +
                    "resources for user '" + userId + "'", e);
        }
    }

    @Override
    public List<WebResourceEnhanced> getShortTermResourceRecommendations(URI userId) throws RecommendationsStorageException {
        try {
            return (List<WebResourceEnhanced>) kvs.getValue(SHORT_TERM_RESOURCES,
                    userId.toString());
        } catch (KVStoreException e) {
            throw new RecommendationsStorageException("Error while getting " +
                    "short term recs for user '" + userId + "'", e);
        }
    }

    @Override
    public void deleteShortTermResourceRecommendations(URI userId) throws RecommendationsStorageException {
        try {
            kvs.deleteValue(SHORT_TERM_RESOURCES, userId.toString());
        } catch (KVStoreException e) {
            throw new RecommendationsStorageException("Error while deleting " +
                    "short term recs for user '" + userId + "'", e);
        }
    }

    @Override
    public void storeShortTermResourceRecommendations(URI userId, List<WebResourceEnhanced> resourceRecommendations) throws RecommendationsStorageException {
        try {
            kvs.setValue(SHORT_TERM_RESOURCES, userId.toString(), resourceRecommendations);
        } catch (KVStoreException e) {
            throw new RecommendationsStorageException("Error while storing " +
                    "short term recs for user '" + userId + "'", e);
        }
    }

    @Override
    public List<ProjectProfile> getProjectRecommendations(URI userId)
            throws RecommendationsStorageException {
        try {
            return (List<ProjectProfile>) kvs.getValue(PROJECTS,
                    userId.toString());
        } catch (KVStoreException e) {
            throw new RecommendationsStorageException("Error while getting " +
                    "projects recs for user '" + userId + "'", e);
        }
    }

    @Override
    public void storeProjectProfileRecommendations(
            URI userId,
            List<ProjectProfile> projectProfileRecommendations
    ) throws RecommendationsStorageException {
        try {
            kvs.setValue(PROJECTS, userId.toString(), projectProfileRecommendations);
        } catch (KVStoreException e) {
            throw new RecommendationsStorageException("Error while storing " +
                    "projects recs for user '" + userId + "'", e);
        }
    }

    @Override
    public void deleteProjectProfileRecommendations(URI userId)
            throws RecommendationsStorageException {
        try {
            kvs.deleteValue(PROJECTS, userId.toString());
        } catch (KVStoreException e) {
            throw new RecommendationsStorageException("Error while deleting " +
                    "projects recs for user '" + userId + "'", e);
        }
    }

    @Override
    public void deleteExpertsRecommandationsForProject(URI projectId)
            throws RecommendationsStorageException {
        try {
            kvs.deleteValue(EXPERTS, projectId.toString());
        } catch (KVStoreException e) {
            throw new RecommendationsStorageException("Error while deleting " +
                    "experts recs for project '" + projectId + "'", e);
        }
    }

    @Override
    public void storeExpertsRecommendations(URI projectId, List<UserProfile> userProfiles)
            throws RecommendationsStorageException {
        try {
            kvs.setValue(
                    EXPERTS,
                    projectId.toString(),
                    userProfiles
            );
        } catch (KVStoreException e) {
            throw new RecommendationsStorageException("Error while storing " +
                    "experts recs for project '" + projectId + "'", e);
        }
    }

    @Override
    public List<UserProfile> getExpertsRecommendations(URI projectId)
            throws RecommendationsStorageException {
        try {
            return (List<UserProfile>) kvs.getValue(EXPERTS,
                    projectId.toString());
        } catch (KVStoreException e) {
            throw new RecommendationsStorageException("Error while getting " +
                    "experts recs for project '" + projectId + "'", e);
        }
    }
}
