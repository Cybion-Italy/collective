package com.collective.model;

import java.util.List;

/**
 * This class represents a problem how it has been defined in
 * the <i>Collective Conceptual Model</i>.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Problem {

    /**
     * a free-text human readable description of the problem's preconditions.
     */
    private String preconditions;

    /**
     * a free-text human readable description of the problem's post conditions.
     */
    private String postconditions;

    /**
     * a set of associated solutions that have been proved solve this problem.
     */
    private List<Solution> associatedSolutions;

    public String getPreconditions() {
        return preconditions;
    }

    public void setPreconditions(String preconditions) {
        this.preconditions = preconditions;
    }

    public String getPostconditions() {
        return postconditions;
    }

    public void setPostconditions(String postconditions) {
        this.postconditions = postconditions;
    }

    public List<Solution> getAssociatedSolutions() {
        return associatedSolutions;
    }

    public void setAssociatedSolutions(List<Solution> associatedSolutions) {
        this.associatedSolutions = associatedSolutions;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "preconditions='" + preconditions + '\'' +
                ", postconditions='" + postconditions + '\'' +
                ", associatedSolutions=" + associatedSolutions +
                '}';
    }
}
