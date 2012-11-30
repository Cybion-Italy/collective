package com.collective.document.analyzer.storage;

import com.collective.analyzer.storage.DocumentStorageException;
import com.collective.document.analyzer.configuration.InMemoryRepositoriesProviderTestCase;
import com.collective.document.analyzer.utils.DomainFixtures;
import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import org.apache.log4j.Logger;
import org.nnsoft.be3.typehandler.TypeHandlerRegistryException;
import org.openrdf.model.Statement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class SesameVirtuosoDocumentStorageTestCase extends InMemoryRepositoriesProviderTestCase {

    private Logger logger = Logger.getLogger(SesameVirtuosoDocumentStorageTestCase.class);

    @BeforeClass
    public void setUp() throws TypeHandlerRegistryException, URISyntaxException {
        logger.info("start of setUp method");
    }

    @AfterClass
    public void tearDown() {
        logger.info("end of test tearDown method");
    }

    @Test
    public void shouldSaveAndDeleteWebResourceToDocumentStorage()
            throws DocumentStorageException, MalformedURLException {

        WebResourceEnhanced webResourceEnhanced = DomainFixtures.getWebResourceEnhanced();

        try {
            documentStore.storeWebResource(webResourceEnhanced);
        }  finally {
            documentStore.deleteWebResource(webResourceEnhanced);
        }

        List<Statement> statementList =
                documentStore.getAllGraphStatements(
                        documentStore.defaultGraph.toString());

        Assert.assertEquals(statementList.size(), 0);
    }
}
