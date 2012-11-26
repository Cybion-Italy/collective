package com.collective.persistencewebresources.persistence.utils.typehandlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

public class JodaTimeTypeHandler implements TypeHandler {

    private static Logger logger = Logger.getLogger(JodaTimeTypeHandler.class);

	public void setParameter(PreparedStatement ps, int i, Object parameter,
			JdbcType jdbcType) throws SQLException {
		DateTime dateTime = (DateTime) parameter;
		ps.setLong(i, dateTime.getMillis());
	}

	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		long timeMillis = rs.getLong(columnName);
		DateTime dateTime = new DateTime(timeMillis);
		return dateTime;
	}

	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		long timeMillis = cs.getLong(columnIndex);
		DateTime dateTime = new DateTime(timeMillis);
		return dateTime;
	}

}
