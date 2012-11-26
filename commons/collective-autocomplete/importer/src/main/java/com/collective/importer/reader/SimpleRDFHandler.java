package com.collective.importer.reader;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SimpleRDFHandler implements RDFHandler {

    private List<Statement> statements;

    public void startRDF() throws RDFHandlerException {
        statements = new ArrayList<Statement>();
    }

    public void endRDF() throws RDFHandlerException {}

    public void handleNamespace(String s, String s1) throws RDFHandlerException {}

    public void handleStatement(Statement statement) throws RDFHandlerException {
        statements.add(statement);
    }

    public void handleComment(String s) throws RDFHandlerException {}

    public List<Statement> getStatements() {
        return statements;
    }

}
