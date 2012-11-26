package com.collective.consumer.runner;

import com.collective.consumer.QueueGateway;
import com.collective.consumer.QueueGatewayException;
import com.collective.consumer.configuration.ConfigurationManager;
import com.collective.consumer.listeners.MessageListenerDispatcher;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
/* TODO: implement a shutdown hook to close gracefully when ctrl-c or ctrl-x
* http://www.developerfeed.com/threads/tutorial/understanding-java-shutdown-hook
* http://stackoverflow.com/questions/2921945/java-useful-example-of-a-shutdown-hook
* */
public class Runner {

    private final static Logger logger = Logger.getLogger(Runner.class);

    public static void main(String[] args) {
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

        logger.info("Loading configuration from: '" + confFilePath + "'");
        ConfigurationManager configurationManager =
                ConfigurationManager.getInstance(confFilePath);

        //TODO: get a list of queues to listen/monitor and spawn processes that each process/consume each queue
        /* build a gateway */
        QueueGateway queueGateway = new QueueGateway(configurationManager.getQueueGatewayConfiguration());

        /* build a dispatcher with persistence configuration */
        MessageListenerDispatcher messageListenerDispatcher =
                new MessageListenerDispatcher(configurationManager.getMessagesPersistenceConfiguration());

        /* set the dispatcher to the queueGateway */
        queueGateway.setMessageListenerDispatcher(messageListenerDispatcher);

        try {
            queueGateway.run();
        } catch (QueueGatewayException e) {
            logger.error("Error while running queueGateway", e);
            System.exit(-1);
        }
    }
}
