package com.collective.persistencewebresources.persistence.mappers;

import com.collective.model.persistence.Source;

import java.util.List;

public interface SourceMapper {

	void insertFonte(Source source);

    Source selectFonte(Integer id);

    Source selectFonteAndFontiRss(Integer id);

    List<Source> selectAllFonte();

    void deleteFonte(Integer id);

    List<Source> selectAllFonteAndFontiRss();

    List<Source> selectAllFonteAndValidFontiRss();
}
