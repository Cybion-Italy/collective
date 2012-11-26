package com.collective.analyzer.runner;

import com.collective.concepts.core.DefaultUserDefinedConceptStoreImpl;
import com.collective.concepts.core.UserDefinedConceptStore;
import com.collective.analyzer.storage.DocumentStorage;
import com.collective.analyzer.configuration.ConfigurationManager;
import com.collective.analyzer.storage.MyBatisWebResourceRepository;
import com.collective.analyzer.storage.WebResourceRepository;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.configuration.KVStoreConfiguration;
import tv.notube.commons.storage.kvs.mybatis.MyBatisKVStore;
import tv.notube.commons.storage.model.fields.serialization.SerializationManager;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class DocumentAnalyzerRunner {

    private static final Logger logger = Logger.getLogger(DocumentAnalyzerRunner.class);

    private final static String CONFIGURATION = "configuration";
    private final static String CUSTOM_CONCEPTS_CONFIG = "customconcepts";

    public static void main(String[] args) {

        Options options = new Options();
        options.addOption(CONFIGURATION, true, "XML Configuration file");
        options.addOption(CUSTOM_CONCEPTS_CONFIG, true,
                "XML configuration file for custom concepts repository");
        CommandLineParser commandLineParser = new PosixParser();
        if (args.length != 4) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Runner", options);
            System.exit(-1);
        }
        CommandLine commandLine;
        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            logger.error("Error while parsing arguments", e);
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Runner", options);
            System.exit(-1);
            throw new RuntimeException();
        }

        String customConceptsConfiguration =
                commandLine.getOptionValue(CUSTOM_CONCEPTS_CONFIG);

        String confFilePath = commandLine.getOptionValue(CONFIGURATION);

        /**
         * Parse the configuration file and instantiates all the needed dependencies
         */
        logger.info("Loading configuration from: '" + confFilePath + "'");
        ConfigurationManager configurationManager =
                ConfigurationManager.getInstance(confFilePath);

        Long documentsToAnalyze = configurationManager.getDocumentsToAnalyze();
        DocumentStorage documentStore = configurationManager.getDocumentStorage();
        WebResourceRepository webResourceRepository =
                new MyBatisWebResourceRepository(configurationManager
                    .getWebResourcesPersistenceConfiguration()
                    .getProperties());
        UserDefinedConceptStore userDefinedConceptStore =
                new DefaultUserDefinedConceptStoreImpl(getKVS(customConceptsConfiguration));

        logger.info("analysing " + documentsToAnalyze + " webResources");

        DocumentAnalyzer documentAnalyzer =
                new DocumentAnalyzer(
                        documentStore,
                        documentsToAnalyze,
                        webResourceRepository,
                        userDefinedConceptStore
                );

        try {
            documentAnalyzer.run();
        } catch (DocumentAnalyzerException e) {
            logger.error("Error while analysing document", e);
            System.exit(-1);
        }
        logger.info("DocumentAnalyzer ended");
        //should close files first?
        System.exit(0);
    }

    /* The filename passed as parameter should be found in the resources directory,
     * since the produced jar is specified as the path.
     * This is caused by the kvs configuration. */
    private static KVStore getKVS(String kvsConfigFile) {
        final String CONFIGURATION = kvsConfigFile;
        KVStoreConfiguration configuration =
                        tv.notube.commons.storage.kvs.configuration.ConfigurationManager
                                .getInstance(CONFIGURATION)
                                .getKVStoreConfiguration();
        logger.debug("kvs configuration: " + configuration.getProperties().toString());
                return new MyBatisKVStore(
                        configuration.getProperties(),
                        new SerializationManager()
                );
    }
}
