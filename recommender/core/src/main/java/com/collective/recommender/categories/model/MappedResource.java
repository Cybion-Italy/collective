package com.collective.recommender.categories.model;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class MappedResource {

    private int resourceId;
    private long userId;
    private String resourceUrl;
    private long timestamp;

    public MappedResource(int resourceId, long userId, String resourceUrl, long timestamp) {
        this.resourceId = resourceId;
        this.userId = userId;
        this.resourceUrl = resourceUrl;
        this.timestamp = timestamp;
    }


}
