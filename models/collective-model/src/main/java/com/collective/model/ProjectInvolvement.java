package com.collective.model;

import org.joda.time.DateTime;
import org.nnsoft.be3.annotations.RDFClassType;
import org.nnsoft.be3.annotations.RDFIdentifier;
import org.nnsoft.be3.annotations.RDFProperty;

import java.io.Serializable;
import java.net.URI;
import java.util.UUID;

/**
 * This class associated a {@link com.collective.model.User} to a {@link com.collective.model.Project}
 * specifying the {@link com.collective.model.Role} he has in it.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
@RDFClassType(type = "http://collective.com/ProjectInvolvement")
public class ProjectInvolvement implements Serializable {

    private Long id;

    /**
     * The URI of the {@link com.collective.model.profile.UserProfile} involved.
     */
    private URI userProfile;

    /**
     * A {@link com.collective.model.Role} covered by the user participating in the
     * related {@link com.collective.model.Project}.
     */
//    private Role role;

    private String role;

    /**
     * The date when the {@link com.collective.model.User}
     * joined the {@link com.collective.model.Project}.
     */
    private DateTime since;

    /**
     * The date when the {@link com.collective.model.User} left the project.
     * It may be <code>null</code>
     */
    private DateTime to;

    @RDFIdentifier
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @RDFProperty( properties = { "http://collective.com/ProjectInvolvement/role" } )
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public DateTime getSince() {
        return since;
    }

    public void setSince(DateTime since) {
        this.since = since;
    }

    public DateTime getTo() {
        return to;
    }

    public void setTo(DateTime to) {
        this.to = to;
    }

    @RDFProperty( properties = { "http://collective.com/ProjectInvolvement/user" } )
    public URI getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(URI userProfile) {
        this.userProfile = userProfile;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectInvolvement that = (ProjectInvolvement) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public String toString() {
        return "ProjectInvolvement{" +
                "id=" + id +
                ", userProfile=" + userProfile +
                ", role=" + role +
                ", since=" + since +
                ", to=" + to +
                '}';
    }
}
