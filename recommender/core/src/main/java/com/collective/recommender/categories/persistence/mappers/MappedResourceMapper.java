package com.collective.recommender.categories.persistence.mappers;

import com.collective.recommender.categories.model.MappedResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public interface MappedResourceMapper {

    List<MappedResource> selectLatestMappingsByUserId(@Param("userId") Long userId,
                                                      @Param ("maxAmount") int amount);

}
