package com.collective.resources.responses;

import com.google.gson.annotations.Expose;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class ActivityServiceResponse extends ServiceResponse<String>
{
    //user who owned the activities
    @Expose
    private String userId;

    public ActivityServiceResponse(Status status, String message, String userId) {
        super(status, message);
        this.userId = userId;
    }

    @Override
    public String getObject()
    {
        return userId;
    }
}
