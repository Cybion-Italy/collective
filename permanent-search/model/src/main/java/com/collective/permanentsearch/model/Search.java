package com.collective.permanentsearch.model;

import com.google.gson.annotations.Expose;
import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class Search {

    @Expose
    private long id;

    @Expose
    private String title;

    @Expose
    private ArrayList<LabelledURI> commonUris = new ArrayList<LabelledURI>();

    @Expose
    private ArrayList<LabelledURI> customUris = new ArrayList<LabelledURI>();

    private DateTime lastProfilationDate;

    public Search() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<LabelledURI> getCommonUris() {
        return commonUris;
    }

    public void setCommonUris(ArrayList<LabelledURI> commonUris) {
        this.commonUris = commonUris;
    }

    public void addCommonUri(LabelledURI labelledURI) {
        this.commonUris.add(labelledURI);
    }

    public void addCustomUri(LabelledURI labelledURI) {
        this.customUris.add(labelledURI);
    }

    public DateTime getLastProfilationDate() {
        return lastProfilationDate;
    }

    public void setLastProfilationDate(DateTime lastProfilationDate) {
        this.lastProfilationDate = lastProfilationDate;
    }

    public ArrayList<LabelledURI> getCustomUris() {
        return customUris;
    }

    public void setCustomUris(ArrayList<LabelledURI> customUris) {
        this.customUris = customUris;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Search search = (Search) o;

        if (id != search.id) return false;
        if (commonUris != null ? !commonUris.equals(search.commonUris) : search.commonUris != null)
            return false;
        if (customUris != null ? !customUris.equals(search.customUris) : search.customUris != null)
            return false;
        if (lastProfilationDate != null ? !lastProfilationDate.equals(search.lastProfilationDate) : search.lastProfilationDate != null)
            return false;
        if (title != null ? !title.equals(search.title) : search.title != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (commonUris != null ? commonUris.hashCode() : 0);
        result = 31 * result + (customUris != null ? customUris.hashCode() : 0);
        result = 31 * result + (lastProfilationDate != null ? lastProfilationDate.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "Search{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", commonUris=" + commonUris +
                ", customUris=" + customUris +
                ", lastProfilationDate=" + lastProfilationDate +
                '}';
    }
}
