package com.collective.concepts.core.lookup.lucene;

import com.collective.concepts.core.Concept;
import com.collective.concepts.core.UserDefinedConceptStore;
import com.collective.concepts.core.UserDefinedConceptStoreException;

import java.net.URL;
import java.util.*;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class InMemoryConceptStore implements UserDefinedConceptStore
{
    private Map<Long, List<Concept>> conceptStore = new HashMap<Long, List<Concept>>();


    public void storeConcept(long userId, Concept concept) throws UserDefinedConceptStoreException
    {
        if (this.conceptStore.containsKey(userId)) {
            this.conceptStore.get(userId).add(concept);
        } else {
            List<Concept> userConcepts = new ArrayList<Concept>();
            userConcepts.add(concept);
            this.conceptStore.put(userId, userConcepts);
        }
    }

    public List<Concept> getUserConcepts(long userId) throws UserDefinedConceptStoreException
    {
        if (this.conceptStore.containsKey(userId)) {
            return this.conceptStore.get(userId);
        } else {
            return Collections.emptyList();
        }
    }

    public void deleteConcept(long userId, URL conceptId) throws UserDefinedConceptStoreException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteAllConcepts(long userId) throws UserDefinedConceptStoreException
    {
        if (this.conceptStore.containsKey(userId)) {
            this.conceptStore.remove(userId);
        }
    }

    public List<Long> getUsers() throws UserDefinedConceptStoreException
    {
        throw new UnsupportedOperationException(
                "not implemented yet");  //To change body of implemented methods use File | Settings | File Templates.
    }
}
