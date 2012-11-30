package com.collective.recommender.dynamicprofiler;

import com.collective.recommender.categories.model.MappedResource;

import java.net.URI;
import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class ShortTermUserProfileCalculator {

    public ShortTermUserProfileCalculator() {
        //TODO init a local enricher
    }

    public ShortTermUserProfile updateProfile(URI userId, List<MappedResource> latestMappedResources) {

        //  for each one, enrich the com.collective.resources.Enricher class service (or from jar maybe?...)
        //  do a query to get latest resources that match those URIs

        throw new UnsupportedOperationException("NIY");
    }
}
