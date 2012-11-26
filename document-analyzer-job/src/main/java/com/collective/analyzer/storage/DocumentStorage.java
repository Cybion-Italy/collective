package com.collective.analyzer.storage;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public interface DocumentStorage {

    public void storeWebResource(WebResourceEnhanced enhancedWebResource)
            throws DocumentStorageException;

    public void storeUserWebResource(WebResourceEnhanced webResourceEnhanced, Long userId)
            throws DocumentStorageException;

    public void deleteWebResource(WebResourceEnhanced enhancedWebResource)
            throws DocumentStorageException;
}
