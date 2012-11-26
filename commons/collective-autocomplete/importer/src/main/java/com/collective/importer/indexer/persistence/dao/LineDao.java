package com.collective.importer.indexer.persistence.dao;

import com.collective.importer.Line;
import com.collective.importer.indexer.persistence.ConnectionFactory;
import com.collective.importer.indexer.persistence.mapper.LineMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LineDao {

    private static final Logger logger = Logger.getLogger(LineDao.class);

    public Line selectLine(String url) {
        logger.info("looking for line with url '" + url + "'");
        SqlSession session = ConnectionFactory.getSession().openSession();
        LineMapper lineMapper = session.getMapper(LineMapper.class);
        try {
            return lineMapper.selectLine(url);
        } finally {
            session.close();
        }
    }

    public List<Line> suggestLine(String subString, long owner) {
        logger.info("looking for line with substring is '" + subString + "'");
        SqlSession session = ConnectionFactory.getSession().openSession();
        LineMapper lineMapper = session.getMapper(LineMapper.class);
        try {
            return lineMapper.selectLineBySubstring(subString + "%", owner);
        } finally {
            session.close();
        }
    }

    public void insertLine(Line line) {
        logger.info("inserting line '" + line + "'");
        SqlSession session = ConnectionFactory.getSession().openSession();
        LineMapper lineMapper = session.getMapper(LineMapper.class);
        try {
            lineMapper.insertLine(line);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void deleteLine(String url) {
        logger.info("deleting line with url: " + url);
        SqlSession session = ConnectionFactory.getSession().openSession();
        LineMapper lineMapper = session.getMapper(LineMapper.class);
        try {
            lineMapper.deleteLine(url);
            session.commit();
        } finally {
            session.close();
        }
    }

    public List<Line> suggestLineFiltered(String subString, long owner) {
        logger.info("looking for line with substring is '" + subString + "'");
        SqlSession session = ConnectionFactory.getSession().openSession();
        LineMapper lineMapper = session.getMapper(LineMapper.class);
        try {
            return lineMapper.selectLineBySubstringFiltered(subString + "%", owner);
        } finally {
            session.close();
        }
    }
}

