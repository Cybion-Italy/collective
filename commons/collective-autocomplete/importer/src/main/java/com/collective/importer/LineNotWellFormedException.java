package com.collective.importer;

/**
 * @auuthor Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LineNotWellFormedException extends Exception {

    public LineNotWellFormedException(String message, Exception e) {
        super(message, e);
    }
}
