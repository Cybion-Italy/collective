package com.collective.profilingline.skos.repository;

import com.collective.profilingline.skos.SkosSubject;

import java.net.URI;
import java.util.List;

/**
 * A persistent implementation of {@link com.collective.profilingline.skos.repository.SkosRepository}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class MyBatisSkosRepository implements SkosRepository {

    private SkosSubjectDao skosSubjectDao = new SkosSubjectDao();

    public SkosSubject lookup(URI uri) throws SkosRepositoryException {
        return skosSubjectDao.getMostRepresentativeSkosSubjectByResourceURI(uri);
    }

    public List<SkosSubject> browse(SkosSubject skosSubject, SKOS skos) throws SkosRepositoryException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public SkosSubject getSkosByURI(URI uri) throws SkosRepositoryException {
        return skosSubjectDao.getSkosSubjectByURI(uri);
    }
}
