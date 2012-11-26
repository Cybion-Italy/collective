package com.collective.resources.activities.model;

import com.google.gson.annotations.Expose;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public abstract class BaseActivity
{
    @Expose
    protected String activityType;

    protected BaseActivity(String activityType)
    {
        this.activityType = activityType;
    }

    protected BaseActivity() {}

    public String getActivityType()
    {
        return activityType;
    }

    @Override
    public String toString()
    {
        return "BaseActivity{" +
                "activityType='" + activityType + '\'' +
                '}';
    }
}
