package com.collective.concepts.core.lookup;

import com.collective.concepts.core.Concept;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ConceptLookup {

    private String ngram;

    private Concept concept;

    public ConceptLookup(String ngram, Concept concept) {
        this.ngram = ngram;
        this.concept = concept;
    }

    public String getNgram() {
        return ngram;
    }

    public Concept getConcept() {
        return concept;
    }

    @Override
    public String toString() {
        return "ConceptLookup{" +
                "ngram='" + ngram + '\'' +
                ", concept=" + concept +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConceptLookup)) return false;

        ConceptLookup that = (ConceptLookup) o;

        if (concept != null ? !concept.equals(that.concept) : that.concept != null) return false;
        if (ngram != null ? !ngram.equals(that.ngram) : that.ngram != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ngram != null ? ngram.hashCode() : 0;
        result = 31 * result + (concept != null ? concept.hashCode() : 0);
        return result;
    }
}
