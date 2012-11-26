package com.collective.recommender.runner;

import com.collective.permanentsearch.model.Search;
import com.collective.permanentsearch.persistence.dao.SearchDao;

import java.util.List;
import java.util.Properties;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class MyBatisPermanentSearchStorage {

    private SearchDao searchDao;

    public MyBatisPermanentSearchStorage(Properties properties) {
        searchDao = new SearchDao(properties);
    }

    public List<Search> selectAllPermanentSearches() {
        return this.searchDao.selectAllSearches();
    }
}
