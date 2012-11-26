package com.collective.importer.indexer;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class MySqlImporterException extends Exception {

    public MySqlImporterException(String message, Exception e) {
        super(message, e);
    }
}
