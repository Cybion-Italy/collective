package com.collective.resources.responses;

import com.google.gson.annotations.Expose;

/**
 * All service responses will extend from this base abstract class.
 * The common fields are status, message and object.
 *
 * Every extending subcass will hold a specific object.
 *
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public abstract class ServiceResponse<T>
{

    public enum Status {
        OK,
        NOK
    }

    @Expose
    private Status status;

    @Expose
    private String message;

    public ServiceResponse() {}

    public ServiceResponse(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public ServiceResponse(String message) {
        this(Status.OK, message);
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public abstract T getObject();

}
