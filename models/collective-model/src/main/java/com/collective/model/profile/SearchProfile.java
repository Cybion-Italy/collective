package com.collective.model.profile;

import com.collective.rdfizer.annotations.RDFClassType;
import com.collective.rdfizer.annotations.RDFIdentifier;
import com.collective.rdfizer.annotations.RDFProperty;
import com.google.gson.annotations.Expose;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 * Represents the profile of a search instantiated to deliver results over time.
 */
@RDFClassType(type = "http://collective.com/profile/search")
public class SearchProfile implements Profile {

    @Expose
    private Long id;
    @Expose
    private String title;
    @Expose
    private List<URI> concepts = new ArrayList<URI>();

    @RDFIdentifier
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @RDFProperty(properties = {"http://purl.org/dc/terms/title"})
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @RDFProperty( properties = { "http://collective.com/SearchProfile#searchConcept" } )
    public List<URI> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<URI> concepts) {
        this.concepts = concepts;
    }

     public void addConcept(URI concept) throws URISyntaxException {
        this.concepts.add(concept);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchProfile that = (SearchProfile) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    public int hashCode() {
        return id.hashCode();
    }

    public String toString() {
        return "SearchProfile{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", concepts=" + concepts +
                '}';
    }
}
