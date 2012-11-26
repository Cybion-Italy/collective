package com.collective.permanentsearch.persistence.typehandlers;

import com.collective.permanentsearch.model.LabelledURI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class LabelledURIArrayListTypeHandler implements TypeHandler<ArrayList<LabelledURI>> {

    private static Logger logger = Logger.getLogger(LabelledURIArrayListTypeHandler.class);

    public void setParameter(PreparedStatement preparedStatement,
                             int i, ArrayList<LabelledURI> labelledURIs,
                             JdbcType jdbcType) throws SQLException {
        //parse List content into gson serialization
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                        .create();

        Type listType = new TypeToken<ArrayList<LabelledURI>>() {}.getType();
        String jsonString = "";
        jsonString = gson.toJson(labelledURIs, listType);
        preparedStatement.setString(i, jsonString);
    }

    public ArrayList<LabelledURI> getResult(ResultSet resultSet, String columnName)
            throws SQLException {

        Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .create();

        String jsonConcepts = resultSet.getString(columnName);

        //it seems gson is not able to deserialize to ArrayList
        Type listType = new TypeToken<LinkedList<LabelledURI>>() {}.getType();

        LinkedList<LabelledURI> labelledURIs =
                gson.fromJson(jsonConcepts, listType);

        ArrayList<LabelledURI> labelledURIsArrayList =
                new ArrayList<LabelledURI>();
        labelledURIsArrayList.addAll(labelledURIs);
        return labelledURIsArrayList;
    }

    public ArrayList<LabelledURI> getResult(CallableStatement callableStatement,
                                       int columnIndex)
            throws SQLException {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                        .create();
        String jsonConcepts = callableStatement.getString(columnIndex);

        Type listType = new TypeToken<LinkedList<LabelledURI>>() {}.getType();

        LinkedList<LabelledURI> labelledURIs =
                gson.fromJson(jsonConcepts, listType);


        ArrayList<LabelledURI> labelledURIsArrayList =
                new ArrayList<LabelledURI>();
        labelledURIsArrayList.addAll(labelledURIs);
        return labelledURIsArrayList;
    }
}
