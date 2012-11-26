package com.collective.importer.indexer.persistence.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class URLTypeHandler implements TypeHandler {

    public void setParameter(
            PreparedStatement preparedStatement,
            int i,
            Object o,
            JdbcType jdbcType
    ) throws SQLException {
        preparedStatement.setString(i, o.toString());
    }

    public Object getResult(ResultSet resultSet, String s) throws SQLException {
        try {
            return new URL(resultSet.getString(s));
        } catch (MalformedURLException e) {
            throw new SQLException("value '" + resultSet.getString(s)+ "' is not a valid URL", e);
        }
    }

    public Object getResult(CallableStatement callableStatement, int i) throws SQLException {
        return callableStatement.getString(i);
    }
}
