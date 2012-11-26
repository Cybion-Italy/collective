package com.collective.dynamicprofile.model;

import com.google.gson.annotations.Expose;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class DynamicUserProfile
{
    @Expose
    private String entityId;

    @Expose
    private Set<Interest> interests = new HashSet<Interest>();

    @Expose
    private DateTime lastUpdatedAt;

    @Expose
    private Interval relevantInterval;

    //for gson
    public DynamicUserProfile()
    {}

    public DynamicUserProfile(String entityId, Set<Interest> interests, Interval interval)
    {
        this.entityId = entityId;
        this.interests = interests;
        this.relevantInterval = interval;
    }

    public String getEntityId()
    {
        return entityId;
    }

    public void setEntityId(String entityId)
    {
        this.entityId = entityId;
    }

    public Set<Interest> getInterests()
    {
        return interests;
    }

    public void setInterests(Set<Interest> interests)
    {
        this.interests = interests;
    }

    public DateTime getLastUpdatedAt()
    {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(DateTime lastUpdatedAt)
    {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Interval getRelevantInterval()
    {
        return relevantInterval;
    }

    public void setRelevantInterval(Interval relevantInterval)
    {
        this.relevantInterval = relevantInterval;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DynamicUserProfile that = (DynamicUserProfile) o;

        if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null)
            return false;
        if (interests != null ? !interests.equals(that.interests) : that.interests != null)
            return false;
        if (lastUpdatedAt != null ? !lastUpdatedAt.equals(
                that.lastUpdatedAt) : that.lastUpdatedAt != null) return false;
        if (relevantInterval != null ? !relevantInterval.equals(
                that.relevantInterval) : that.relevantInterval != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = entityId != null ? entityId.hashCode() : 0;
        result = 31 * result + (interests != null ? interests.hashCode() : 0);
        result = 31 * result + (lastUpdatedAt != null ? lastUpdatedAt.hashCode() : 0);
        result = 31 * result + (relevantInterval != null ? relevantInterval.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "DynamicUserProfile{" +
                "entityId='" + entityId + '\'' +
                ", interests=" + interests +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", relevantInterval=" + relevantInterval +
                '}';
    }
}
