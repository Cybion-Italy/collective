package com.collective.recommender.categories.exceptions;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class CategoriesMappingStorageException extends Exception {

    public CategoriesMappingStorageException(String emsg, Exception e) {
        super(emsg, e);
    }
}
