package com.collective.profilingline.skos.repository;

import com.collective.profilingline.skos.SkosSubject;
import com.collective.profilingline.skos.repository.mappers.SkosMapper;
import org.apache.ibatis.session.SqlSession;

import java.net.URI;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com ) 
 */
public class SkosSubjectDao {

    public SkosSubject getSkosSubjectByURI(URI uri) {
        SqlSession session = ConnectionFactory.getSession().openSession();
		SkosMapper skosMapper = session.getMapper(SkosMapper.class);
		SkosSubject skosSubject = skosMapper.selectSkosSubjectByURI(uri.getPath().split("/")[2].split(":")[1]);
		session.close();
		return skosSubject;
    }

    public URI getResourceByURI(URI uri) {
        throw new UnsupportedOperationException("NIY");
    }

    public SkosSubject getMostRepresentativeSkosSubjectByResourceURI(URI uri) {
        SqlSession session = ConnectionFactory.getSession().openSession();
		SkosMapper skosMapper = session.getMapper(SkosMapper.class);
		List<SkosSubject> skosSubjects = skosMapper.selectMostRepresentativeSkosSubjectByResourceURI(
                uri.getPath().split("/")[2]
        );
        SkosSubject skosSubject = null;
        if(skosSubjects.size() > 0) {
            skosSubject = skosSubjects.get(0);
            // TODO (high): are you sure do you want just to get the first? should be unique.            
        }
		session.close();
		return skosSubject;
    }

}
