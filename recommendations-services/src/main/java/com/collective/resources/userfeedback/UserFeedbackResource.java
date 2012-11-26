package com.collective.resources.userfeedback;

import com.collective.resources.InstanceManager;
import com.collective.usertests.model.UserFeedback;
import com.sun.jersey.api.core.InjectParam;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
@Path("/user-feedback-test/userfeedback")
@Produces(MediaType.APPLICATION_JSON)
public class UserFeedbackResource {

    @InjectParam
    private InstanceManager instanceManager;

    private static Logger logger = Logger.getLogger(UserFeedbackResource.class);

    @GET
    @Path("/list/{userId}")
    public List getUserFeedbackByUserId(@PathParam("userId") String userId) {
        Long userIdLong = Long.parseLong(userId);
        List<UserFeedback> userFeedbackList =
                instanceManager.getUserFeedbackDao()
                        .getAllUserFeedbackByUserId(userIdLong);

        for (UserFeedback userFeedback: userFeedbackList) {
            logger.debug(userFeedback.toString());
        }
        return userFeedbackList;
    }

    @GET
    @Path("/{id}")
    public UserFeedback getUserFeedbackById(@PathParam("id") String id) {
        Long idLong = Long.parseLong(id);
        UserFeedback userFeedback = instanceManager.getUserFeedbackDao().getUserFeedback(idLong);
        return userFeedback;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CreatedUserFeedbackResponse insertUserFeedback(UserFeedback userFeedback) {
        //timestamp the object
        userFeedback.setDate(new DateTime());
//        logger.debug("received POST with: " + userFeedback.toString());
        instanceManager.getUserFeedbackDao().insertUserFeedback(userFeedback);
        logger.debug("created userFeedback with id: " + userFeedback.getId());
        //TODO (high) refactor to return Result class? then refactor the php
        // client on wordpress
        return new CreatedUserFeedbackResponse(userFeedback.getId());
    }

    @DELETE
    @Path("/{id}")
    public void deleteUserFeedbackById(@PathParam("id") String id) {
        Long idLong = Long.parseLong(id);
        instanceManager.getUserFeedbackDao().deleteUserFeedback(idLong);
    }
}
