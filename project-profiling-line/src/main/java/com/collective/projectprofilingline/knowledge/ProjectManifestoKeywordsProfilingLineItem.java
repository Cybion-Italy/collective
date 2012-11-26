package com.collective.projectprofilingline.knowledge;

import com.collective.model.Project;
import com.collective.model.User;
import com.collective.profiler.line.ProfilingLineItem;
import com.collective.profiler.line.ProfilingLineItemException;
import it.cybion.dbpedia.textsearch.DbpediaConceptByWikipediaTextSearch;
import it.cybion.dbpedia.textsearch.DbpediaConceptSearch;
import it.cybion.dbpedia.textsearch.DbpediaTextSearch;
import it.cybion.dbpedia.textsearch.exceptions.DbpediaConceptSearchInstantiationException;
import it.cybion.dbpedia.textsearch.exceptions.DbpediaTextSearchInstantiationException;
import it.cybion.dbpedia.textsearch.rest.response.ArrayOfResult;
import it.cybion.dbpedia.textsearch.rest.response.Result;
import org.apache.log4j.Logger;

import java.net.URI;
import java.util.*;

/**
 * This {@link com.collective.profiler.line.ProfilingLineItem} is responsible
 * of the extraction of the projects keywords manifesto in terms of <i>Linked Data Cloud</i>
 * unique identifiers.
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class ProjectManifestoKeywordsProfilingLineItem extends ProfilingLineItem {

    //TODO: is static field right?
    //old one
    private static DbpediaTextSearch dbpediaTextSearch;
    //new one
    private static DbpediaConceptSearch dbpediaConceptSearch;

    private static Logger logger = Logger.getLogger(ProjectManifestoKeywordsProfilingLineItem.class);

    public static String PROJECT_KEY = "project";

    public static String MANIFESTO_CONCEPTS_DBPEDIA_KEY = "manifesto-dbpedia-uris";

    public ProjectManifestoKeywordsProfilingLineItem(String name, String description) {
        super(name, description);

        //instantiate anyway the old one
        try {
            this.dbpediaTextSearch = DbpediaTextSearch.getInstance();
        } catch (DbpediaTextSearchInstantiationException e) {
            //TODO: manage exception
            logger.error("failed to initialize dbpedia textsearch");
            throw new RuntimeException("Error while instantiating the dbpedia textsearch", e);
        }

        try {
            this.dbpediaConceptSearch = DbpediaConceptByWikipediaTextSearch.getInstance();
        } catch (DbpediaConceptSearchInstantiationException e) {
            logger.error("failed to initialize dbpedia textsearch");
            throw new RuntimeException("Error while instantiating the dbpedia concept search", e);
        }


    }

    public void execute(Object o) throws ProfilingLineItemException {
        Project incomingProject = (Project) o;

        final String onCommas = ",";

        List<URI> conceptsInManifesto = new LinkedList<URI>();
        if (incomingProject.getManifestoKeywords() != null) {
            /* analyse manifesto keywords */
            logger.debug("converting manifesto keywords");
            List manifestoKeywordsList = Arrays.asList(incomingProject.getManifestoKeywords().split(onCommas));
            LinkedList<String> manifestoKeywords = new LinkedList<String>(manifestoKeywordsList);
            conceptsInManifesto = convertKeywordsToURI(manifestoKeywords);
        } else {
            logger.warn("no manifesto keywords found for project " + incomingProject.getId());
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(PROJECT_KEY, incomingProject);
        resultMap.put(MANIFESTO_CONCEPTS_DBPEDIA_KEY, conceptsInManifesto);

        super.next.execute(resultMap);
    }

    private static List<URI> convertKeywordsToURI(List<String> keywordsList) {
        List<URI> conceptsList = new LinkedList<URI>();

        for (String skillKeyword : keywordsList) {
            /* strip leading and trailing whitespace */
            //TODO: check non utf-8 characters to clean request
            skillKeyword = skillKeyword.trim();
            logger.debug("found keyword: " + skillKeyword);

            //old method
            /*
            ArrayOfResult results = null;
            results = dbpediaTextSearch.sendRequest(skillKeyword, 1);
            if (results.getResults().size() > 0) {
                Result concept = results.getResults().get(0);
                logger.debug("adding uri to keywords results: " + concept.getUri());
                conceptsList.add(concept.getUri());
            }
            */

            //use the new dbpedia concept search
            List<URI> resultUris = new ArrayList<URI>();
            resultUris = dbpediaConceptSearch.sendRequest(skillKeyword);

            if (resultUris.size() > 0) {
                URI uri = resultUris.get(0);
                logger.debug("adding uri to keywords results: " + uri.toString());
                conceptsList.add(uri);
            }

        }
        return conceptsList;
    }
}

