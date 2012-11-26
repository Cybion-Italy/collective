package com.collective.model;

import com.collective.model.profile.ProjectProfile;
import com.collective.rdfizer.annotations.RDFClassType;
import com.collective.rdfizer.annotations.RDFIdentifier;
import com.collective.rdfizer.annotations.RDFProperty;
import com.google.gson.annotations.SerializedName;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A project how is defined in the <i>Collective Conceptual Model</i>.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
//TODO: add annotations of the Collective Ontology
public class Project implements Serializable {

    private Long id;

    /**
     * 
     */
    private String manifesto;

    /**
     * list of keywords separated by commas that sum up the manifesto
     */
    @SerializedName("goals")
    private String manifestoKeywords;

    @SerializedName("members")
    private List<String> membersIds = new ArrayList<String>();

    private List<Problem> problems;

    private ProjectProfile projectProfile;

    private DateTime lastProfilationDate;


    public Project() {
        super();
    }

    public Project(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManifesto() {
        return manifesto;
    }

    public void setManifesto(String manifesto) {
        this.manifesto = manifesto;
    }

    public String getManifestoKeywords() {
        return manifestoKeywords;
    }

    public void setManifestoKeywords(String manifestoKeywords) {
        this.manifestoKeywords = manifestoKeywords;
    }

    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }

    public ProjectProfile getProjectProfile() {
        return projectProfile;
    }

    public void setProjectProfile(ProjectProfile projectProfile) {
        this.projectProfile = projectProfile;
    }

    public DateTime getLastProfilationDate() {
        return lastProfilationDate;
    }

    public void setLastProfilationDate(DateTime lastProfilationDate) {
        this.lastProfilationDate = lastProfilationDate;
    }

    public List<String> getMembersIds() {
        return membersIds;
    }

    public void setMembersIds(List<String> membersIds) {
        this.membersIds = membersIds;
    }

    public String toString() {
        return "Project{" +
                "id=" + id +
                ", manifesto='" + manifesto + '\'' +
                ", manifestoKeywords='" + manifestoKeywords + '\'' +
                ", membersIds=" + membersIds +
                ", problems=" + problems +
                ", projectProfile=" + projectProfile +
                ", lastProfilationDate=" + lastProfilationDate +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (id != null ? !id.equals(project.id) : project.id != null) return false;

        return true;
    }

    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}