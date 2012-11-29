package com.collective.resources;

import com.collective.dynamicprofile.model.DynamicUserProfile;
import com.collective.resources.utilities.ProductionDomainFixtures;
import com.sun.jersey.api.core.InjectParam;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URISyntaxException;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
@Path("/user-profile")
@Produces(MediaType.APPLICATION_JSON)
public class UserProfileService
{
    private static final Logger logger = Logger.getLogger(UserProfileService.class);

    @InjectParam
    private InstanceManager instanceManager;

    public UserProfileService()
    {}

    @GET
    @Path("short-term/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public DynamicUserProfile getShortTermUserProfile(@PathParam("userId") String userId)
    {
        //TODO
        String shortUserProfile = UserRecommendation.SHORT_TERM_USER_PROFILE_BASE_URI + userId;
        logger.info("returning short-term user profile contained in graph " + shortUserProfile);
        DateTime start = new DateTime().minusDays(30);
        DateTime end = new DateTime().minusDays(7);
        DateTime lastUpdatedAt = new DateTime();

        DynamicUserProfile shortTermUserProfile = null;
        try {
            //THIS IS FAKE DATA
            shortTermUserProfile = ProductionDomainFixtures.getDynamicUserProfile(shortUserProfile,
                                                                                  start, end,
                                                                                  lastUpdatedAt, 2);
        } catch (URISyntaxException e) {
            throw new RuntimeException("error with uri syntax", e);
        }

        return shortTermUserProfile;
    }

}
