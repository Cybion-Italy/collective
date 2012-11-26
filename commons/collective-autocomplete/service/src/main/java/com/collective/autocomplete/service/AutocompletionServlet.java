package com.collective.autocomplete.service;

import com.collective.importer.Line;
import com.collective.importer.indexer.MySqlImporter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class AutocompletionServlet extends HttpServlet {

    private MySqlImporter mySqlImporter;

    private Gson gson;
    private static final Logger logger = Logger.getLogger(AutocompletionServlet.class);

    @Override
    public void init() throws ServletException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        this.gson = gsonBuilder.create();
        mySqlImporter = new MySqlImporter();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String substring = request.getParameter("term");
        String callbackFunction = request.getParameter("callback");

        Response serviceResponse;
        long owner = 0;
        try {
            owner = Long.parseLong(request.getParameter("owner"));
        } catch (Exception e) {
            serviceResponse = Response.error(e.getMessage());
        }
        boolean filter = Boolean.parseBoolean(request.getParameter("filter"));
        if (substring == null) {
            serviceResponse = Response.error(" 'substring'  parameter cannot be null");
        }
        if (substring.length() == 0) {
            serviceResponse = Response.error("'term' parameter cannot be 0-sized");
        } else if (substring == null) {
            serviceResponse = Response.error("'term' parameter cannot be null");
        } else if (substring.length() < 4) {
            serviceResponse = Response.ok(new ArrayList<Line>());
        } else {
            List<Line> suggestions;
            try {
                suggestions = mySqlImporter.suggest(substring, owner, filter);
                serviceResponse = Response.ok(suggestions);
            } catch (Exception e) {
                serviceResponse = Response.error(e.getMessage());
            }
        }
        String jsonResponse = gson.toJson(serviceResponse);

        //wrap with JSONP function name
        if (callbackFunction != null) {
            if (callbackFunction.length() > 0) {
                String wrappedJsonResponse = "(" + jsonResponse + ")";
                jsonResponse = callbackFunction.concat(wrappedJsonResponse);
            }
        }


        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }

}