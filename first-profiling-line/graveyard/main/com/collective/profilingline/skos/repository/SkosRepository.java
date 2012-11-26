package com.collective.profilingline.skos.repository;

import com.collective.profilingline.skos.SkosSubject;

import java.net.URI;
import java.util.List;

/**
 * Defines the main operations to access a database of
 * <i>SKOS subject</i>.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface SkosRepository {

    public enum SKOS {
        NARROWER,
        BROADER
    }

    /**
     * Returns the <i>Most representative SKOS subject</i>
     * of the provided input URI-identified resource.
     *
     * @param uri
     * @return
     * @throws SkosRepositoryException
     */
    public SkosSubject lookup(URI uri)
        throws SkosRepositoryException;

    /**
     * Access to the {@link com.collective.profilingline.skos.SkosSubject}
     * provided as input taxonomy.
     * 
     * @param skosSubject
     * @param skos
     * @return
     * @throws SkosRepositoryException
     */
    public List<SkosSubject> browse(SkosSubject skosSubject, SKOS skos)
        throws SkosRepositoryException;

    /**
     * 
     * @param uri
     * @return
     * @throws SkosRepositoryException
     */
    public SkosSubject getSkosByURI(URI uri)
        throws SkosRepositoryException;

}
