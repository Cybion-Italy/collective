package com.collective.resources.exceptions;

import com.collective.resources.Result;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper extends BaseExceptionMapper<RuntimeException> {

    public Response toResponse(RuntimeException re) {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity( new Result(Result.Status.FAIL, getErrorMessage(re)) )
            .build();
    }

}