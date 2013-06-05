package com.collective.downloader.rss.runner;

import com.collective.downloader.rss.WebResourceCreator;
import com.collective.downloader.rss.extractors.WebResourceExtractor;
import com.collective.downloader.rss.EntriesFilter;
import com.collective.downloader.rss.extractors.SourceRssExtractor;
import com.collective.downloader.rss.model.CompareWebResourcesByDate;
import com.collective.downloader.rss.storage.WebSourcesRepository;
import com.collective.model.persistence.Source;
import com.collective.model.persistence.WebResource;
import com.collective.persistencewebresources.persistence.configuration.ConfigurationManager;
import com.collective.persistencewebresources.persistence.configuration.WebResourcesPersistenceConfiguration;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class is the main entry point for the whole application.
 */
public class Runner {

    private static final Logger LOGGER = Logger.getLogger(Runner.class);

    private static ConfigurationManager configurationManager;

    /* TODO (high) refactor logic to download updates from EACH feed rss and then write
     * each time results on database
    * before downloading the next feed rss' updates */

    /**
	 * @param args
	 */
	public static void main(String[] args) {


        /* load configuration file from command line */
        final String CONFIGURATION = "configuration";

        Options options = new Options();
        options.addOption(CONFIGURATION, true, "XML Configuration file.");
        CommandLineParser commandLineParser = new PosixParser();
        CommandLine commandLine = null;
        if(args.length != 2) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Runner", options);
            System.exit(-1);
        }
        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            LOGGER.error("Error while parsing arguments", e);
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Runner", options);
            System.exit(-1);
        }
        String confFilePath = commandLine.getOptionValue(CONFIGURATION);

        /**
         * Parse the configuration file and instantiates all the needed dependencies
         */
        LOGGER.info("Loading configuration from: '" + confFilePath + "'");
        configurationManager =
                ConfigurationManager.getInstance(confFilePath);

        /* initialize WebSourcesRepository with parameters of ConfigurationManager */

        WebResourcesPersistenceConfiguration webResourcesPersistenceConfiguration
                = configurationManager.getWebResourcePersistenceConfiguration();

        WebResourceCreator webResourceCreator = new WebResourceCreator();
        EntriesFilter entitiesFilter = new EntriesFilter();
        SourceRssExtractor sourceRssExtractor =
                new SourceRssExtractor(entitiesFilter, webResourceCreator);

        WebSourcesRepository webSourcesRepository =
                new WebSourcesRepository(webResourcesPersistenceConfiguration.getProperties(),
                                         sourceRssExtractor);

        WebResourceExtractor resourceExtractor =
                new WebResourceExtractor(webSourcesRepository);

        /* main logic: fetch new webResources, sort and store */
		List<WebResource> allRetrievedWebResources =
                resourceExtractor.fetchNewWebResources();
		
		Collections.sort(allRetrievedWebResources,
                new CompareWebResourcesByDate());

        webSourcesRepository.saveAllWebResources(allRetrievedWebResources);

        LOGGER.info(allRetrievedWebResources.size() + " web resources have been extracted");
		if (webSourcesRepository.getNotAnalyedSources().size() > 0) {
            LOGGER.error("Some sources have been not analyzed: '" +
                         buildExceptionMessage(webSourcesRepository.getNotAnalyedSources()) + "'");
        }
	}
	
	private static String buildExceptionMessage(Map<Source,String> sourcesNotAnalyzed){
		String exception="\nPROBLEMS WITH SOME SOURCES\n";
		for (Source source : sourcesNotAnalyzed.keySet()) {
			exception = exception 
			+ "SOURCE  = " + source.getNome() 			+ "\n"
			+ "ERROR = " + sourcesNotAnalyzed.get(source) + "\n";
		}
		return exception;
	}

}
