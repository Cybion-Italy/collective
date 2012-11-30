package com.collective.recommender.categories.persistence;

import com.collective.recommender.categories.exceptions.CategoriesMappingStorageException;
import com.collective.recommender.categories.model.MappedResource;

import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public interface CategoriesMappingStorage {

    public List<MappedResource> getLatestMappedResources(Long userId, int amount) throws CategoriesMappingStorageException;
}
