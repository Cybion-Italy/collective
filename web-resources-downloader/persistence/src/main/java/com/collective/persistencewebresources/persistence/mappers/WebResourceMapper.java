package com.collective.persistencewebresources.persistence.mappers;

import com.collective.model.persistence.SourceRss;
import com.collective.model.persistence.WebResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WebResourceMapper {

	void insertUrl(WebResource webResource);

	WebResource selectUrl(Integer id);

    List<WebResource> selectLastExtractedUrls(SourceRss sourceRss);

    List<WebResource> selectAllUrls();

	void deleteUrl(Integer id);

//    List<WebResource> selectAllUrlsByExample(WebResource webResource);

	List<WebResource> selectSomeUrlsByExample(@Param("webResource") WebResource notAnalyzedWebResource,
                                              @Param("limit") int limit);

    void updateUrl(WebResource webResource);
}
