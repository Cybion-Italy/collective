package com.collective.concepts.core;

import java.net.URL;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface UserDefinedConceptStore {

    public void storeConcept(long userId, Concept concept)
            throws UserDefinedConceptStoreException;

     public List<Concept> getUserConcepts(long userId)
            throws UserDefinedConceptStoreException;

    public void deleteConcept(long userId, URL conceptId)
            throws UserDefinedConceptStoreException;

    public void deleteAllConcepts(long userId)
            throws UserDefinedConceptStoreException;

    public List<Long> getUsers()
        throws UserDefinedConceptStoreException;

}
