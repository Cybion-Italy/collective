package com.collective.recommender.categories.model;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class MappedResource {

    private long userId;
    // on db it is just a progressive number, not a real tstamp
    private long happenedAt;
    private String resourceUrl;

    public MappedResource() {}

    public MappedResource(long happenedAt, long userId, String resourceUrl) {
        this.happenedAt  = happenedAt;
        this.userId      = userId;
        this.resourceUrl = resourceUrl;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getHappenedAt() {
        return happenedAt;
    }

    public void setHappenedAt(long happenedAt) {
        this.happenedAt = happenedAt;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MappedResource that = (MappedResource) o;

        if (happenedAt != that.happenedAt) return false;
        if (userId != that.userId) return false;
        if (resourceUrl != null ? !resourceUrl.equals(that.resourceUrl) : that.resourceUrl != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (happenedAt ^ (happenedAt >>> 32));
        result = 31 * result + (resourceUrl != null ? resourceUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MappedResource{" +
                "userId=" + userId +
                ", happenedAt=" + happenedAt +
                ", resourceUrl='" + resourceUrl + '\'' +
                '}';
    }
}
