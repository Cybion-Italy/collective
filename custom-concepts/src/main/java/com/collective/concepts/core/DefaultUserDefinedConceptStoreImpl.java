package com.collective.concepts.core;

import org.apache.log4j.Logger;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.KVStoreException;
import tv.notube.commons.storage.model.fields.StringField;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultUserDefinedConceptStoreImpl implements UserDefinedConceptStore {

    private static Logger logger = Logger.getLogger(DefaultUserDefinedConceptStoreImpl.class);

    private static final String TABLE = "concepts";

    private KVStore kvStore;

    public DefaultUserDefinedConceptStoreImpl(KVStore kvStore) {
        this.kvStore = kvStore;
    }

    public void storeConcept(long userId, Concept concept)
            throws UserDefinedConceptStoreException {
        List<Concept> concepts;
        try {
            concepts = (List<Concept>)
                    kvStore.getValue(TABLE, String.valueOf(userId));
        } catch (KVStoreException e) {
            final String errMsg = "Error while retrieving user '" + userId + "' concepts";
            logger.error(errMsg, e);
            throw new UserDefinedConceptStoreException(
                    errMsg,
                    e
            );
        }
        if (concepts == null) {
            List<Concept> conceptsToStore = new ArrayList<Concept>();
            conceptsToStore.add(concept);
            StringField o = new StringField("owner", String.valueOf(userId));
            try {
                kvStore.setValue(TABLE, String.valueOf(userId), conceptsToStore, o);
            } catch (KVStoreException e) {
                final String errMsg = "Error while storing concept '" + concept + "' to user '" + userId + "'";
                logger.error(errMsg, e);
                throw new UserDefinedConceptStoreException(
                        errMsg,
                        e
                );
            }
            return;
        }
        concepts.add(concept);
        try {
            kvStore.deleteValue(TABLE, String.valueOf(userId));
        } catch (KVStoreException e) {
            final String errMsg = "Error while deleting existing concepts for user '" + userId + "'";
            logger.error(errMsg, e);
            throw new UserDefinedConceptStoreException(
                    errMsg,
                    e
            );
        }
        StringField o = new StringField("owner", String.valueOf(userId));
        try {
            kvStore.setValue(TABLE, String.valueOf(userId), concepts, o);
        } catch (KVStoreException e) {
            final String errMsg = "Error while storing concept '" + concept + "' to user '" + userId + "'";
            logger.error(errMsg, e);
            throw new UserDefinedConceptStoreException(
                    errMsg,
                    e
            );
        }
    }

    public List<Concept> getUserConcepts(long userId)
            throws UserDefinedConceptStoreException {
        try {
            return (List<Concept>) kvStore.getValue(TABLE, String.valueOf(userId));
        } catch (KVStoreException e) {
            final String errMsg = "Error while retrieving concepts for user '" + userId + "'";
            logger.error(errMsg, e);
            throw new UserDefinedConceptStoreException(errMsg, e);
        }
    }

    public void deleteConcept(long userId, URL conceptId)
            throws UserDefinedConceptStoreException {
        List<Concept> concepts = getUserConcepts(userId);
        if (concepts != null) {
            List<Concept> newConcepts = new ArrayList<Concept>();
            for (Concept concept : concepts) {
                if (!concept.getURL().equals(conceptId)) {
                    newConcepts.add(concept);
                }
            }
            try {
                kvStore.deleteValue(TABLE, String.valueOf(userId));
            } catch (KVStoreException e) {
                final String errMsg = "Error while deleting existing values for user '" + userId + "'";
                logger.error(errMsg, e);
                throw new UserDefinedConceptStoreException(errMsg, e);
            }
            StringField o = new StringField("owner", String.valueOf(userId));
            try {
                kvStore.setValue(TABLE, String.valueOf(userId), newConcepts, o);
            } catch (KVStoreException e) {
                final String errMsg = "Error while storing concepts for user '" + userId + "'";
                logger.error(errMsg, e);
                throw new UserDefinedConceptStoreException(errMsg, e);
            }
        }
    }

    public void deleteAllConcepts(long userId) throws UserDefinedConceptStoreException {
        try {
            kvStore.deleteValue(TABLE, String.valueOf(userId));
        } catch (KVStoreException e) {
            final String errMsg = "Error while storing concepts for user '" + userId + "'";
            logger.error(errMsg, e);
            throw new UserDefinedConceptStoreException(errMsg, e);
        }
    }

    public List<Long> getUsers() throws UserDefinedConceptStoreException {
        List<String> userIds;
        try {
            userIds = kvStore.search(TABLE);
        } catch (KVStoreException e) {
            final String errMsg = "Error while getting user IDs";
            logger.error(errMsg, e);
            throw new UserDefinedConceptStoreException(errMsg, e);
        }
        List<Long> result = new ArrayList<Long>();
        for (String userId : userIds) {
            result.add(Long.parseLong(userId));
        }
        return result;
    }
}
