package com.collective.resources.userfeedback;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.resources.InstanceManager;
import com.collective.usertests.model.Reason;
import com.sun.jersey.api.core.InjectParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
@Path("/user-feedback-test/reason")
@Consumes("application/json")
public class ReasonResource {

    @InjectParam
    private InstanceManager instanceManager;

    @GET
    @Path("/list")
    public List<Reason> getAvailableReasons() {
        List<Reason> reasonList = instanceManager.getReasonDao()
                .getAllReasons();
        return reasonList;
    }
}
