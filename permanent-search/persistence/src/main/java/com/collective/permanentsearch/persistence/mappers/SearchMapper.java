package com.collective.permanentsearch.persistence.mappers;

import com.collective.permanentsearch.model.Search;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public interface SearchMapper {

    public void insert(Search search);

    public Search select(Long id);

    public void delete(Long id);

    public void update(Search message);

    public List<Search> selectOldest(Long maxAmount);

    public List<Search> selectOldestProjectSearches(Long maxAmount);

    public List<Search> selectOldestUserSearches(Long maxAmount);

    public List<Search> selectByUserId(@Param("userId") Long userId);

    public List<Search> selectByProjectId(@Param("projectId") Long projectId);

    public void insertUserSearch(@Param("userId")   Long userId,
                                 @Param("searchId") Long searchId);

    public void deleteUserSearch(Long userId, Long searchId);

    public void insertProjectSearch(@Param("projectId") Long projectId,
                                    @Param("searchId")  Long searchId);

    void deleteProjectSearch(Long projectId, Long searchId);

    List<Search> selectAllSearches();
}
