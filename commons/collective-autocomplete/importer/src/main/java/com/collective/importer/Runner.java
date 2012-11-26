package com.collective.importer;

import com.collective.importer.indexer.MySqlImporter;
import com.collective.importer.indexer.MySqlImporterException;
import com.collective.importer.reader.NQReader;
import com.collective.importer.reader.NQReaderException;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Runner {

    private static Logger logger = Logger.getLogger(Runner.class);

    private static NQReader nqReader;

    private static MySqlImporter mySqlImporter;

    public static void main(String[] args) {

        String nqFilePath = args[0];

        logger.info("reading NQ from: '" + nqFilePath + "'");

        File nqfile = new File(nqFilePath);
        if (!nqfile.exists()) {
            logger.error("File '" + nqFilePath + "' does not exist");
            System.exit(-1);
            return;
        }
        nqReader = new NQReader(nqfile);
        try {
            nqReader.parse();
        } catch (NQReaderException e) {
            logger.error("Error while parsing file '" + nqFilePath + "'.", e);
            System.exit(-1);
            return;
        }
        mySqlImporter = new MySqlImporter();
        int counter = 0;
        while (nqReader.hasNextLine()) {
            Line line;
            try {
                line = nqReader.nextLine();
            } catch (NQReaderException e) {
                logger.error("Error while reading line. Skipping it.", e);
                continue;
            }
            line.setOwner(new Long(0));
            try {
                mySqlImporter.storeLine(line);
            } catch (MySqlImporterException e) {
                logger.error("Error while indexing line '" + line + "'. Skipping it.", e);
                continue;
            }
            counter++;
        }
        logger.info("Indexing done on " + counter + " lines");
    }

}
