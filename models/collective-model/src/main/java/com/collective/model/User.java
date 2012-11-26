package com.collective.model;

import java.util.List;
import java.util.UUID;
import org.joda.time.DateTime;

import com.google.gson.annotations.SerializedName;


/**
 * This class models all the information needed to profile a generic <i>User</i> how
 * it has been defined within the <i>Collective Conceptual Model</i>.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class User extends Agent {

    /**
     * a free-text human readable description of what are user's main skills.
     */
    @SerializedName("skills_text")
    private String skills;

    /**
     * a text that contains a list of keywords separated by commas that represent a summary of user's skills
     */
    @SerializedName("skills")
    private String skillsKeywords;

    /**
     * a free-text human readable description of what are the user's main interests.
     */
    @SerializedName("interests_text")
    private String interests;

    /**
     * a text that contains a list of keywords separated by commas that represent a summary of user's interests
     */
    @SerializedName("interests")
    private String interestsKeywords;

    /**
     * a list of {@link com.collective.model.Keyword}s intended to be a set of tags
     * each of them univocally identified in our knowledge base. 
     */
    private List<Keyword> keywords;

    /**
     * a list of {@link com.collective.model.ProjectInvolvement}s binding
     * a {@link com.collective.model.User} to a {@link com.collective.model.Project}
     * specifying a {@link com.collective.model.Role}.
     */
    //TODO: remove? in the received message it is not received
    private List<ProjectInvolvement> projectInvolvement;

    /**
     * it indicates the last date when a profiling occurred.
     */
    private DateTime lastProfilationDate;

    public User() {
        super();
    }

    public User(Long id) {
        super(id);
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getSkillsKeywords() {
        return skillsKeywords;
    }

    public void setSkillsKeywords(String skillsKeywords) {
        this.skillsKeywords = skillsKeywords;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getInterestsKeywords() {
        return interestsKeywords;
    }

    public void setInterestsKeywords(String interestsKeywords) {
        this.interestsKeywords = interestsKeywords;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public boolean addKeyword(Keyword keyword) {
        return this.keywords.add(keyword);
    }

    public List<ProjectInvolvement> getProjectInvolvements() {
        return projectInvolvement;
    }

    public void setProjectInvolvements(List<ProjectInvolvement> projectInvolvements) {
        this.projectInvolvement = projectInvolvements;
    }

    public boolean addProjectInvolvement(ProjectInvolvement projectInvolvement) {
        return this.projectInvolvement.add(projectInvolvement);
    }

    public DateTime getLastProfilationDate() {
        return lastProfilationDate;
    }

    public void setLastProfilationDate(DateTime lastProfilationDate) {
        this.lastProfilationDate = lastProfilationDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "skills='" + skills + '\'' +
                ", skillsKeywords='" + skillsKeywords + '\'' +
                ", interests='" + interests + '\'' +
                ", interestsKeywords='" + interestsKeywords + '\'' +
                ", keywords=" + keywords +
                ", projects=" + projectInvolvement +
                ", lastProfilationDate=" + lastProfilationDate +
                "} " + super.toString();
    }
}