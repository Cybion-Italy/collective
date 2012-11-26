package com.collective.profilingline.knowledge;

import com.collective.model.User;
import com.collective.profiler.line.ProfilingLineItem;
import com.collective.profiler.line.ProfilingLineItemException;
import it.cybion.dbpedia.textsearch.DbpediaConceptByWikipediaTextSearch;
import it.cybion.dbpedia.textsearch.DbpediaConceptSearch;
import it.cybion.dbpedia.textsearch.DbpediaTextSearch;
import it.cybion.dbpedia.textsearch.exceptions.DbpediaConceptSearchInstantiationException;
import it.cybion.dbpedia.textsearch.exceptions.DbpediaTextSearchInstantiationException;
import org.apache.log4j.Logger;

import java.net.URI;
import java.util.*;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class KeywordsProfilingLineItem extends ProfilingLineItem {

    private static DbpediaConceptSearch dbpediaConceptSearch;

    private static Logger logger = Logger.getLogger(KeywordsProfilingLineItem.class);

    public static String USER_KEY = "user";

    public static String SKILLS_DBPEDIA_KEY = "skills-dbpedia-uris";

    public static String INTERESTS_DBPEDIA_KEY = "interests-dbpedia-uris";

    public KeywordsProfilingLineItem(String name, String description) {
        super(name, description);
        try {
            dbpediaConceptSearch = DbpediaConceptByWikipediaTextSearch.getInstance();
        } catch (DbpediaConceptSearchInstantiationException e) {
            logger.error("failed to initialize dbpedia concept search");
            throw new RuntimeException("Error while instantiating the dbpedia concept search", e);
        }
    }

    public void execute(Object o) throws ProfilingLineItemException {
        User incomingUser = (User) o;
        final String onCommas = ",";

        List<URI> conceptsInSkills = new LinkedList<URI>();
        List<URI> conceptsInInterests = new LinkedList<URI>();
        if (incomingUser.getSkillsKeywords() != null) {
            /* analyse skills keywords */
            logger.debug("converting skills keywords");
            List skillsKeywordsList = Arrays.asList(incomingUser.getSkillsKeywords().split(onCommas));
            LinkedList<String> skillsKeywords = new LinkedList<String>(skillsKeywordsList);
            conceptsInSkills = convertKeywordsToURI(skillsKeywords);
        } else {
            logger.warn("no skills keywords found for user " + incomingUser.getId());
        }

        if (incomingUser.getInterestsKeywords() != null) {
            /* analyse interests keywords */
            logger.debug("converting interests keywords");
            List interestsKeywordsList = Arrays.asList(incomingUser.getInterestsKeywords().split(onCommas));
            LinkedList<String> interestsKeywords = new LinkedList<String>(interestsKeywordsList);
            conceptsInInterests = convertKeywordsToURI(interestsKeywords);
        } else {
            logger.warn("no interests keywords found for user " + incomingUser.getId());
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(USER_KEY, incomingUser);
        resultMap.put(SKILLS_DBPEDIA_KEY, conceptsInSkills);
        resultMap.put(INTERESTS_DBPEDIA_KEY, conceptsInInterests);

        super.next.execute(resultMap);
    }

    public static List<URI> convertKeywordsToURI(List<String> keywordsList) {
        List<URI> conceptsList = new LinkedList<URI>();
        for (String skillKeyword : keywordsList) {
            /* strip leading and trailing whitespace */
            skillKeyword = skillKeyword.trim();
            logger.debug("found keyword: " + skillKeyword);
            List<URI> resultUris;
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

