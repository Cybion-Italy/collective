package com.collective.model;

import java.util.List;

/**
 * A keyword is just a string associated with a {@link com.collective.model.Concept}
 * representing a user's skill or interest in a concise way.
 *
 * Examples of keywords are:
 * <ul>
 *      <li>linked data</li>
 *      <li>knowledge representation</li>
 *      <li>project management</li>
 * </ul>
 * and are intended to be selected manually by the user from a list of choices
 * already present within the system.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Keyword {

    private String ngram;

    private List<Concept> concepts;

    public String getNgram() {
        return ngram;
    }

    public void setNgram(String ngram) {
        this.ngram = ngram;
    }

    public List<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<Concept> concepts) {
        this.concepts = concepts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Keyword keyword = (Keyword) o;

        if (!ngram.equals(keyword.ngram)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ngram.hashCode();
    }

    @Override
    public String toString() {
        return "Keyword{" +
                "ngram='" + ngram + '\'' +
                ", concepts=" + concepts +
                '}';
    }

}
