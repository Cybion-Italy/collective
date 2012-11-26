package com.collective.profilingline.skos;

import com.collective.profiler.line.ProfilingLineItem;
import com.collective.profiler.line.ProfilingLineItemException;
import com.collective.profiler.line.model.Interest;
import com.collective.profilingline.knowledge.KnowledgeExtractionProfilingLineItem;
import com.collective.profilingline.skos.repository.MyBatisSkosRepository;
import com.collective.profilingline.skos.repository.SkosRepository;
import com.collective.profilingline.skos.repository.SkosRepositoryException;
import org.apache.log4j.Logger;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This {@link com.collective.profiler.line.ProfilingLineItem} is responsible
 * to enrich the <i>Linked Data Cloud</i> unique identifiers coming from
 * the {@link com.collective.profilingline.knowledge.KnowledgeExtractionProfilingLineItem}
 * with its <a href="http://www.springerlink.com/index/V113U52627248870.pdf">
 * Most representative SKOS subject</i>.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SkosLinkerProfilingLineItem extends ProfilingLineItem {

    private static Logger logger = Logger.getLogger(SkosLinkerProfilingLineItem.class);

    public static final String SKOS_URIS_KEY = "user-interests-with-skos";

    private SkosRepository skosRepository;

    public SkosLinkerProfilingLineItem(String name, String description) {
        super(name, description);
        skosRepository = new MyBatisSkosRepository();
    }

    public void execute(Object o) throws ProfilingLineItemException {
        logger.info("Skos Linker item started");
        Map<String, Object> rawProfile = (Map<String, Object>) o;
        List<URI> uris = (List<URI>) rawProfile.get(KnowledgeExtractionProfilingLineItem.DBPEDIA_URL_KEY);
        List<Interest> interests = new ArrayList<Interest>();
        for(URI uri : uris) {
            Interest interest = new Interest(uri);
            SkosSubject skosSubject;
            try {
                skosSubject = skosRepository.lookup(uri);
                if(skosSubject != null) {
                    interest.addSubject(skosSubject.getIdentifier());
                    logger.info("SKOS: '" + skosSubject  + "'");
                }
            } catch (SkosRepositoryException e) {
                final String errMsg = "Error while looking up SKOS subject for '"  + uri + "'";
                logger.error(errMsg, e);
                throw new ProfilingLineItemException(errMsg, e);
            }
            interests.add(interest);
        }
        rawProfile.put(SKOS_URIS_KEY, interests);
        next.execute(rawProfile);
    }
}
