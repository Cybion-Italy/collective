package com.collective.concepts.core.lookup.lucene;

import com.collective.concepts.core.Concept;
import com.collective.concepts.core.UserDefinedConceptStore;
import com.collective.concepts.core.UserDefinedConceptStoreException;
import com.collective.concepts.core.lookup.AbstractLookupEngine;
import com.collective.concepts.core.lookup.UserDefinedConceptLookupEngineException;
import com.collective.concepts.core.lookup.lucene.runner.LookupReport;
import com.collective.concepts.core.lookup.lucene.runner.LuceneMatcher;
import com.collective.concepts.core.lookup.result.ResultListener;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LuceneLookupEngine extends AbstractLookupEngine {

    private static Logger logger = Logger.getLogger(LuceneLookupEngine.class);

    private int processes;

    private ExecutorService executorService;

    private LuceneTokenizer tokenizer;

    public LuceneLookupEngine(UserDefinedConceptStore conceptStore, int processes) {
        super(conceptStore);
        this.processes = processes;
        executorService = Executors.newFixedThreadPool(this.processes);
    }

    public void lookup(String text, long userId, ResultListener resultListener)
            throws UserDefinedConceptLookupEngineException {
        List<Concept> concepts;
        try {
            concepts = conceptStore.getUserConcepts(userId);
        } catch (UserDefinedConceptStoreException e) {
            throw  new UserDefinedConceptLookupEngineException("", e);
        }
        Map<Integer, String[]> splitTokens;
        List<Future<LookupReport>> futures = new ArrayList<Future<LookupReport>>();
        for (Concept concept : concepts) {

            //added to prevent tokenizer failures
            int maximumNgramSizeAllowed = 2;
            if (concept.getMaximumNgramSize() > maximumNgramSizeAllowed) {
                maximumNgramSizeAllowed = concept.getMaximumNgramSize();
            }

            tokenizer = new LuceneTokenizer(text, maximumNgramSizeAllowed);
            String[] tokens;
            try {
                tokens = tokenizer.tokenize();
            } catch (Exception e) {
                throw new UserDefinedConceptLookupEngineException("", e);
            }
            splitTokens = split(tokens, this.processes);
            for (Integer i : splitTokens.keySet()) {
                if (!executorService.isShutdown()) {
                    LuceneMatcher matcher = new LuceneMatcher(
                            i,
                            concept,
                            splitTokens.get(i),
                            resultListener
                    );
                    Future<LookupReport> future = executorService.submit(matcher);
                    futures.add(future);
                }
            }
        }
        executorService.shutdown();
        while(!executorService.isTerminated()) {
            // nop. Let's wait for the others, mate.
        }
        for(Future<LookupReport> future : futures) {
            LookupReport report;
            try {
                report = future.get();
            } catch (InterruptedException e) {
                throw new UserDefinedConceptLookupEngineException("", e);
            } catch (ExecutionException e) {
                throw new UserDefinedConceptLookupEngineException("", e);
            }
            logger.info("Lookup report: " + report);
        }
    }

    private Map<Integer, String[]> split(String[] tokens, int processes) {
        int tokensForProcess = tokens.length / processes;
        int rest = tokens.length - (tokensForProcess * processes);
        Map<Integer, String[]> splitTokens = new HashMap<Integer, String[]>();
        for (int processIndex = 0; processIndex < processes; processIndex++) {
            String[] processTokens = new String[tokensForProcess];
            for (int index = 0; index < tokensForProcess; index++) {
                processTokens[index] = tokens[processIndex*tokensForProcess + index];
            }
            splitTokens.put(processIndex, processTokens);
        }
        if(rest > 0) {
            String[] remainingTokens = new String[rest];
            int i=0;
            for(int index = (tokens.length - rest); index < tokens.length; index++) {
                remainingTokens[i] = tokens[index];
                i++;
            }
            splitTokens.put(processes, remainingTokens);
        }
        return splitTokens;
    }
}
