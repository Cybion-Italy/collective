package com.collective.analyzer.storage;

import com.collective.model.persistence.WebResource;

import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public interface WebResourceRepository {

    List<WebResource> selectWebResourcesToAnalyse(int amount) throws WebResourceRepositoryException;

    void update(WebResource webResource);
}
