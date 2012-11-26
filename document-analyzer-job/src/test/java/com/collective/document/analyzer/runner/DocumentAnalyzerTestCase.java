package com.collective.document.analyzer.runner;

import com.collective.analyzer.runner.DocumentAnalyzer;
import com.collective.analyzer.runner.DocumentAnalyzerException;
import com.collective.analyzer.storage.DocumentStorageException;
import com.collective.document.analyzer.analysis.MockAlchemyAPI;
import com.collective.document.analyzer.configuration.InMemoryRepositoriesProviderTestCase;
import com.collective.model.persistence.WebResource;
import com.collective.rdfizer.annotations.RDFClassType;
import com.collective.rdfizer.typehandler.TypeHandlerRegistryException;
import org.apache.log4j.Logger;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class DocumentAnalyzerTestCase extends InMemoryRepositoriesProviderTestCase {

    private DocumentAnalyzer documentAnalyzer;

    private static final Logger logger =
            Logger.getLogger(DocumentAnalyzerTestCase.class);


    public DocumentAnalyzerTestCase() {
        super();
    }

    @BeforeClass
    public void setUp() throws TypeHandlerRegistryException {
        logger.info("analysing " + documentsToAnalyze + " webResources");

        documentAnalyzer = new DocumentAnalyzer(
                documentStore,
                documentsToAnalyze,
                resourcesRepository,
                userDefinedConceptStore
        );

        //set a mock instance to return fake things during analysis
        documentAnalyzer.setAlchemyApi(new MockAlchemyAPI("fake-api-key"));

        logger.debug("set up DocumentAnalyzer instance");
    }

    @AfterClass
    public void tearDown() {
        documentStore = null;
        resourcesRepository = null;
        documentAnalyzer = null;
    }

    //TODO refactor using mock dbpedia spotlight
    @Test (enabled = false)
    public void shouldAnalyseTestResources()
            throws DocumentAnalyzerException, DocumentStorageException
    {

        documentAnalyzer.run();

        int predCounter = 0;

        //inspect results contained in in-memory repositories

        List<Statement> statements =
                    this.documentStore.getAllGraphStatements(
                    InMemoryRepositoriesProviderTestCase.DEFAULT_GRAPH);

        URI dcSubject = null;
        try {
            dcSubject = new URI("http://purl.org/dc/terms/subject");
        } catch (URISyntaxException e) {
            //should never happen
            e.printStackTrace();
        }

        //count the dc:topic statements
        for (Statement stmt : statements) {
                if (stmt.getPredicate().toString()
                        .equals(dcSubject.toString())) {
                    predCounter++;
                }
        }

        // dcSubject is = 6 since there are 3 webResources, each with 2 concepts
        Assert.assertEquals(predCounter, 3);

        for(WebResource webResource : this.resourcesRepository.getWebResources()) {
            //assert that starting instance of webresource was marked as analysed
            Assert.assertTrue(webResource.isAnalyzed());
        }
    }

    private org.openrdf.model.URI getRDFType(Class beanClass) {
        RDFClassType rdfClassType =
                (RDFClassType) beanClass.getAnnotation(RDFClassType.class);
        if (rdfClassType == null) {
            throw new IllegalArgumentException(
                    String.format(
                            "The class '%s' must specify the '%s' annotation.",
                            beanClass,
                            RDFClassType.class
                    )
            );
        }
        final String classType = rdfClassType.type();
        if (classType.trim().length() == 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid class type '%s'", classType));
        }
        return new URIImpl(classType);
    }
}
