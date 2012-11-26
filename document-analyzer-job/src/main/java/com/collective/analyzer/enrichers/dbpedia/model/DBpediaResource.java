package com.collective.analyzer.enrichers.dbpedia.model;

import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class DBpediaResource {

    @SerializedName("@URI")
    private URI uri;

    public DBpediaResource() {}

    public DBpediaResource(String uri) throws URISyntaxException {
        this.uri = new URI(uri);
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBpediaResource that = (DBpediaResource) o;

        if (uri != null ? !uri.equals(that.uri) : that.uri != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uri != null ? uri.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DBpediaResource{" +
                "uri=" + uri +
                '}';
    }
}
