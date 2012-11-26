package com.collective.activity.storage;


import org.apache.abdera2.activities.model.Activity;

import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public interface ActivityStore {

    public void storeActorActivity(String actorId, Activity activity)
            throws ActivityStoreException;

    public Activity getActorActivity(String actorId, String activityId)
            throws ActivityStoreException;

    public void deleteActorActivity(String actorId, String activityId)
            throws ActivityStoreException;

    public void deleteActorActivities(String actorId)
            throws ActivityStoreException;

    public List<Activity> getActorActivities(String actorId, long start, long end)
            throws ActivityStoreException;

    public List<Activity> getAllActorActivities(String actorId)
            throws ActivityStoreException;

    public void storeActorActivities(String actorId, List<Activity> activitiesToSave)
            throws ActivityStoreException;
}
