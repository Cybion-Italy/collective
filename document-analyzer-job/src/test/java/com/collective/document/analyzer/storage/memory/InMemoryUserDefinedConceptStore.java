package com.collective.document.analyzer.storage.memory;

import com.collective.concepts.core.Concept;
import com.collective.concepts.core.UserDefinedConceptStore;
import com.collective.concepts.core.UserDefinedConceptStoreException;

import java.net.URL;
import java.util.*;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class InMemoryUserDefinedConceptStore implements UserDefinedConceptStore {

    private Map<Long, List<Concept>> userConcepts;

    public InMemoryUserDefinedConceptStore() {
        this.userConcepts = new HashMap<Long, List<Concept>>();
    }

    public void storeConcept(long userIdLong, Concept concept)
            throws UserDefinedConceptStoreException {
        Long userId = new Long(userIdLong);
        List<Concept> existingUserConcepts = this.userConcepts.get(userId);
        if (existingUserConcepts != null) {
            List<Concept> updatedConceptList = existingUserConcepts;
            updatedConceptList.add(concept);
            this.userConcepts.put(userId, updatedConceptList);
        } else {
            List<Concept> newUserConceptList = new ArrayList<Concept>();
            this.userConcepts.put(userId, newUserConceptList);
        }
    }

    public List<Concept> getUserConcepts(long userId)
            throws UserDefinedConceptStoreException {
        return this.userConcepts.get(new Long(userId));
    }

    public void deleteConcept(long l, URL url)
            throws UserDefinedConceptStoreException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteAllConcepts(long userId)
            throws UserDefinedConceptStoreException {
        this.userConcepts.remove(new Long(userId));
    }

    public List<Long> getUsers() throws UserDefinedConceptStoreException {
        Set<Long> userIdsSet = this.userConcepts.keySet();
        return new ArrayList<Long>(userIdsSet);
    }
}
