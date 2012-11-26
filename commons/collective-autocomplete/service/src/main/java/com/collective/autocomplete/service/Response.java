package com.collective.autocomplete.service;

import com.collective.importer.Line;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Response {

    private final static String OK = "OK";

    private final static String NOK = "NOK";

    private String status;

    private String errorMessage;

    private int numberOfResults;

    private List<Line> suggestions = new ArrayList<Line>();

    public Response(List<Line> suggestions) {
        this.suggestions = suggestions;
        this.numberOfResults = suggestions.size();
        this.status = OK;
        this.errorMessage = "";
    }

    public Response(String message) {
        this.errorMessage = message;
        this.status = NOK;
        this.numberOfResults = 0;
    }

    public static Response ok(List<Line> suggestions) {
        Response response = new Response(suggestions);
        return response;
    }

    public static Response error(String message) {
        Response response = new Response(message);
        return response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumberOfResults() {
        return numberOfResults;
    }

    public void setNumberOfResults(int numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    public List<Line> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Line> suggestions) {
        this.suggestions = suggestions;
    }
}
