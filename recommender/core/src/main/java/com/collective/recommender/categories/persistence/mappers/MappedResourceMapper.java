package com.collective.recommender.categories.persistence.mappers;

import com.collective.recommender.categories.model.MappedResource;

import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public interface MappedResourceMapper {

    List<MappedResource> selectByUserId(Long userId, int amount);

}
