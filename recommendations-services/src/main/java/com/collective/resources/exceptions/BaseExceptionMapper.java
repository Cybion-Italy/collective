package com.collective.resources.exceptions;

import javax.ws.rs.ext.ExceptionMapper;

public abstract class BaseExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {

    /**
     * @param e input error.
     * @return a message composed of all the causes of the exception.
     */
    protected String getErrorMessage(Exception e) {
        StringBuilder sb = new StringBuilder();
        Throwable cause = e;
        while (cause != null) {
            sb.append("[")
                    .append(cause.getClass().getName())
                    .append(":")
                    .append(cause.getStackTrace()[0].getFileName())
                    .append(":")
                    .append(cause.getStackTrace()[0].getMethodName())
                    .append(":")
                    .append(cause.getStackTrace()[0].getLineNumber())
                    .append(": ")
                    .append(cause.getMessage())
                    .append("]\n");
            cause = cause.getCause();
        }
        return sb.toString();
    }
}