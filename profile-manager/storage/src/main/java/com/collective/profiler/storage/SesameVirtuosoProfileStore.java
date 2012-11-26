package com.collective.profiler.storage;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.Profile;
import com.collective.model.profile.ProjectProfile;
import com.collective.model.profile.SearchProfile;
import com.collective.model.profile.UserProfile;
import com.collective.rdfizer.RDFizer;
import com.collective.rdfizer.RDFizerException;
import com.collective.rdfizer.annotations.RDFClassType;
import org.apache.log4j.Logger;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import virtuoso.sesame2.driver.VirtuosoRepository;
import org.openrdf.model.vocabulary.RDF;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * <a href="http://virtuoso.openlinksw.com">Virtuoso</a> and <a href="http://openrdf.org">Sesame</a>
 * based implementation of {@link com.collective.profiler.storage.ProfileStore}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class SesameVirtuosoProfileStore implements ProfileStore {

    private static Logger logger = Logger.getLogger(SesameVirtuosoProfileStore.class);

    private Repository repository;

    protected RDFizer rdfizer;

    private ProfileStoreConfiguration profileStoreConfiguration;

    private URI defaultGraph;

    public SesameVirtuosoProfileStore(
            ProfileStoreConfiguration profileStoreConfiguration,
            RDFizer rdfizer
    ) {
        this.profileStoreConfiguration = profileStoreConfiguration;
        this.repository = getRepository(this.profileStoreConfiguration);
        try {
            repository.initialize();
            logger.info("Virtuoso connection initialized.");
        } catch (RepositoryException e) {
            final String errMsg = "Error while initializing connection to Virtuoso";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
        this.rdfizer = rdfizer;
    }

    public SesameVirtuosoProfileStore(
            ProfileStoreConfiguration profileStoreConfiguration,
            RDFizer rdfizer,
            URI defaultGraph
    ) {
        this.profileStoreConfiguration = profileStoreConfiguration;
        this.repository = getRepository(this.profileStoreConfiguration);
        try {
            repository.initialize();
            logger.info("Virtuoso connection initialized.");
        } catch (RepositoryException e) {
            final String errMsg = "Error while initializing connection to Virtuoso";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
        this.rdfizer = rdfizer;
        this.defaultGraph = defaultGraph;
    }

    private Repository getRepository(ProfileStoreConfiguration profileStoreConfiguration) {
        return new VirtuosoRepository(
                "jdbc:virtuoso://" + profileStoreConfiguration.getHost() + ":" + profileStoreConfiguration.getPort(),
                profileStoreConfiguration.getUser(),
                profileStoreConfiguration.getPassword()
        );
    }

    /**
     * {@inheritDoc}
     */
    public UserProfile getUserProfile(URI id) throws ProfileStoreException {
        logger.info("Trying to retrieve Profile: '" + id + "'");                
        RepositoryConnection repositoryConnection = getConnection();
        RepositoryResult repositoryResult;
        try {
            repositoryResult = repositoryConnection.getStatements(
                    null,
                    null,
                    null,
                    false,
                    new URIImpl(id.toString())
            );
        } catch (RepositoryException e) {
            final String errMsg = "Error while retrieving profile: '" + id + "' from Virtuoso";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
        UserProfile result;
        logger.debug("getting Profile with id: " + id.toString());
        try {
            result = (UserProfile) rdfizer.getObject(
                    repositoryResult.asList(),
                    getIdentifierURI(UserProfile.class, id),
                    UserProfile.class
            );
        } catch (RDFizerException e) {
            final String errMsg = "Error while deserializing from RDF profile: '" + id + "'";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        } catch (RepositoryException e) {
            final String errMsg = "Error while accessing to RDF profile during deserialization: '" + id + "'";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
        releaseConnection(repositoryConnection);
        return result;
    }

    private static org.openrdf.model.URI getIdentifierURI(Class clazz, URI id) {
        String[] idString = id.toString().split("/");
        String lastPart = idString[idString.length - 1];
        RDFClassType rdfClassType = (RDFClassType) clazz.getAnnotation(RDFClassType.class);
        logger.debug("building uriImpl " + rdfClassType.type() + '/' + lastPart);
        return new URIImpl(rdfClassType.type() + '/' + lastPart);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteGraph(URI id) throws ProfileStoreException {
        RepositoryConnection repositoryConnection = getConnection();
        RepositoryResult repositoryResult;
        try {
            repositoryResult = repositoryConnection.getStatements(
                    null,
                    null,
                    null,
                    false,
                    new URIImpl(id.toString())
            );
        } catch (RepositoryException e) {
            final String errMsg = "Error while retrieving profile: '" + id + "' from Virtuoso";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }

        List<Statement> profileStatements;
        try {
           profileStatements = repositoryResult.asList();
        } catch (RepositoryException e) {
            final String errMsg = "Error while retrieving profile: '" + id + "' from Virtuoso for deletion";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
        for (Statement statement : profileStatements) {
            try {
                repositoryConnection.remove(statement, new URIImpl(id.toString()));
            } catch (RepositoryException e) {
                final String errMsg = "Error while removing RDF statements of profile: '" + id + "'";
                logger.error(errMsg, e);
                throw new ProfileStoreException(errMsg, e);
            }
        }

        try {
            repositoryConnection.commit();
        } catch (RepositoryException e) {
            final String errMsg = "Error while committing profile: '" + id + "' to on Virtuoso";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
        releaseConnection(repositoryConnection);
        logger.info("Profile: '" + id + "' successfully deleted");                
    }

    public ProjectProfile getProjectProfile(URI id) throws ProfileStoreException {
        logger.info("Trying to retrieve ProjectProfile: '" + id + "'");
        RepositoryConnection repositoryConnection = getConnection();
        RepositoryResult repositoryResult;
        try {
            repositoryResult = repositoryConnection.getStatements(
                    null,
                    null,
                    null,
                    false,
                    new URIImpl(id.toString())
            );
        } catch (RepositoryException e) {
            final String errMsg = "Error while retrieving ProjectProfile: '" + id + "' from Virtuoso";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
        ProjectProfile result;
        try {
            result = (ProjectProfile) rdfizer.getObject(
                    repositoryResult.asList(),
                    getIdentifierURI(ProjectProfile.class, id),
                    ProjectProfile.class
            );
        } catch (RDFizerException e) {
            final String errMsg = "Error while deserializing from RDF profile: '" + id + "'";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        } catch (RepositoryException e) {
            final String errMsg = "Error while accessing to RDF profile during deserialization: '" + id + "'";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
        releaseConnection(repositoryConnection);
        return result;
    }

    /**
     * @param id
     * @return
     * @throws ProfileStoreException
     *
     */
    public SearchProfile getSearchProfile(URI id) throws ProfileStoreException {
        logger.info("Trying to retrieve SearchProfile: '" + id + "'");
        RepositoryConnection repositoryConnection = getConnection();
        RepositoryResult repositoryResult;
        try {
            repositoryResult = repositoryConnection.getStatements(
                    null,
                    null,
                    null,
                    false,
                    new URIImpl(id.toString())
            );
        } catch (RepositoryException e) {
            final String errMsg = "Error while retrieving SearchProfile: '" + id + "' from Virtuoso";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
        SearchProfile result;
        try {
            result = (SearchProfile) rdfizer.getObject(
                    repositoryResult.asList(),
                    getIdentifierURI(SearchProfile.class, id),
                    SearchProfile.class
            );
        } catch (RDFizerException e) {
            final String errMsg = "Error while deserializing from RDF profile: '" + id + "'";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        } catch (RepositoryException e) {
            final String errMsg = "Error while accessing to RDF profile during deserialization: '" + id + "'";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
        releaseConnection(repositoryConnection);
        return result;

    }

    /**
     * {@inheritDoc}
     */
    public void close() throws ProfileStoreException {
        try {
            repository.shutDown();
        } catch (RepositoryException e) {
            final String errMsg = "Error while shutting down the connection with Virtuoso";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
    }

    public ProfileStoreConfiguration getConfiguration() {
        if(profileStoreConfiguration == null)
            throw new IllegalStateException("uhm it seems the ProfileStore has not been configured yet");
        return profileStoreConfiguration;
    }

    public void storeProfile(Profile profile, URI uri) throws ProfileStoreException {
        RepositoryConnection repositoryConnection = getConnection();
        List<Statement> statements;
        try {
            statements = rdfizer.getRDFStatements(profile);
        } catch (RDFizerException e) {
            final String errMsg = "Error while serializing in RDF object: '" + profile + "'";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }

        for (Statement statement : statements) {
            try {
                repositoryConnection.add(statement, new URIImpl(uri.toString()));
            } catch (RepositoryException e) {
                final String errMsg = "Error while adding RDF statement: '"
                        + statement + "' for object: '" + profile + "'";
                logger.debug(errMsg, e);
                continue;
            }
        }

        try {
            repositoryConnection.commit();
        } catch (RepositoryException e) {
            final String errMsg = "Error while committing object: '" + profile + "' on Virtuoso";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
        logger.info("Object: '" + profile + "' successfully stored on graph: '" + uri + "'");
        releaseConnection(repositoryConnection);
    }

    public void deleteProfileFromGraphIndex(URI indexGraphName, URI profileGraphName)
            throws ProfileStoreException {
        logger.debug("deleting triple from graph index: " + indexGraphName.toString() + " with uri: " +
                profileGraphName.toString());

        // build the uri to store in the graph index
        URIImpl uriToDelete = new URIImpl(profileGraphName.toString());
        //get only the statements with the uri as subject
        RepositoryConnection repositoryConnection = getConnection();
        RepositoryResult repositoryResult;

        //5th parameter is the context, the graph where it was saved
        try {
            repositoryResult = repositoryConnection.getStatements(
                    uriToDelete,
                    null,
                    null,
                    false,
                    new URIImpl(indexGraphName.toString())
            );
        } catch (RepositoryException e) {
            final String errMsg = "Error while retrieving profile: '" + profileGraphName + "' from Virtuoso";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }

        List<Statement> profileGraphStatements;
        try {
           profileGraphStatements = repositoryResult.asList();
        } catch (RepositoryException e) {
            final String errMsg = "Error while retrieving profile: '" + profileGraphName + "' from Virtuoso for deletion";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
        for (Statement statement : profileGraphStatements) {
            try {
                logger.debug("about to delete the statement: " + statement.toString() +
                        "from graph: " + indexGraphName.toString());
                repositoryConnection.remove(statement, new URIImpl(indexGraphName.toString()));
            } catch (RepositoryException e) {
                final String errMsg = "Error while removing RDF statements of profile: '" + profileGraphName + "'";
                logger.error(errMsg, e);
                throw new ProfileStoreException(errMsg, e);
            }
        }
        try {
            repositoryConnection.commit();
        } catch (RepositoryException e) {
            final String errMsg = "Error while committing profile: '" + profileGraphName + "' to on Virtuoso";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
        releaseConnection(repositoryConnection);
        logger.info("Profile: '" + profileGraphName + "' successfully deleted from graph " + indexGraphName.toString());

    }

    public void storeProfileToGraphIndex(URI indexGraphName, URI profileGraphName) throws ProfileStoreException, URISyntaxException {
        logger.debug("storing graph to graph index: " + indexGraphName.toString() + " with uri: " +
                indexGraphName.toString() + profileGraphName.toString());
        // build the uri to store in the graph index
        URIImpl uriToStore = new URIImpl(profileGraphName.toString());
        RepositoryConnection repositoryConnection = getConnection();
        Statement triple = new StatementImpl(uriToStore, RDF.TYPE, RDFS.CLASS);
        try {
            repositoryConnection.add(triple, new URIImpl(indexGraphName.toString()));
        } catch (RepositoryException e) {
            final String errMsg = "Error while adding RDF statement: '"
                    + triple + "' for object: '" + uriToStore + "'";
            logger.debug(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
        try {
            repositoryConnection.commit();
        } catch (RepositoryException e) {
            final String errMsg = "Error while committing object: '" + uriToStore + "' on Virtuoso";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
        logger.info("Object: '" + uriToStore + "' successfully stored on graph: '" + indexGraphName.toString() + "'");
        releaseConnection(repositoryConnection);
    }

    public List<Resource> getAllTriplesSubjectsFromGraphIndex(URI uri) throws ProfileStoreException {
        logger.debug("getting all resources from graph: " + uri.toString());
        List<Resource> resources = new ArrayList<Resource>();
        RepositoryConnection repositoryConnection = getConnection();
        RepositoryResult repositoryResult;
        try {
            repositoryResult = repositoryConnection.getStatements(
                    null,
                    null,
                    null,
                    false,
                    new URIImpl(uri.toString())
            );
        } catch (RepositoryException e) {
            final String errMsg = "Error while retrieving graph: '" + uri + "' from Virtuoso";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }

        try {
            List<Statement> resultStatements = repositoryResult.asList();
            for (Statement statement : resultStatements) {
                resources.add(statement.getSubject());
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        finally {
            releaseConnection(repositoryConnection);
        }
        return resources;
    }

    protected RepositoryConnection getConnection() throws ProfileStoreException {
        try {
            return repository.getConnection();
        } catch (RepositoryException e) {
            final String errMsg = "Error while getting session on Virtuoso";
            logger.error(errMsg, e);
            throw new ProfileStoreException("Error while getting session on Virtuoso", e);
        }
    }

    protected void releaseConnection(RepositoryConnection repositoryConnection)
            throws ProfileStoreException {
        try {
            repositoryConnection.close();
        } catch (RepositoryException e) {
            final String errMsg = "Error while closing session on Virtuoso";
            logger.error(errMsg);
            throw new ProfileStoreException(errMsg, e);
        }
    }

}
