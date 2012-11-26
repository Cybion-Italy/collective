package com.collective.usertests.model;

import com.google.gson.annotations.Expose;
import org.joda.time.DateTime;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class UserFeedback {

    //always annotate explicitly every field that needs to be exposed in services
    @Expose
    private Long id;

    @Expose
    private Long userId;

    @Expose
    private Long projectId;

    @Expose
    private String urlResource;

    @Expose
    private boolean like;

    @Expose
    private int reasonId;

    private DateTime date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getUrlResource() {
        return urlResource;
    }

    public void setUrlResource(String urlResource) {
        this.urlResource = urlResource;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public int getReasonId() {
        return reasonId;
    }

    public void setReasonId(int reasonId) {
        this.reasonId = reasonId;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserFeedback that = (UserFeedback) o;

        if (like != that.like) return false;
        if (reasonId != that.reasonId) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (projectId != null ? !projectId.equals(that.projectId) : that.projectId != null) return false;
        if (urlResource != null ? !urlResource.equals(that.urlResource) : that.urlResource != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

        return true;
    }

    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (projectId != null ? projectId.hashCode() : 0);
        result = 31 * result + (urlResource != null ? urlResource.hashCode() : 0);
        result = 31 * result + (like ? 1 : 0);
        result = 31 * result + reasonId;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "UserFeedback{" +
                "id=" + id +
                ", userId=" + userId +
                ", projectId=" + projectId +
                ", urlResource='" + urlResource + '\'' +
                ", like=" + like +
                ", reasonId=" + reasonId +
                ", date=" + date +
                '}';
    }
}
