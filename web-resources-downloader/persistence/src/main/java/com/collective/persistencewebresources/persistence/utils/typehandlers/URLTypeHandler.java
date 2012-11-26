package com.collective.persistencewebresources.persistence.utils.typehandlers;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class URLTypeHandler implements TypeHandler {

    private static Logger logger = Logger.getLogger(URLTypeHandler.class);

    public void setParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        URL url = (URL) o;
		preparedStatement.setString(i, url.toString());
    }

    public Object getResult(ResultSet resultSet, String columnName) throws SQLException {
        String urlString = resultSet.getString(columnName);
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            logger.error("error while building url: " + urlString);
            //TODO
            e.printStackTrace();
        }
        return url;
    }

    public Object getResult(CallableStatement callableStatement, int i) throws SQLException {
        String urlString = callableStatement.getString(i);
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            logger.error("error while building url: " + urlString);
            //TODO
            e.printStackTrace();
        }
        return url;
    }
}
