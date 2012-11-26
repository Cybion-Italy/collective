package com.collective.analyzer.enrichers.dbpedia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class DBpediaAPIResponse {

    @Expose
    String text;

    @SerializedName("Resources")
    @Expose
    List<DBpediaResource> resources = new ArrayList<DBpediaResource>();

    public DBpediaAPIResponse(String text)
    {
        this.text = text;
    }

    public List<DBpediaResource> getResources() {
        return resources;
    }

    public String getText()
    {
        return text;
    }

    public void addDbpediaResource(DBpediaResource res) {
        this.resources.add(res);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBpediaAPIResponse that = (DBpediaAPIResponse) o;

        if (resources != null ? !resources.equals(that.resources) : that.resources != null)
            return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (resources != null ? resources.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "DBpediaAPIResponse{" +
                "text='" + text + '\'' +
                ", resources=" + resources +
                '}';
    }
}
