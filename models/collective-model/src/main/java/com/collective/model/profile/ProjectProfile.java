package com.collective.model.profile;

import com.collective.model.ProjectInvolvement;
import com.google.gson.annotations.Expose;
import org.nnsoft.be3.annotations.RDFClassType;
import org.nnsoft.be3.annotations.RDFIdentifier;
import org.nnsoft.be3.annotations.RDFProperty;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
@RDFClassType(type = "http://collective.com/profile/project")
public class ProjectProfile implements Profile {

    @Expose
    private Long id;

    @Expose
    private List<URI> manifestoConcepts = new ArrayList<URI>();

    private List<ProjectInvolvement> projectInvolvements = new ArrayList<ProjectInvolvement>();

    @RDFIdentifier
    public Long getId() {
        return id;
    }

    public void setId(Long id) throws URISyntaxException {
        this.id = id;
    }

    @RDFProperty( properties = { "http://collective.com/ProjectProfile#manifestoConcepts" } )
    public List<URI> getManifestoConcepts() {
        return manifestoConcepts;
    }

    public void setManifestoConcepts(List<URI> manifestoConcepts) {
        this.manifestoConcepts = manifestoConcepts;
    }

    public void addManifestoConcept(URI manifestoConcept) throws URISyntaxException {
        this.manifestoConcepts.add(manifestoConcept);
    }

    //TODO: single or plural in the ontology?
    @RDFProperty( properties = { "http://collective.com/ProjectProfile#projectInvolvements" } )
    public List<ProjectInvolvement> getProjectInvolvements() {
        return projectInvolvements;
    }

    public void setProjectInvolvements(List<ProjectInvolvement> projectInvolvements) {
        this.projectInvolvements = projectInvolvements;
    }

    public void addProjectInvolvement(ProjectInvolvement projectInvolvement) {
        this.projectInvolvements.add(projectInvolvement);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectProfile that = (ProjectProfile) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public String toString() {
        return "ProjectProfile{" +
                "id=" + id +
                ", manifestoConcepts=" + manifestoConcepts +
                ", projectInvolvements=" + projectInvolvements +
                '}';
    }
}
