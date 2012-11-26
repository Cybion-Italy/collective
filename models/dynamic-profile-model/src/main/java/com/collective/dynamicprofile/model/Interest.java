package com.collective.dynamicprofile.model;

import com.collective.permanentsearch.model.LabelledURI;
import com.google.gson.annotations.Expose;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class Interest
{

    @Expose
    private final float weight;

    @Expose
    private final LabelledURI concept;

    public Interest(float weight, final LabelledURI concept)
    {
        this.weight = weight;
        this.concept = concept;
    }

    public float getWeight()
    {
        return weight;
    }

    public LabelledURI getConcept()
    {
        return concept;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interest interest = (Interest) o;

        if (Float.compare(interest.weight, weight) != 0) return false;
        if (concept != null ? !concept.equals(interest.concept) : interest.concept != null)
            return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = (weight != +0.0f ? Float.floatToIntBits(weight) : 0);
        result = 31 * result + (concept != null ? concept.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Interest{" +
                "weight=" + weight +
                ", concept=" + concept +
                '}';
    }
}
