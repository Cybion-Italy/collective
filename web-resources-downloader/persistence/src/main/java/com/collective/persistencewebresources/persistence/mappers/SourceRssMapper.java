package com.collective.persistencewebresources.persistence.mappers;


import com.collective.model.persistence.Source;
import com.collective.model.persistence.SourceRss;

import java.util.List;

public interface SourceRssMapper {
    
	SourceRss selectFonteRss(Integer id);

    List<SourceRss> selectAllFonteRss();

    List<SourceRss> selectFonteRssByFonte(Source source);

    SourceRss selectFonteRssByExample(SourceRss sourceRssExample);

    void updateFonteRssIfNecessary(SourceRss sourceRss);

    void insertFonteRss(SourceRss sourceRss);

    void deleteFonteRss(Integer id);
}
