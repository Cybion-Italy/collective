package com.collective.analyzer.runner;

import com.collective.concepts.core.Concept;
import com.collective.concepts.core.UserDefinedConceptStore;
import com.collective.concepts.core.UserDefinedConceptStoreException;
import com.collective.concepts.core.lookup.UserDefinedConceptLookupEngine;
import com.collective.concepts.core.lookup.UserDefinedConceptLookupEngineException;
import com.collective.concepts.core.lookup.lucene.LuceneLookupEngine;
import com.collective.concepts.core.lookup.result.ResultListener;
import com.collective.concepts.core.lookup.result.ResultListnerException;
import com.collective.analyzer.enrichers.EnrichmentService;
import com.collective.analyzer.enrichers.EnrichmentServiceException;
import com.collective.analyzer.enrichers.dbpedia.DBPediaAPI;
import com.collective.analyzer.storage.DocumentStorage;
import com.collective.analyzer.storage.DocumentStorageException;
import com.collective.analyzer.storage.WebResourceRepository;
import com.collective.analyzer.storage.WebResourceRepositoryException;
import com.collective.model.persistence.WebResource;
import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import org.apache.log4j.Logger;
import tv.notube.commons.alchemyapi.AlchemyAPI;
import tv.notube.commons.alchemyapi.AlchemyAPIException;
import tv.notube.commons.alchemyapi.AlchemyAPIResponse;
import tv.notube.commons.alchemyapi.Identified;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class DocumentAnalyzer {

    private static final Logger logger = Logger.getLogger(DocumentAnalyzer.class);

    private static final String ALCHEMY_API_KEY = "8b40dcbf923714613e8ef1b0579d8453086d5622";

    private DocumentStorage documentStore;

    private Long documentsToAnalyze;

    private AlchemyAPI alchemyApi;

    private EnrichmentService dbpediaApi;

    private UserDefinedConceptStore userDefinedConceptStore;

    private UserDefinedConceptLookupEngine engine;

    private List<WebResource> analyzedWebResources = new ArrayList<WebResource>();

    private WebResourceRepository webResourceRepository;

    public DocumentAnalyzer(DocumentStorage documentStore,
                            Long documentsToAnalyze,
                            WebResourceRepository webResourceRepository,
                            UserDefinedConceptStore userDefinedConceptStore
    ) {
        this.documentStore = documentStore;
        this.documentsToAnalyze = documentsToAnalyze;
        this.webResourceRepository = webResourceRepository;
        //TODO high: move out the analysis service used
        alchemyApi = new AlchemyAPI(ALCHEMY_API_KEY);
        dbpediaApi = new DBPediaAPI();
        this.userDefinedConceptStore = userDefinedConceptStore;
        engine = new LuceneLookupEngine(userDefinedConceptStore, 50);
    }

    public void run() throws DocumentAnalyzerException {
        WebResource notAnalyzedWebResource = new WebResource();
        notAnalyzedWebResource.setAnalyzed(false);
        List<WebResource> toAnalyzeWebResources = null;
        try {
            toAnalyzeWebResources = this.webResourceRepository
                    .selectWebResourcesToAnalyse(documentsToAnalyze.intValue()
                    );
        } catch (WebResourceRepositoryException e) {
            final String eMsg = "error while getting " +
                    "web resources to analyse from persistence";
            logger.error(eMsg + e.getMessage());
            throw new DocumentAnalyzerException(eMsg, e);
        }
        this.analyzedWebResources.addAll(toAnalyzeWebResources);

        logger.info("analysing resources for custom concepts");
        /* extract custom concepts from resources */
        extractCustomConcepts(toAnalyzeWebResources);

        logger.info("analysing resources for common DBpedia concepts");
        /* then, use external APIs to extract dbpedia concepts from resources */
        //TODO (high): change logic to set analysed=true in resources
        /* right now, this method updates boolean in resources */
        extractDBpediaConcepts(toAnalyzeWebResources);
    }

    private void extractDBpediaConcepts(List<WebResource> toAnalyzeWebResources)
            throws DocumentAnalyzerException
    {

        for (WebResource webResource : toAnalyzeWebResources) {

            WebResourceEnhanced enhancedWebResource =
                    analyzeWebResource(webResource);

            /* only if topics are > 0 the web resource should be saved,
             * not to overload triple store */
            if (enhancedWebResource.getTopics().size() > 0) {
                try {
                    documentStore.storeWebResource(enhancedWebResource);
                } catch (DocumentStorageException e) {
                    final String errMsg = "Error while storing: '"
                            + enhancedWebResource + "'";
                    logger.error(errMsg, e);
                    throw new DocumentAnalyzerException(errMsg, e);
                }
            }

            /* TODO high: issue - resource is set as analysed even if
             * it was NOT analysed by the UserDefinedConceptLookup engine */
            webResource.setAnalyzed(true);
            webResourceRepository.update(webResource);
            logger.info("analysed webResource " + webResource.getId()
                                + " for common DBpedia concepts");
        }
    }

    private void extractCustomConcepts(
            List<WebResource> toAnalyzeWebResources
    ) throws DocumentAnalyzerException {
        List<Long> userIds;
        try {
            userIds = userDefinedConceptStore.getUsers();
        } catch (UserDefinedConceptStoreException e) {
            final String errMsg = "Error while getting user IDs";
            logger.error(errMsg, e);
            throw new DocumentAnalyzerException(errMsg, e);
        }

        logger.info("got userIds for custom concepts analysis");

        for (WebResource webResource : toAnalyzeWebResources) {
            for (Long userId : userIds) {
                ResultListener resultListener =
                        new DocumentAnalyzerMultiThreadResultListener();
                try {
                    logger.debug("analysing webResource id: " + webResource.getId());
                    engine.lookup(webResource.getContenutoTesto(),
                            userId, resultListener);
                } catch (UserDefinedConceptLookupEngineException e) {
                    final String errMsg =
                            "Error while looking up concepts of user '"
                                    + userId + "'";
                    logger.error(errMsg, e);
                    throw new DocumentAnalyzerException(errMsg, e);
                }
                List<URI> concepts;
                try {
                    concepts = toURI(resultListener.getConcepts());
                } catch (ResultListnerException e) {
                    final String errMsg = "Error while getting all the " +
                            "concepts from result listener";
                    logger.error(errMsg, e);
                    throw new DocumentAnalyzerException(errMsg, e);
                }

                //store to user graph ONLY IF concepts > 0
                if (concepts.size() > 0) {
                    WebResourceEnhanced webResourceEnhanced =
                            new WebResourceEnhanced(
                                    webResource,
                                    concepts
                            );
                    try {
                        documentStore.storeUserWebResource(webResourceEnhanced,
                                userId);
                    } catch (DocumentStorageException e) {
                        final String errMsg =
                                "Error while storing user annotations";
                        logger.error(errMsg, e);
                        throw new DocumentAnalyzerException(errMsg, e);
                    }
                    logger.info("saved webResource custom concepts for userId "
                                + userId);
                }
            }
        }
    }

    private List<URI> toURI(Set<Concept> concepts) {
        List<URI> result = new ArrayList<URI>();
        for(Concept concept : concepts) {
            try {
                result.add(concept.getURL().toURI());
            } catch (URISyntaxException e) {
                // just skip
                continue;
            }
        }
        return result;
    }

    private WebResourceEnhanced analyzeWebResource(WebResource webResource)
            throws DocumentAnalyzerException
    {
        logger.debug("analyzing webResource with id: '"
                                                + webResource.getId() + "'");
        String textToAnalyze = "";
        textToAnalyze = textToAnalyze.concat(webResource.getTitolo());
        textToAnalyze = textToAnalyze.concat(" " + webResource.getDescrizione());
        textToAnalyze = textToAnalyze.concat(" " + webResource.getContenutoTesto());

        //contains ALL the concepts found in the document, named entities and ranked concepts
        List<URI> concepts = new ArrayList<URI>();

        /* get named entities from dbpedia spotlight */
        try {
            concepts.addAll(dbpediaApi.getConceptsURIs(textToAnalyze));
        } catch (EnrichmentServiceException e) {
            final String errMsg = "Error while getting Named entities from dbpedia spotlight";
            logger.error(errMsg, e);
            throw new DocumentAnalyzerException(errMsg, e);
        }

        /* uses alchemy only if dbpedia failed */
        if (concepts.size() == 0)
        {
            /* alchemy is turned OFF */
//            concepts.addAll(analyseWithAlchemy(textToAnalyze));
        }
        return new WebResourceEnhanced(webResource, concepts);
    }

    private List<URI> analyseWithAlchemy(String textToAnalyze)
            throws DocumentAnalyzerException {
        List<URI> concepts = new ArrayList<URI>();
        AlchemyAPIResponse alchemyApiResponse;
        /* get named entities from alchemy */
        try {
            alchemyApiResponse = alchemyApi.getNamedEntities(textToAnalyze);
        } catch (AlchemyAPIException e) {
            final String errMsg = "Error while getting Named entities from Alchemy";
            logger.error(errMsg, e);
            throw new DocumentAnalyzerException(errMsg, e);
        }

        for (Identified identifiedEntity : alchemyApiResponse.getIdentified()) {
            concepts.add(identifiedEntity.getIdentifier());
        }

        /* get concepts */
        try {
            alchemyApiResponse = alchemyApi.getRankedConcept(textToAnalyze);
        } catch (AlchemyAPIException e) {
            final String errMsg = "Error while getting Concepts from Alchemy";
            logger.error(errMsg, e);
            throw new DocumentAnalyzerException(errMsg, e);
        }
        for (Identified identifiedRankedConcept : alchemyApiResponse.getIdentified()) {
            concepts.add(identifiedRankedConcept.getIdentifier());
        }
        return concepts;
    }

    public List<WebResource> getAnalyzedWebResources() {
        return this.analyzedWebResources;
    }

    public void setAlchemyApi(AlchemyAPI alchemyApi) {
        this.alchemyApi = alchemyApi;
    }
}
