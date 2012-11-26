package com.collective.persistencewebresources.persistence.dao;

import com.collective.model.persistence.Source;
import com.collective.model.persistence.SourceRss;
import com.collective.persistencewebresources.persistence.ConnectionFactory;
import com.collective.persistencewebresources.persistence.mappers.SourceRssMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Properties;

public class SourceRssDao extends ConfigurableDao{

    public SourceRssDao(Properties properties) {
        super(properties);
    }

    public SourceRss selectById(Integer id) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        try {
            SourceRssMapper sourceRssMapper = session.getMapper(SourceRssMapper.class);
            SourceRss sourceRss = sourceRssMapper.selectFonteRss(id);
            return sourceRss;
        } finally {
            session.close();
        }
    }

    public List<SourceRss> selectAll() {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        try {
            SourceRssMapper sourceRssMapper = session.getMapper(SourceRssMapper.class);
            List<SourceRss> sourcesRss = sourceRssMapper.selectAllFonteRss();
            return sourcesRss;
        } finally {
            session.close();
        }
    }

    public List<SourceRss> selectFonteRssByFonte(Source source) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        try {
            SourceRssMapper sourceRssMapper = session.getMapper(SourceRssMapper.class);
            List<SourceRss> sourcesRss = sourceRssMapper.selectFonteRssByFonte(source);
            return sourcesRss;
        } finally {
            session.close();
        }
    }

    public SourceRss selectFonteRssByExample(SourceRss sourceRss) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        try {
            SourceRssMapper sourceRssMapper = session.getMapper(SourceRssMapper.class);
            SourceRss sourceRssDb = sourceRssMapper.selectFonteRssByExample(sourceRss);
            return sourceRssDb;
        } finally {
            session.close();
        }
    }

    public void update(SourceRss sourceRss) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        try {
            SourceRssMapper sourceRssMapper = session.getMapper(SourceRssMapper.class);
            sourceRssMapper.updateFonteRssIfNecessary(sourceRss);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void insert(SourceRss sourceRss) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        try {
            SourceRssMapper sourceRssMapper = session.getMapper(SourceRssMapper.class);
            sourceRssMapper.insertFonteRss(sourceRss);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void delete(Integer id) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        try {
            SourceRssMapper sourceRssMapper = session.getMapper(SourceRssMapper.class);
            sourceRssMapper.deleteFonteRss(id);
            session.commit();
        } finally {
            session.close();
        }
    }
}
