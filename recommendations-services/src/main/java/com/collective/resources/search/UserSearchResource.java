package com.collective.resources.search;

import com.collective.permanentsearch.model.LabelledURI;
import com.collective.permanentsearch.model.Search;
import com.collective.permanentsearch.persistence.dao.SearchDao;
import com.collective.resources.InstanceManager;
import com.collective.resources.Result;
import com.collective.resources.search.model.SearchCreationBean;
import com.sun.jersey.api.core.InjectParam;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserSearchResource {

    private static final String key = "search";

    private static final Logger logger =
            Logger.getLogger(UserSearchResource.class);

    @InjectParam
    private InstanceManager instanceManager;

    @GET
    @Path("/searches/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Search> getAllSearchesByUserId(
            @PathParam("userId") long userId) {

        logger.debug("getting all the searches");

        SearchDao searchDao = instanceManager.getSearchDao();
        ArrayList<Search> result = new ArrayList<Search>();
        try {
             result.addAll(searchDao.selectByUserId(userId));
        } catch (Exception e) {
            throw new RuntimeException("Error while getting user's searches",
                    e);
        }

        if(result != null) {
            return result;
        }

        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/search")
    public Result saveUserSearch(SearchCreationBean searchCreationBean) {

        logger.debug("reading searchCreationBean: " + searchCreationBean);

        // model should include the search and the userId, serialized in json

        Long userIdLong = searchCreationBean.getUserId();

        Search searchToSave = new Search();
        searchToSave.setTitle(searchCreationBean.getTitle());

        ArrayList<LabelledURI> commonConcepts = new ArrayList<LabelledURI>();
        ArrayList<LabelledURI> customConcepts = new ArrayList<LabelledURI>();
        commonConcepts.addAll(searchCreationBean.getCommonConcepts());
        customConcepts.addAll(searchCreationBean.getCustomConcepts());

        searchToSave.setCommonUris(commonConcepts);
        searchToSave.setCustomUris(customConcepts);
        searchToSave.setLastProfilationDate(new DateTime());

        logger.debug("saving new search on its table using SearchDao");

        // save search
        instanceManager.getSearchDao().insert(searchToSave);

        logger.debug("saved search with: " + searchToSave.getId());
        // save search ownership to join table on database
        logger.debug("saving new search ownership on its user search table");
        instanceManager.getSearchDao().insertUserSearch(userIdLong, searchToSave.getId());

        return new Result(Result.Status.OK, "User search successfully created");
    }
}
