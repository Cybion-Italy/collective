package com.collective.usertests.model;

import com.google.gson.annotations.Expose;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class Reason {

    //always annotate what is going to be exposed in webservices
    @Expose
    private int id;

    @Expose
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reason reason = (Reason) o;

        if (id != reason.id) return false;
        if (description != null ? !description.equals(reason.description) : reason.description != null) return false;

        return true;
    }

    public int hashCode() {
        int result = id;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "Reason{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
