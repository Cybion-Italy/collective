package com.collective.resources.search.model;

import com.collective.permanentsearch.model.LabelledURI;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class SearchCreationBean {

    @Expose
    private Long userId;

    @Expose
    private String title;

    @Expose
    private List<LabelledURI> commonConcepts;

    @Expose
    private List<LabelledURI> customConcepts;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LabelledURI> getCommonConcepts() {
        return commonConcepts;
    }

    public void setCommonConcepts(List<LabelledURI> commonConcepts) {
        this.commonConcepts = commonConcepts;
    }

    public List<LabelledURI> getCustomConcepts() {
        return customConcepts;
    }

    public void setCustomConcepts(List<LabelledURI> customConcepts) {
        this.customConcepts = customConcepts;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchCreationBean that = (SearchCreationBean) o;

        if (commonConcepts != null ? !commonConcepts.equals(that.commonConcepts) : that.commonConcepts != null)
            return false;
        if (customConcepts != null ? !customConcepts.equals(that.customConcepts) : that.customConcepts != null)
            return false;
        if (title != null ? !title.equals(that.title) : that.title != null)
            return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (commonConcepts != null ? commonConcepts.hashCode() : 0);
        result = 31 * result + (customConcepts != null ? customConcepts.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "SearchCreationBean{" +
                "userId=" + userId +
                ", title='" + title + '\'' +
                ", commonConcepts=" + commonConcepts +
                ", customConcepts=" + customConcepts +
                '}';
    }
}
