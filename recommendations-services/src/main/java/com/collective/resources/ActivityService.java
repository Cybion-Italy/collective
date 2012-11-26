package com.collective.resources;

import com.collective.activity.storage.ActivityStore;
import com.collective.activity.storage.ActivityStoreException;
import com.collective.resources.activities.model.UserVisitActivity;
import com.collective.resources.responses.ActivityServiceResponse;
import com.collective.resources.responses.ArrayListActivityResponse;
import com.collective.resources.responses.ServiceResponse;
import com.sun.jersey.api.core.InjectParam;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.objects.PersonObject;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.collective.activity.model.TopicObject.makeTopic;
import static com.collective.activity.model.WebResourceEnhancedObject.makeWebResourceEnhanced;
import static org.apache.abdera2.activities.model.Activity.makeActivity;
import static org.apache.abdera2.activities.model.Collection.makeCollection;
import static org.apache.abdera2.activities.model.Verb.SAVE;
import static org.apache.abdera2.activities.model.objects.PersonObject.makePerson;

/**
 * Used by external clients to add events to the ActivityStream of a user.
 *
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */

@Path("/activity")
@Produces(MediaType.APPLICATION_JSON)
public class ActivityService
{

    private static final Logger logger = Logger.getLogger(ActivityService.class);

    @InjectParam
    private InstanceManager instanceManager;

    public ActivityService()
    {
    }

    /*
     * Creates a new {@link Activity} performed by a <i>User</i>, that is the actor
     * of the {@link Activity}
     *
     *
    * @param userId         the identifier of the <i>User</i> inside the platform.
    * @param activityType   the type of activity that happened.
    * @param topics         the topics associated with the activity.
    *                       These should be expressed in terms of a
    *                       list of {@link URI} referring to <i>DBpedia</i>
    *                       pages or concepts.
    * */
    @POST
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addUserActivity(@PathParam("userId") String userId,
                                  UserVisitActivity userVisitActivity)
    {
        ActivityStore activityStore = instanceManager.getActivityStore();

        String activityId = UUID.randomUUID().toString();

        if (userVisitActivity.getTopics().size() != 0) {
            PersonObject user = makePerson()
                    .url(UserRecommendation.USER_BASE_URI + userId)
                    .id(UserRecommendation.USER_BASE_URI + userId)
                    .get();

            org.apache.abdera2.activities.model.Activity savedResourceActivity = null;
            //TODO (med) dynamically switch and build the proper verb and object
            //activity type should be 'save', or it doesnt work
            if (userVisitActivity.getActivityType().equals(SAVE.getName())) {

                List<ASObject> topicObjects = convertToASObjects(userVisitActivity);

                savedResourceActivity = makeActivity()
                        .actor(user)
                        .verb(SAVE)
                        .object(makeWebResourceEnhanced(
                                userVisitActivity.getSourceUrl())
                                        .topics(makeCollection(topicObjects))
                                        .get()
                        )

                        .published(new DateTime())
                        .id(activityId)
                        .get();
            }

            //TODO (high) remove null check and use null pattern
            if (savedResourceActivity != null) {
                //save it
                try {
                    activityStore.storeActorActivity(UserRecommendation.USER_BASE_URI + userId,
                                                     savedResourceActivity);
                } catch (ActivityStoreException e) {
                    final String emsg = "failed to store activity for userId: "
                            + UserRecommendation.USER_BASE_URI + userId;
                    throw new RuntimeException(emsg, e);
                }
            }
        }
        return activityId;
    }

    private List<ASObject> convertToASObjects(UserVisitActivity userVisitActivity)
    {
        List<ASObject> topicObjects = new ArrayList<ASObject>();

        for (String topic : userVisitActivity.getTopics()) {
            topicObjects.add(makeTopic(topic).get());

        }
        return topicObjects;
    }

    /*
    * Returns all the activities of a <i>User</i>
    *
    * @param userid     the id of the user
    * @param startTime  the start of chosen lapse of time
    * @param endTime    the end of chosen lapse of time
    *
    * */
    @GET
    @Path("/{userId}/{startTime}/{endTime}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserActivities(@PathParam("userId") String userId,
                                      @PathParam("startTime") String startTime,
                                      @PathParam("endTime") String endTime)
    {
        Long startLong = Long.parseLong(startTime);
        Long endLong = Long.parseLong(endTime);
        ArrayList<Activity> activities = new ArrayList<Activity>();

        String userProfileId = UserRecommendation.USER_BASE_URI + userId;

        logger.info("getting userId '" + userProfileId + "' activities from '" + startLong
                            + "' to endtime '" + endLong);
        try {
            activities.addAll(
                    this.instanceManager.getActivityStore().getActorActivities(userProfileId,
                                                                               startLong,
                                                                               endLong));
        } catch (ActivityStoreException e) {
            throw new RuntimeException(e);
        }

        Response.ResponseBuilder rb = Response.ok();
        rb.entity(new ArrayListActivityResponse(
                ServiceResponse.Status.OK,
                "user  " + userProfileId + " platform activities",
                activities));

//        logger.debug("deleted all userId '" + userProfileId + "' activities");
        return rb.build();


//        return activities;
    }

    /*
    * Deletes a specific activity of a <i>User</i>
    *
    * @param userId         the user who owns the activity
    * @param activityId     the id of the activity to delete
    * */
    @DELETE
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserActivity(@PathParam("userId") String userId)
    {

        String userProfileId = UserRecommendation.USER_BASE_URI + userId;
        logger.debug("about to delete userId activities '" + userProfileId + "'");

        ActivityStore activityStore = this.instanceManager.getActivityStore();

        try {
            activityStore.deleteActorActivities(userProfileId);
        } catch (ActivityStoreException e) {
            final String emsg = "failed deleting activities for user: " + "userId '" + userProfileId + "'";
            throw new RuntimeException(emsg, e);
        }

        Response.ResponseBuilder rb = Response.ok();
        rb.entity(new ActivityServiceResponse(
                ServiceResponse.Status.OK,
                "activity stream deleted successfully for user " + userProfileId,
                userProfileId));

        logger.debug("deleted all userId '" + userProfileId + "' activities");
        return rb.build();
    }
}
