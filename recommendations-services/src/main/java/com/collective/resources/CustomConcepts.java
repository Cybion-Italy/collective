package com.collective.resources;

import com.collective.concepts.core.Concept;
import com.collective.concepts.core.UserDefinedConceptStore;
import com.collective.concepts.core.UserDefinedConceptStoreException;
import com.sun.jersey.api.core.InjectParam;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Path("/concepts")
@Produces(MediaType.APPLICATION_JSON)
public class CustomConcepts {

    private static Logger logger = Logger.getLogger(CustomConcepts.class);
    private static final String UTF_ENCODING = "UTF-8";

    @InjectParam
    private InstanceManager instanceManager;

    public CustomConcepts() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{company}/{owner}/{name}/{label}")
    public Result storeConcept(
            @PathParam("company") String company,
            @PathParam("owner")   String owner,
            @PathParam("name")    String name,
            @PathParam("label")   String label,
            @FormParam("user")    long   userId,
            @FormParam("description") String description,
            @FormParam("keywords")    String keywords) {

        UserDefinedConceptStore store = instanceManager.getConceptStore();

        String decodedName  = "";
        String decodedLabel = "";

        try {
            decodedName  = URLDecoder.decode(name,  UTF_ENCODING);
            decodedLabel = URLDecoder.decode(label, UTF_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error while parsing concept from url",
                                        e);
        }

        logger.debug("userId: " + userId);
        logger.debug("description: " + description);
        logger.debug("keywords: " + keywords);

        Concept c = new Concept(company, owner, decodedName, decodedLabel);
        c.setVisibility(Concept.Visibility.PUBLIC);
        c.setDescription(description);
        String kws[] = keywords.trim().split(",");
        for (String keyword : kws) {
            c.addKeyword(keyword);
        }

        logger.debug("created concept: " + c.toString());
        try {
            store.storeConcept(userId, c);
        } catch (UserDefinedConceptStoreException e) {
            throw new RuntimeException("Error while storing concept", e);
        }
        return new Result(Result.Status.OK, "Concept successfully stored");
    }

    @GET
    @Path("/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Concept> getConcepts(
            @PathParam("user") long userId
    ) {
        logger.debug("looking for concepts of userId: " + userId);
        UserDefinedConceptStore store = instanceManager.getConceptStore();
        List<Concept> result;
        try {
             result = store.getUserConcepts(userId);
        } catch (UserDefinedConceptStoreException e) {
            throw new RuntimeException("Error while storing concept", e);
        }
        if(result != null) {
            return result;
        }
        return new ArrayList<Concept>();
    }

    /* TODO med : add PUT to edit custom concept */

    @DELETE
    @Path("/{company}/{owner}/{name}/{label}")
    @Produces(MediaType.APPLICATION_JSON)
    public Result deleteConcept(
            @PathParam("company") String company,
            @PathParam("owner")   String owner,
            @PathParam("name")    String name,
            @PathParam("label")   String label
    ) {

        String decodedName  = "";
        String decodedLabel = "";

        try {
            decodedName  = URLDecoder.decode(name,  UTF_ENCODING);
            decodedLabel = URLDecoder.decode(label, UTF_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error while parsing concept from url",
                                        e);
        }

        Concept concept = new Concept(company, owner, decodedName, decodedLabel);

        logger.debug("deleting concept: " + concept.toString());
        logger.debug("of user: " + owner);

        UserDefinedConceptStore store = instanceManager.getConceptStore();
        try {
            store.deleteConcept(Long.parseLong(owner), concept.getURL());
        } catch (UserDefinedConceptStoreException e) {
            throw new RuntimeException("Error while deleting concept: '"
                                                   + concept.getURL() + "'", e);
        }
        return new Result(Result.Status.OK, "Concept successfully deleted");
    }

    @DELETE
    @Path("/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public Result deleteConcept(
            @PathParam("user") long user
    ) {
        UserDefinedConceptStore store = instanceManager.getConceptStore();
        try {
            store.deleteAllConcepts(user);
        } catch (UserDefinedConceptStoreException e) {
            throw new RuntimeException(
                    "Error while deleting concepts for user: '" + user + "'", e);
        }
        return new Result(Result.Status.OK, "All the concepts have been deleted");
    }
}
