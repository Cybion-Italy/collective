package com.collective.importer.indexer.persistence.mapper;

import com.collective.importer.Line;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface LineMapper {

    public void insertLine(Line line);

    public Line selectLine(
            @Param("url") String url
    );

    public void deleteLine(
            @Param("url") String url
    );

    public List<Line> selectLineBySubstring(
            @Param("substring") String subString,
            @Param("owner") long owner);

    List<Line> selectLineBySubstringFiltered(
            @Param("substring") String subString,
            @Param("owner")  long owner);
}
