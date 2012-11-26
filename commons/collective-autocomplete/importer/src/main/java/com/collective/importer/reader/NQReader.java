package com.collective.importer.reader;

import com.collective.importer.Line;
import com.collective.importer.LineNotWellFormedException;
import org.deri.any23.parser.NQuadsParser;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class NQReader {

    private File input;

    private NQuadsParser nQuadsParser;

    private SimpleRDFHandler rdfHandler = new SimpleRDFHandler();

    private List<Statement> statements;

    private int index = 0;

    public NQReader(File input) {
        this.input = input;
        nQuadsParser = new NQuadsParser();
        nQuadsParser.setRDFHandler(rdfHandler);
    }

    public void parse() throws NQReaderException {
        InputStream is;
        try {
            is = new FileInputStream(input);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File '" + input + "' does not exist", e);
        }
        try {
            nQuadsParser.parse(is, "http://dbpedia.org");
        } catch (IOException e) {
            throw new NQReaderException("I/O error", e);
        } catch (RDFParseException e) {
            throw new NQReaderException("Error while parsing NQuads", e);
        } catch (RDFHandlerException e) {
            throw new NQReaderException("Error while handling RDF", e);
        }
        statements = rdfHandler.getStatements();
    }

    public boolean hasNextLine() {
        return index < statements.size();
    }

    public Line nextLine() throws NQReaderException {
        String uri, label;
        if (!hasNextLine()) {
            throw new IllegalStateException("It seems you already read all the lines");
        }
        Statement statement = statements.get(index);
        Resource s = statement.getSubject();
        Value o = statement.getObject();
        uri = s.stringValue();
        label = o.stringValue();
        Line line;
        try {
            line = Line.buildLine(uri, label);
        } catch (LineNotWellFormedException e) {
            throw new NQReaderException("Line no valid, skipped", e);
        }
        index++;
        return line;
    }


}
