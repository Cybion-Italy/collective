package com.collective.profiler.storage;

import com.collective.model.profile.Profile;
import com.collective.model.profile.ProjectProfile;
import com.collective.model.profile.SearchProfile;
import com.collective.model.profile.UserProfile;
import org.openrdf.model.Resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Main interface defining the behavior of a generic
 * {@link com.collective.model.profile.Profile} repository.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public interface ProfileStore {

    /**
     *
     * @param graph
     * @return
     * @throws ProfileStoreException
     */
    public UserProfile getUserProfile(URI graph)
        throws ProfileStoreException;

    /**
     * 
     * @param id
     * @return
     * @throws ProfileStoreException
     */
    public ProjectProfile getProjectProfile(URI id)
        throws ProfileStoreException;

    /**
     *
     * @param id
     * @return
     * @throws ProfileStoreException
     */
    public SearchProfile getSearchProfile(URI id)
        throws ProfileStoreException;

    /**
     * Deletes all the triples stored in a named graph. The id represents the graph name.
     * @param id
     * @throws ProfileStoreException
     */
    public void deleteGraph(URI id)
        throws ProfileStoreException;

    /**
     * 
     * @throws ProfileStoreException
     */
    public void close()
        throws ProfileStoreException;

    /**
     * Get the {@link com.collective.profiler.storage.ProfileStoreConfiguration}.
     *
     * @return a not <code>null</code> {@link com.collective.profiler.storage.ProfileStoreConfiguration}
     */
    public ProfileStoreConfiguration getConfiguration();

    void storeProfile(Profile profile, URI uri)
            throws ProfileStoreException;

    public void deleteProfileFromGraphIndex(URI graphNamespace, URI profileGraphName)
            throws ProfileStoreException;

    public void storeProfileToGraphIndex(URI indexGraphName, URI profileGraphName)
            throws ProfileStoreException, URISyntaxException;

    // TODO: understand and refactor
    List<Resource> getAllTriplesSubjectsFromGraphIndex(URI indexGraphName)
            throws ProfileStoreException;
}
