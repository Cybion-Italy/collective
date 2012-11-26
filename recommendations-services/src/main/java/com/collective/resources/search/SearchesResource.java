package com.collective.resources.search;

import javax.ws.rs.*;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
@Path("/searches")
public class SearchesResource {

    @GET
    @Path("user/{userId}")
    public List<SearchResource> getUserSearches(@PathParam("userId") String userId) {
        // get all Searches owned by the userId
        throw new RuntimeException();
    }

    @GET
    @Path("project/{projectId}")
    public List<SearchResource> getProjectSearches(@PathParam("projectId") String projectId) {
        // get all Searches owned by the projectId
        throw new RuntimeException();
    }
}
