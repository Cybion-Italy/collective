package com.collective.profilingline.skos.repository.mappers;

import com.collective.profilingline.skos.SkosSubject;

import java.net.URI;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface SkosMapper {

    SkosSubject selectSkosSubjectByURI(String uriSegment);

    URI selectResourceByURI(String uriSegment);

    List<SkosSubject> selectMostRepresentativeSkosSubjectByResourceURI(String uriSegment);

}
