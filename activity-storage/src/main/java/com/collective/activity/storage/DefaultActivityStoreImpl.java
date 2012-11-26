package com.collective.activity.storage;

import org.apache.abdera2.activities.model.Activity;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.KVStoreException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class DefaultActivityStoreImpl implements ActivityStore {

    private KVStore kvStore;
    
    private static final String ACTIVITIES_TABLE = "activity-stream";

    public DefaultActivityStoreImpl(KVStore kvStore) {
        this.kvStore = kvStore;
    }

    @Override
    public synchronized void storeActorActivity(String actorId, Activity activity)
            throws ActivityStoreException {

        //GET existing list
        List<Activity> activities = null;
        try {
             activities = (List<Activity>) kvStore.getValue(ACTIVITIES_TABLE, actorId);
        } catch (KVStoreException e) {
            final String emsg = "failed to read kvstore: '" + ACTIVITIES_TABLE
                    + "' with key: '" + actorId + "' ";
            throw new ActivityStoreException(emsg,  e);
        }

        //ADD activity
        if (activities == null) {
            activities = new ArrayList<Activity>();
            activities.add(activity);            
        } else {
            activities.add(activity);
        }

        //TODO once new upsert kvs is redeployed, remove the delete
        //DELETE old value
        try {
            kvStore.deleteValue(ACTIVITIES_TABLE, actorId);
        } catch (KVStoreException e) {
            final String emsg = "failed to delete old activities list on " +
                    "kvstore: '" + ACTIVITIES_TABLE
                    + "' with key: '" + actorId + "'";
            throw new ActivityStoreException(emsg,  e);
        }

        //UPDATE it, writing it back
        try {
            kvStore.setValue(ACTIVITIES_TABLE, actorId, activities);
        } catch (KVStoreException e) {
            final String emsg = "failed to write activities list on " +
                    "kvstore: '" + ACTIVITIES_TABLE
                    + "' with key: '" + actorId + "'";
            throw new ActivityStoreException(emsg,  e);
        }
    }

    @Override
    public Activity getActorActivity(String actorId, String activityId)
            throws ActivityStoreException {

        //GET existing list
        List<Activity> activities = new ArrayList<Activity>();
        try {
            activities.addAll((List<Activity>) kvStore.getValue(ACTIVITIES_TABLE, actorId));
        } catch (KVStoreException e) {
            final String emsg = "failed to read kvstore: '" + ACTIVITIES_TABLE
                    + "' with key: '" + actorId + "' ";
            throw new ActivityStoreException(emsg,  e);
        }

        Activity foundActivity = null;

        if (activities != null) {
            boolean found = false;
            Iterator<Activity> iterator = activities.iterator();
            while (!found && iterator.hasNext()) {
                Activity activity = iterator.next();
                if (activity.getId().equals(activityId)) {
                    found = true;
                    foundActivity = activity;
                }
            }
        }
        return foundActivity;
    }

    @Override
    public synchronized void deleteActorActivity(String actorId, String activityId)
            throws ActivityStoreException {
        throw new UnsupportedOperationException("NIY");
    }

    @Override
    public synchronized void deleteActorActivities(String userId)
            throws ActivityStoreException {
        try {
            kvStore.deleteValue(ACTIVITIES_TABLE, userId);
        } catch (KVStoreException e) {
            final String emsg = "failed to delete value from kvstore: '" + ACTIVITIES_TABLE
                    + "' with key: '" + userId + "' ";
            throw new ActivityStoreException(emsg,  e);
        }
    }

    @Override
    public List<Activity> getActorActivities(String userId, long start, long end)
            throws ActivityStoreException {

        if (start > end)
            throw new IllegalArgumentException("wrong parameters: start '"  + start + "' is higher than end '" + end + "'");

        //GET existing list
        List<Activity> activities = null;
        try {
            activities = (List<Activity>) kvStore.getValue(ACTIVITIES_TABLE, userId);
        } catch (KVStoreException e) {
            final String emsg = "failed to read kvstore: '" + ACTIVITIES_TABLE
                    + "' with key: '" + userId + "' ";
            throw new ActivityStoreException(emsg,  e);
        }

        List<Activity> matchingActivities = new ArrayList<Activity>();

        if (activities != null) {
            for (Activity activity : activities) {
                if (activity.getPublished().isAfter(start) &&
                    activity.getPublished().isBefore(end)
                        ) {
                    matchingActivities.add(activity);
                }
            }
        }
        return matchingActivities;
    }

    @Override
    public List<Activity> getAllActorActivities(String userId)
            throws ActivityStoreException {
        List<Activity> activities = new ArrayList<Activity>();
        try {
            activities.addAll((List<Activity>) kvStore.getValue(ACTIVITIES_TABLE, userId));
        } catch (KVStoreException e) {
            final String emsg = "failed to read kvstore: '" + ACTIVITIES_TABLE
                    + "' with key: '" + userId + "' ";
            throw new ActivityStoreException(emsg,  e);
        }
        return activities;
    }

    @Override
    public synchronized void storeActorActivities(String userId, List<Activity> activitiesToSave)
            throws ActivityStoreException {
        //GET existing list
        List<Activity> activities = null;
        try {
            activities = (List<Activity>) kvStore.getValue(ACTIVITIES_TABLE, userId);
        } catch (KVStoreException e) {
            final String emsg = "failed to read kvstore: '" + ACTIVITIES_TABLE
                    + "' with key: '" + userId + "' ";
            throw new ActivityStoreException(emsg,  e);
        }

        //ADD activity
        if (activities == null) {
            activities = new ArrayList<Activity>();
            activities.addAll(activitiesToSave);
        } else {
            activities.addAll(activitiesToSave);
        }

        //DELETE old value
        try {
            kvStore.deleteValue(ACTIVITIES_TABLE, userId);
        } catch (KVStoreException e) {
            final String emsg = "failed to delete old activities list on " +
                    "kvstore: '" + ACTIVITIES_TABLE
                    + "' with key: '" + userId + "'";
            throw new ActivityStoreException(emsg,  e);
        }

        //UPDATE it, writing it back
        try {
            kvStore.setValue(ACTIVITIES_TABLE, userId, activities);
        } catch (KVStoreException e) {
            final String emsg = "failed to write activities list on " +
                    "kvstore: '" + ACTIVITIES_TABLE
                    + "' with key: '" + userId + "'";
            throw new ActivityStoreException(emsg,  e);
        }
    }
}
