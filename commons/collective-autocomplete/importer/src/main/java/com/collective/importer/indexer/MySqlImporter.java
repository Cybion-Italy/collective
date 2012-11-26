package com.collective.importer.indexer;

import com.collective.importer.Line;
import com.collective.importer.indexer.persistence.dao.LineDao;

import java.net.URL;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class MySqlImporter {

    private LineDao lineDao;

    public MySqlImporter() {
        lineDao = new LineDao();
    }

    public void storeLine(Line line) throws MySqlImporterException {
        if(lineDao.selectLine(line.getUrl().toString()) == null) {
            lineDao.insertLine(line);
        }
    }

    public Line getLine(URL url) {
        return lineDao.selectLine(url.toString());
    }

    public List<Line> suggest(String sstr, long owner, boolean filter) {
        if(filter) {
            return lineDao.suggestLineFiltered(sstr, owner);
        }
        return lineDao.suggestLine(sstr, owner);
    }

    public void deleteLine(URL url) {
        lineDao.deleteLine(url.toString());
    }
}
