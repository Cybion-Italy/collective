package com.collective.concepts.core;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Concept implements Serializable {

    private static final long serialVersionUID = 7526472295622776147L;

    public static final String UTF_8 = "UTF-8";

    public enum Visibility implements Serializable {
        PUBLIC,
        PRIVATE
    }

    public static String BASE = "http://collective.com/concepts/";

    @Expose
    private String company;

    @Expose
    private String owner;

    @Expose
    private String name;

    @Expose
    private Visibility visibility;

    @Expose
    private List<String> keywords;

    @Expose
    private String label;

    @Expose
    private String description;

    @Expose
    private URL url;

    public Concept(String company, String owner, String name, String label) {
        this.company = company;
        this.owner = owner;
        this.name = name;
        this.label = label;

        String encodedCompany = "";
        String encodedOwner = "";
        String encodedName = "";

        try {
            encodedCompany = URLEncoder.encode(this.company, UTF_8);
            encodedOwner = URLEncoder.encode(this.owner, UTF_8);
            encodedName = URLEncoder.encode(this.name, UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("error while encoding variables for " +
                    "concept url: " + company + " " + owner + " "
                            + name + " " + label);
        }

        try {
            url = new URL(BASE + encodedCompany + "/" + encodedOwner
                               + "/" + encodedName);
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "URL: '" + BASE + encodedCompany + "/" + encodedOwner + "/"
                             + encodedName + " is not a valid concept identifier");
        }
        this.keywords = new ArrayList<String>();
    }

    public String getCompany() {
        return this.company;
    }

    public String getUser() {
        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    public URL getURL() {
        if (url == null) {
            throw new IllegalStateException("Have you used the constructor " +
                    "to build this instance?");
        }
        return url;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public boolean addKeyword(String s) {
        return keywords.add(s);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaximumNgramSize() {
        return Collections.max(keywords, new NgramComparator()).split(" ").length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Concept)) return false;

        Concept concept = (Concept) o;

        if (url != null ? !url.equals(concept.url) : concept.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Concept{" +
                "company='" + company + '\'' +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", visibility=" + visibility +
                ", keywords=" + keywords +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", url=" + url +
                '}';
    }

    public class NgramComparator implements Comparator<String> {

        public int compare(String s, String s1) {
            String[] s_grams = s.split(" ");
            String[] s1_grams = s1.split(" ");
            int difference = s_grams.length - s1_grams.length;
            if(difference == 0) {
                return 0;
            }
            if(difference < 0) {
                return -1;
            }
            return 1;
        }
    }
}
