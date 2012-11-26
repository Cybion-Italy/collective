package com.collective.resources.responses;

import com.google.gson.annotations.Expose;
import org.apache.abdera2.activities.model.Activity;

import java.util.ArrayList;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class ArrayListActivityResponse extends ServiceResponse<ArrayList<Activity>>
{
    @Expose
    private ArrayList<Activity> activities;

    public ArrayListActivityResponse(Status status, String message, ArrayList<Activity> activities)
    {
        super(status, message);
        this.activities = activities;
    }

    @Override
    public ArrayList<Activity> getObject()
    {
        return this.activities;
    }
}
