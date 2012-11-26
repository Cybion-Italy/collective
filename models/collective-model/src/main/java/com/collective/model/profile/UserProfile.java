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
 * This Class models  a <i>Profile</i> for a {@link com.collective.model.User}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
@RDFClassType(type = "http://xmlns.com/foaf/0.1/Person")
public class UserProfile implements Profile {

    //always annotate explicitly every field that needs to be exposed in services
    @Expose
    private Long id;

    private List<URI> skills = new ArrayList<URI>();

    private List<URI> interests = new ArrayList<URI>();

    @RDFIdentifier
    public Long getId() {
        return id;
    }

    public void setId(Long id) throws URISyntaxException {
        this.id = id;
    }

    @RDFProperty( properties = { "http://purl.org/ontology/cco/core#skill" } )
    public List<URI> getSkills() {
        return skills;
    }

    public void setSkills(List<URI> skills) {
        this.skills = skills;
    }

    public void addSkill(URI skill) {
        skills.add(skill);
    }

    @RDFProperty( properties = { "http://purl.org/ontology/cco/core#interest" } )
    public List<URI> getInterests() {
        return interests;
    }

    public void setInterests(List<URI> interests) {
        this.interests = interests;
    }

    public void addInterest(URI interest) {
        interests.add(interest);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserProfile profile = (UserProfile) o;

        if (id != null ? !id.equals(profile.id) : profile.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", skills=" + skills +
                ", interests=" + interests +
                '}';
    }
}
