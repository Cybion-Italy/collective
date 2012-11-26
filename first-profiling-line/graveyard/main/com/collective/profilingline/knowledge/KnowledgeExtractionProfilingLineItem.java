package com.collective.profilingline.knowledge;

import it.cybion.analyzer.AlchemyNamedEntitiesExtractor;
import it.cybion.analyzer.AnalyzationException;
import org.apache.log4j.Logger;

import com.collective.profiler.line.ProfilingLineItem;
import com.collective.profiler.line.ProfilingLineItemException;
import com.collective.model.User;

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
public class KnowledgeExtractionProfilingLineItem extends ProfilingLineItem {

    public static final String DBPEDIA_URL_KEY = "dbpedia-uris";

    public static final String USER_KEY = "user";

	private static Logger logger = Logger.getLogger(KnowledgeExtractionProfilingLineItem.class);

    private AlchemyNamedEntitiesExtractor alchemyNamedEntitiesExtractor;

    public KnowledgeExtractionProfilingLineItem(String name, String description) {
        super(name, description);
        try {
            this.alchemyNamedEntitiesExtractor = new AlchemyNamedEntitiesExtractor();
        } catch (AnalyzationException e) {
            throw new RuntimeException("Error while instantiating the AlchemyExtractor");
        }
    }

    /*
      * this ProfilingLine simply reads interests from the User and sends it to
      * the next KnowledgeExtractionProfilingLineItem
      * */
	@Override
	public void execute(Object input) throws ProfilingLineItemException {
		User castedUser = (User) input;
		String interests = castedUser.getInterests();
		logger.debug("read interests: " + interests);
        List<URI> concepts;
        try {
            concepts = this.alchemyNamedEntitiesExtractor.analyzeText(interests);
        } catch (AnalyzationException e) {
            final String errMsg = "Error while calling Alchemy Entity Extractor on: '" + interests + "'";
            logger.error(errMsg, e);
            throw new ProfilingLineItemException(errMsg, e);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(USER_KEY, castedUser);
        resultMap.put(DBPEDIA_URL_KEY, concepts);
        logger.debug("list of concepts: '" + concepts.toString() + "'");
        super.next.execute(resultMap);
	}

}
