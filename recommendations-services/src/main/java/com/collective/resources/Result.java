package com.collective.resources;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Defines the result of a processing.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Produces(MediaType.APPLICATION_JSON)
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Result {

    public enum Status {
        OK,
        FAIL
    }

    private Status status;
    private String message;

    public Result() {}

    public Result(Status s, String m) {
        status = s;
        message = m;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
