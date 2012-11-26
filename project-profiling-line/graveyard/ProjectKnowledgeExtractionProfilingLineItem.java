package com.collective.projectprofilingline.knowledge;

import com.collective.model.Project;
import it.cybion.analyzer.AlchemyNamedEntitiesExtractor;
import it.cybion.analyzer.AnalyzationException;
import org.apache.log4j.Logger;

import com.collective.profiler.line.ProfilingLineItem;
import com.collective.profiler.line.ProfilingLineItemException;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This {@link com.collective.profiler.line.ProfilingLineItem} is responsible
 * of the extraction of the implicit knowledge in terms of <i>Linked Data Cloud</i>
 * unique identifiers.
 * 
 * @author Matteo Moci ( moci@cybion.it )
 */
public class ProjectKnowledgeExtractionProfilingLineItem extends ProfilingLineItem {

    public static final String PROJECT_DBPEDIA_URL_KEY = "project-dbpedia-uris";

    public static final String PROJECT_KEY = "project";

	private static Logger logger = Logger.getLogger(ProjectKnowledgeExtractionProfilingLineItem.class);

    private AlchemyNamedEntitiesExtractor alchemyNamedEntitiesExtractor;

    public ProjectKnowledgeExtractionProfilingLineItem(String name, String description) {
        super(name, description);
        try {
            //TODO: refactor and get as singleton
            this.alchemyNamedEntitiesExtractor = new AlchemyNamedEntitiesExtractor();
        } catch (AnalyzationException e) {
            throw new RuntimeException("Error while instantiating the AlchemyExtractor");
        }
    }

    /*
      * this ProfilingLine simply reads projects manifesto and sends it to
      * the next ProjectKnowledgeExtractionProfilingLineItem
      * */
    /* TODO: should be made "smarter"
    so that every class that it takes in input, always enriches its textual description */
 	@Override
	public void execute(Object input) throws ProfilingLineItemException {
        Project castedProject = (Project) input;
        String manifesto = castedProject.getManifesto();

        logger.debug("read manifesto: " + manifesto);
        List<URI> concepts;
        try {
            concepts = this.alchemyNamedEntitiesExtractor.analyzeText(manifesto);
        } catch (AnalyzationException e) {
            final String errMsg = "Error while calling Alchemy Entity Extractor on project manifesto: '" + manifesto + "'";
            logger.error(errMsg, e);
            throw new ProfilingLineItemException(errMsg, e);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();

        resultMap.put(PROJECT_KEY, castedProject);
        resultMap.put(PROJECT_DBPEDIA_URL_KEY, concepts);

        logger.debug("list of concepts found in manifesto: '" + concepts.toString() + "'");

        super.next.execute(resultMap);
	}

}
