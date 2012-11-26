package com.collective.analyzer.runner;

import com.asemantics.notube.rdf.storage.virtuoso.VirtuosoTripleStorage;
import com.asemantics.notube.rdf.storage.virtuoso.VirtuosoTripleStorageConfig;
import com.asemantics.rdf.TripleSet;
import com.asemantics.rdf.jena.JenaConversionUtil;
import com.asemantics.rdf.storage.StorageException;
import com.hp.hpl.jena.ontology.OntModel;
import it.cybion.analyzer.AlchemyConceptsExtractor;
import it.cybion.analyzer.AlchemyNamedEntitiesExtractor;
import it.cybion.analyzer.AnalyzationException;
import com.collective.document.analyzer.domain.UrlMapped;
import com.collective.document.analyzer.jena.JenaOntologyModelBuilder;
import it.cybion.document.builder.SimpleTextualRepresentationBuilder;
import it.cybion.document.builder.TextualRepresentationBuilder;
import it.cybion.proconsult.domain.Url;
import org.apache.log4j.Logger;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

public class DocumentAnalyzerJobNeverLaunch {
	
    private static Logger logger = Logger.getLogger(DocumentAnalyzerJobNeverLaunch.class);


	public static void main(String[] args) {
		logger.info("Starting Document Analyzer Job");

//		DocumentManager documentManager = new DocumentManagerImpl();

		/* open triplestore to save in a specific graph */
		String urlGraphName = "http://www.cybion.it/proconsult/url";
//		String urlGraphName = "http://www.cybion.it/proconsult/url#tests";

		VirtuosoTripleStorage storage = new VirtuosoTripleStorage();
//		String virtuosoHost = "localhost";
		// when running from remote machines, refer to virtuoso instance on
		// cibionte
		 String virtuosoHost = "cibionte.dyndns.org";
		VirtuosoTripleStorageConfig config = new VirtuosoTripleStorageConfig(
				"jdbc:virtuoso://" + virtuosoHost + ":1111", urlGraphName,
				"dba", "cybiondba");
		/*
		 * TODO: refactor in an external factory, that takes parameters from
		 * properties file?
		 */
		storage.openStorage(config);

		/*
		 * get all non-analyzed documents from db table (url in this case)
		 * should take just the last N documents for performance issues of alchemy
		 */

		int documentsAmount = 100;

        List<Url> documentsToAnalyze = null;
//		List<Url> documentsToAnalyze = documentManager
//				.getLastNDocuments(documentsAmount);

		logger.info("got " + documentsToAnalyze.size() + " urls to analyse");

		// call uima on each document and get annotations results
		AlchemyConceptsExtractor alchemyConceptsExtractor = null;
		AlchemyNamedEntitiesExtractor alchemyNamedEntitiesExtractor = null;
		try {
			alchemyConceptsExtractor = new AlchemyConceptsExtractor();
			alchemyNamedEntitiesExtractor = new AlchemyNamedEntitiesExtractor(); 

		} catch (AnalyzationException e1) {
			// TODO Auto-generated catch block
			logger.error("could not load annotators");
			System.exit(-1);
		}

		TextualRepresentationBuilder textBuilder = new SimpleTextualRepresentationBuilder();

		for (Url document : documentsToAnalyze) {
			logger.info("analysing document: " + document.getId() + " " + document.getTitolo());

			//analyse whole text
			String toAnalyze = textBuilder.concatAllText(document);
			logger.debug("analysing text: " + toAnalyze);

			/*
			 * TODO: we should try to detect language before calling alchemy api, so
			 * that we can do less calls to the api if it is not in english
			 */
			List<URI> annotations = new LinkedList<URI>();
			
			List<URI> namedEntitiesAnnotations = new LinkedList<URI>();

			try {
				annotations = alchemyConceptsExtractor.analyzeText(toAnalyze);
				namedEntitiesAnnotations = alchemyNamedEntitiesExtractor.analyzeText(toAnalyze);
				
				logger.debug("found annotations: ");
				for (URI uri : annotations) { 
					logger.debug(uri.toString()); 
					}
				
				logger.debug("found named entities: "); 
				for (URI uri : namedEntitiesAnnotations) { 
					logger.debug(uri.toString()); 
				}
				
				/* now build a jenabeans annotated bean - UrlMapped - to an
				 * object that serializes it on the triple store
				 */
				UrlMapped urlMapped = new UrlMapped(document, annotations);
				
				//add the named entities to the bean
				urlMapped.getTopics().addAll(namedEntitiesAnnotations);

				OntModel urlJenaModel = JenaOntologyModelBuilder
						.buildOntologyModel(urlMapped);

				/* convert jena model to TripleSet */
				TripleSet tripleSet = JenaConversionUtil
						.convertJenaModelToTripleSet(urlJenaModel);
				
				/* store the triples in virtuoso */
				
				try {
					storage.store(tripleSet);
					/* update the object in the relational database */
					document.setAnalyzed(true);
//					documentManager.update(document);
				} catch (StorageException e) {
					logger.error("failed to store triple on virtuoso: " + tripleSet.toString());
					logger.error("as storing url id: " + document.getId());
					e.printStackTrace();
					System.exit(-1);
				}
			}
			
			catch (AnalyzationException e) {
				logger.warn("analysis error on url with id = " + document.getId());
			}
			
		}

		/* end, close virtuoso */
		storage.closeStorage();
		storage = null;
		logger.info("Ending Document Analyzer Job");
	}
}
