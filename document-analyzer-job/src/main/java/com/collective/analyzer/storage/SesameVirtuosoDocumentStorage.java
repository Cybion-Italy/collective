package com.collective.analyzer.storage;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.analyzer.configuration.DocumentStorageConfiguration;
import org.apache.log4j.Logger;
import org.nnsoft.be3.Be3;
import org.nnsoft.be3.RDFizerException;
import org.nnsoft.be3.annotations.RDFClassType;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import virtuoso.sesame2.driver.VirtuosoRepository;

import java.net.URI;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class SesameVirtuosoDocumentStorage implements DocumentStorage {

    private static  Logger logger = Logger.getLogger(SesameVirtuosoDocumentStorage.class);

    protected Be3 rdfizer;

    public URI defaultGraph;

    protected URI usersAnnotationsTemplate;

    protected Repository repository;

    /* used for tests */
    public SesameVirtuosoDocumentStorage(Be3 rdfizer,
                                         URI defaultGraph,
                                         URI usersAnnotationsTemplate) {
        this.rdfizer = rdfizer;
        this.defaultGraph = defaultGraph;
        this.usersAnnotationsTemplate = usersAnnotationsTemplate;
    }

    public SesameVirtuosoDocumentStorage(
            DocumentStorageConfiguration documentStorageConfiguration,
            Be3 rdfizer,
            URI defaultGraph,
            URI usersAnnotationsTemplate
    ) {

        this.rdfizer = rdfizer;
        this.defaultGraph = defaultGraph;

        //init a virtuoso repository
        this.repository = getRepository(documentStorageConfiguration);

        this.usersAnnotationsTemplate = usersAnnotationsTemplate;

        try {
            repository.initialize();
            logger.info("Virtuoso connection initialized.");
        } catch (RepositoryException e) {
            final String errMsg = "Error while initializing connection to Virtuoso";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }

    private Repository getRepository(DocumentStorageConfiguration documentStorageConfiguration) {
        return new VirtuosoRepository(
                "jdbc:virtuoso://" + documentStorageConfiguration.getHost()
                        + ":" + documentStorageConfiguration.getPort(),
                documentStorageConfiguration.getUser(),
                documentStorageConfiguration.getPassword()
        );
    }

    public void storeWebResource(WebResourceEnhanced enhancedWebResource)
            throws DocumentStorageException {
        RepositoryConnection repositoryConnection = getConnection();
        List<Statement> statements;
        try {
            statements = rdfizer.getRDFStatements(enhancedWebResource);
        } catch (RDFizerException e) {
            final String errMsg = "Error while serializing " +
                    "enhancedWebResource: '" + enhancedWebResource + "'";
            logger.error(errMsg, e);
            throw new DocumentStorageException(errMsg, e);
        }
        for (Statement statement : statements) {
            try {
                repositoryConnection.add(statement,
                                          new URIImpl(defaultGraph.toString()));
            } catch (RepositoryException e) {
                final String errMsg = "Error while adding RDF statement: '"
                        + statement + "' for enhancedWebResource: '"
                                    + enhancedWebResource + "'";
                logger.debug(errMsg, e);
                continue;
            }
        }

        try {
            repositoryConnection.commit();
        } catch (RepositoryException e) {
            final String errMsg = "Error while committing " +
                    "enhancedWebResource: '" + enhancedWebResource
                    + "' on Virtuoso";
            logger.error(errMsg, e);
            throw new DocumentStorageException(errMsg, e);
        }
        logger.info("enhancedWebResource: '" + enhancedWebResource
                + "' successfully stored on default graph: '"
                + this.defaultGraph.toString() + "'");
        releaseConnection(repositoryConnection);
    }

    public void deleteWebResource(WebResourceEnhanced enhancedWebResource)
            throws DocumentStorageException {
        RepositoryConnection repositoryConnection = getConnection();
        List<Statement> statements;
        try {
            statements = rdfizer.getRDFStatements(enhancedWebResource);
        } catch (RDFizerException e) {
            final String errMsg = "Error while serializing " +
                    "enhancedWebResource: '" + enhancedWebResource + "'";
            logger.error(errMsg, e);
            throw new DocumentStorageException(errMsg, e);
        }
        for (Statement statement : statements) {
            try {
                repositoryConnection.remove(statement,
                                        new URIImpl(defaultGraph.toString()));
            } catch (RepositoryException e) {
                final String errMsg = "Error while removing RDF statement: '"
                        + statement + "' when deleting enhancedWebResource: '"
                        + enhancedWebResource + "'";
                logger.debug(errMsg, e);
                continue;
            }
        }

        try {
            repositoryConnection.commit();
        } catch (RepositoryException e) {
            final String errMsg = "Error while committing the deletion " +
                    "of enhancedWebResource: '" + enhancedWebResource
                    + "' on Virtuoso";
            logger.error(errMsg, e);
            throw new DocumentStorageException(errMsg, e);
        }
        logger.info("enhancedWebResource: '" + enhancedWebResource
                + "' successfully deleted from default graph: '"
                + this.defaultGraph.toString() + "'");
        releaseConnection(repositoryConnection);

    }

    public void storeUserWebResource(
            WebResourceEnhanced webResourceEnhanced,
            Long userId
    ) throws DocumentStorageException {
        RepositoryConnection repositoryConnection = getConnection();
        List<Statement> statements;
        try {
            statements = rdfizer.getRDFStatements(webResourceEnhanced);
        } catch (RDFizerException e) {
            final String errMsg = "Error while serializing enhancedWebResource: '"
                    + webResourceEnhanced + "'";
            logger.error(errMsg, e);
            throw new DocumentStorageException(errMsg, e);
        }
        for (Statement statement : statements) {
            try {
                repositoryConnection.add(statement,
                        new URIImpl(usersAnnotationsTemplate.toString() + userId));
            } catch (RepositoryException e) {
                final String errMsg = "Error while adding RDF statement: '"
                        + statement + "' when saving enhancedWebResource: '"
                        + webResourceEnhanced + "' to graph '"
                        + usersAnnotationsTemplate.toString() + userId + "'";
                logger.debug(errMsg, e);
                continue;
            }
        }
        try {
            repositoryConnection.commit();
        } catch (RepositoryException e) {
            final String errMsg = "Error while committing enhancedWebResource: '"
                    + webResourceEnhanced + "' on Virtuoso";
            logger.error(errMsg, e);
            throw new DocumentStorageException(errMsg, e);
        }
        logger.info("enhancedWebResource: '" + webResourceEnhanced
                + "' successfully stored on user annotations graph: '"
                + this.defaultGraph.toString() + "'");
        releaseConnection(repositoryConnection);
    }

    protected RepositoryConnection getConnection()
            throws DocumentStorageException {
        try {
            return repository.getConnection();
        } catch (RepositoryException e) {
            final String errMsg = "Error while getting session on Virtuoso";
            logger.error(errMsg, e);
            throw new DocumentStorageException("Error while " +
                    "getting session on Virtuoso", e);
        }
    }

    protected void releaseConnection(RepositoryConnection repositoryConnection)
            throws DocumentStorageException {
        try {
            repositoryConnection.close();
        } catch (RepositoryException e) {
            final String errMsg = "Error while closing session on Virtuoso";
            logger.error(errMsg);
            throw new DocumentStorageException(errMsg, e);
        }
    }

    private static org.openrdf.model.URI getIdentifierURI(Class clazz, URI id) {
        String[] idString = id.toString().split("/");
        String lastPart = idString[idString.length - 1];
        RDFClassType rdfClassType = (RDFClassType) clazz.getAnnotation(RDFClassType.class);
        logger.debug("building uriImpl " + rdfClassType.type() + '/' + lastPart);
        return new URIImpl(rdfClassType.type() + '/' + lastPart);
    }
}
