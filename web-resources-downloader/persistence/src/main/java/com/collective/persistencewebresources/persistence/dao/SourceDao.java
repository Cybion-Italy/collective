package com.collective.persistencewebresources.persistence.dao;

import com.collective.model.persistence.Source;
import com.collective.persistencewebresources.persistence.ConnectionFactory;
import com.collective.persistencewebresources.persistence.mappers.SourceMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SourceDao extends ConfigurableDao {

    public SourceDao(Properties properties) {
        super(properties);
    }

    public List<Source> selectAllFonte() {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        try {
            SourceMapper sourceMapper = session.getMapper(SourceMapper.class);
            List<Source> fonti = sourceMapper.selectAllFonte();
            return fonti;
        } finally {
            session.close();
        }
    }

    public Source selectFonteAndFontiRss(Integer id) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        try {
            SourceMapper sourceMapper = session.getMapper(SourceMapper.class);
            Source source = sourceMapper.selectFonteAndFontiRss(id);
            return source;
        } finally {
            session.close();
        }
    }

    @Deprecated
    public List<Source> selectAllFonteAndFontiRss() {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        try {
            SourceMapper sourceMapper = session.getMapper(SourceMapper.class);
            List<Source> fonti = sourceMapper.selectAllFonteAndFontiRss();
            return fonti;
        } finally {
            session.close();
        }
    }

    public List<Source> selectAllFonteAndValidFontiRss() {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        List<Source> fonti = new ArrayList<Source>();
        try {
            SourceMapper sourceMapper = session.getMapper(SourceMapper.class);
            fonti.addAll(sourceMapper.selectAllFonteAndValidFontiRss());
        } finally {
            session.close();
        }
        return fonti;
    }

}
