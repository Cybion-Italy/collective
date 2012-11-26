package com.collective.resources.search;

import com.collective.permanentsearch.model.Search;
import com.collective.resources.InstanceManager;
import com.collective.resources.Result;
import com.sun.jersey.api.core.InjectParam;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
public class SearchResource
{

    private static final Logger logger = Logger.getLogger(SearchResource.class);

    @InjectParam
    private InstanceManager instanceManager;


    @GET
    @Path("/{searchId}")
    public Search getSearch(@PathParam("searchId") String searchId)
    {
        Search search = instanceManager.getSearchDao().select(Long.parseLong(searchId));
        return search;
    }

    @DELETE
    @Path("/{searchId}")
    public Result deleteUserSearch(@PathParam("searchId") Long searchId)
    {
        instanceManager.getSearchDao().delete(searchId);
        logger.debug("deleted search with id: " + searchId);
        return new Result(Result.Status.OK, "User search successfully deleted");
    }
}
