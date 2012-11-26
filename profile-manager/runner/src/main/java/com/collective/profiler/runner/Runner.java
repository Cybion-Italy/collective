package com.collective.profiler.runner;

import com.collective.profiler.Profiler;
import com.collective.profiler.ProfilerException;
import com.collective.profiler.configuration.ConfigurationManager;
import com.collective.profiler.container.ProfilingLineContainer;
import com.collective.profiler.data.DataManager;
import com.collective.profiler.storage.ProfileStore;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

/**
 * Main application's entry point. It implement a simple command
 * line interface.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Runner {

    private static Logger logger = Logger.getLogger(Runner.class);

    public static void main(String args[]) {

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
            logger.error("Error while parsing arguments", e);
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Runner", options);
            System.exit(-1);
        }
        String confFilePath = commandLine.getOptionValue(CONFIGURATION);

        /**
         * Parse the configuration file and instantiates all the needed dependencies
         */
        logger.info("Loading configuration from: '" + confFilePath + "'");
        ConfigurationManager configurationManager =
                ConfigurationManager.getInstance(confFilePath);
        ProfilingLineContainer profilingLineContainer = configurationManager.getProfilingLineContainer();
        logger.info("ProfilingLineContainer properly instantiated with the following "
                + profilingLineContainer.getNumberOfProfilingLines() +  " lines: '"
                + profilingLineContainer.getProfilingLineNames() + "'");

        DataManager dataManager = configurationManager.getDataManager();
        logger.info("DataManager properly instantiated.");
        ProfileStore profileStore = configurationManager.getProfileStore();
        logger.info("DataManager properly instantiated.");

        Profiler profiler = new Profiler(dataManager, profilingLineContainer, profileStore, null);
        try {
            profiler.run();
        } catch (ProfilerException e) {
            logger.error("Error while profiling process", e);
            System.exit(-1);
        }
    }

}
