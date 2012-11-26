package com.collective.permanentsearch.persistence.dao;

import com.collective.permanentsearch.model.Search;
import com.collective.permanentsearch.persistence.ConnectionFactory;
import com.collective.permanentsearch.persistence.mappers.SearchMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class SearchDao extends ConfigurableDao {

    private static final Logger logger = Logger.getLogger(SearchDao.class);

    public SearchDao(Properties properties) {
        super(properties);
    }

    public Search select(Long id) {
        logger.info("select search by id: '" + id + "'");
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        SearchMapper searchMapper = session.getMapper(SearchMapper.class);
        Search search = null;
        try {
           search = searchMapper.select(id);
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
        } finally {
            session.close();
        }
        return search;
    }

    public List<Search> selectAllSearches() {
        logger.info("searching all searches");
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        SearchMapper searchMapper = session.getMapper(SearchMapper.class);
        try {
            List<Search> searches = searchMapper.selectAllSearches();
            return searches;
        } finally {
            session.close();
        }
    }

    public List<Search> selectByUserId(Long userId) {

        logger.info("searching saved searches by userId");
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        SearchMapper searchMapper = session.getMapper(SearchMapper.class);
        try {
            List<Search> searches = searchMapper.selectByUserId(userId);
            return searches;
        } finally {
            session.close();
        }
    }

    public List<Search> selectByProjectId(Long projectId) {

        logger.info("searching saved searches by projectId");
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        SearchMapper searchMapper = session.getMapper(SearchMapper.class);
        try {
            List<Search> searches = searchMapper.selectByProjectId(projectId);
            return searches;
        } finally {
            session.close();
        }
    }

    public List<Search> selectOldest(Long maxAmount) {
        logger.info("select oldest search, limit: '" + maxAmount + "'");
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        SearchMapper searchMapper = session.getMapper(SearchMapper.class);
        try {
            List<Search> searches = searchMapper.selectOldest(maxAmount);
            return searches;
        } finally {
            session.close();
        }
    }

    public List<Search> selectOldestProjectSearches(Long maxAmount) {
        logger.info("select oldest project searches, limit: '" + maxAmount + "'");
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        SearchMapper searchMapper = session.getMapper(SearchMapper.class);
        try {
            List<Search> searches = searchMapper.selectOldestProjectSearches(maxAmount);
            return searches;
        } finally {
            session.close();
        }
    }

    public List<Search> selectOldestUserSearches(Long maxAmount) {
        logger.info("select oldest user searches, limit: '" + maxAmount + "'");
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        SearchMapper searchMapper = session.getMapper(SearchMapper.class);
        try {
            List<Search> searches = searchMapper.selectOldestUserSearches(maxAmount);
            return searches;
        } finally {
            session.close();
        }
    }

    public void insert(Search message) {
        logger.info("saving search: " + message);
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();

        SearchMapper searchMapper = session.getMapper(SearchMapper.class);
        try {
            searchMapper.insert(message);
            session.commit();
        } catch (Exception e) {
            logger.error("erro: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    public void delete(Long id) {
        logger.info("deleting search with id: " + id);
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        SearchMapper searchMapper = session.getMapper(SearchMapper.class);
        try {
            searchMapper.delete(id);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void insertUserSearch(Long userId, Long searchId) {
        logger.info("saving search: '" + searchId + "' for user: '" + userId + "'");
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        SearchMapper searchMapper = session.getMapper(SearchMapper.class);
        try {
            searchMapper.insertUserSearch(userId, searchId);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void insertProjectSearch(Long projectId, Long searchId) {
        logger.info("saving search: '" + searchId + "' for project: '" + projectId + "'");
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        SearchMapper searchMapper = session.getMapper(SearchMapper.class);
        try {
            searchMapper.insertProjectSearch(projectId, searchId);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void deleteUserSearch(Long userId, Long searchId) {
        logger.info("deleting search '" + searchId + "' from userId: " + userId);
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        SearchMapper searchMapper = session.getMapper(SearchMapper.class);
        try {
            searchMapper.deleteUserSearch(userId, searchId);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void deleteProjectSearch(Long projectId, Long searchId) {
        logger.info("deleting search '" + searchId + "' from projectId: " + projectId);
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        SearchMapper searchMapper = session.getMapper(SearchMapper.class);
        try {
            searchMapper.deleteProjectSearch(projectId, searchId);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void update(Search search) {
        logger.info("updating search: " + search);
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        SearchMapper searchMapper = session.getMapper(SearchMapper.class);
        try {
            searchMapper.update(search);
            session.commit();
        } finally {
            session.close();
        }
    }
}
