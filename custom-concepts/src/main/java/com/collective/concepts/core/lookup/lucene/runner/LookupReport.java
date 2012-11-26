package com.collective.concepts.core.lookup.lucene.runner;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LookupReport {

    private String matcher;

    private boolean found;

    public LookupReport(String matcher, boolean found) {
        this.matcher = matcher;
        this.found = found;
    }

    @Override
    public String toString() {
        return "LookupReport{" +
                "matcher='" + matcher + '\'' +
                ", found=" + found +
                '}';
    }
}
