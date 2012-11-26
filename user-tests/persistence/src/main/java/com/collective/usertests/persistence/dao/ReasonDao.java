package com.collective.usertests.persistence.dao;

import com.collective.usertests.model.Reason;
import com.collective.usertests.persistence.ConnectionFactory;
import com.collective.usertests.persistence.mappers.ReasonMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */

//TODO manage dao exceptions?
public class ReasonDao extends ConfigurableDao {

    public ReasonDao(Properties properties) {
        super(properties);
    }

    public void insertReason(Reason reason) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        ReasonMapper reasonMapper = session.getMapper(ReasonMapper.class);
        try {
            reasonMapper.insertReason(reason);
            session.commit();
        } finally {
            session.close();
        }
    }

    public Reason getReason(int id) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        ReasonMapper sourceRssMapper = session.getMapper(ReasonMapper.class);
        try {
            Reason reason = sourceRssMapper.getReason(id);
            return reason;
        } finally {
            session.close();
        }
    }

    public void updateReason(Reason reason) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        ReasonMapper reasonMapper = session.getMapper(ReasonMapper.class);
        try {
            reasonMapper.updateReason(reason);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void deleteReason(int id) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        ReasonMapper reasonMapper = session.getMapper(ReasonMapper.class);
        try {
            reasonMapper.deleteReason(id);
            session.commit();
        } finally {
            session.close();
        }
    }

    public List<Reason> getAllReasons() {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        ReasonMapper reasonMapper = session.getMapper(ReasonMapper.class);
        List<Reason> reasons = new ArrayList<Reason>();
        try {
            reasons = reasonMapper.getAllReasons();
            return reasons;
        } finally {
            session.close();
        }
    }
}
