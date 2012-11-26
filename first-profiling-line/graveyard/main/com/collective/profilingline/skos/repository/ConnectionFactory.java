package com.collective.profilingline.skos.repository;

import org.apache.ibatis.session.SqlSessionFactory;

import java.io.IOException;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class ConnectionFactory {

  private static SqlSessionFactory sqlMapper;

    static {
        try {
            String resource = "sql-map-config.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            sqlMapper = new SqlSessionFactoryBuilder().build(reader, "skos");
        } catch (IOException e) {
            throw new RuntimeException("Error while accessing to myBatis map/dao configuration", e);
        }
    }

    public static SqlSessionFactory getSession(){
	  return sqlMapper;
  }

}

