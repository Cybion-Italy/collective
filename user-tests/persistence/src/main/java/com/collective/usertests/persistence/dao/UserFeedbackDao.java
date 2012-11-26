package com.collective.usertests.persistence.dao;

import com.collective.usertests.model.UserFeedback;
import com.collective.usertests.persistence.ConnectionFactory;
import com.collective.usertests.persistence.mappers.UserFeedbackMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class UserFeedbackDao extends ConfigurableDao {

    public UserFeedbackDao(Properties properties) {
        super(properties);
    }

    public void insertUserFeedback(UserFeedback userFeedback) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        UserFeedbackMapper userFeedbackMapper = session.getMapper(UserFeedbackMapper.class);
        try {
            userFeedbackMapper.insertUserFeedback(userFeedback);
            session.commit();
        } finally {
            session.close();
        }
    }

     public UserFeedback getUserFeedback(Long id) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        UserFeedbackMapper userFeedbackMapper = session.getMapper(UserFeedbackMapper.class);
        try {
            UserFeedback userFeedback = userFeedbackMapper.getUserFeedback(id);
            return userFeedback;
        } finally {
            session.close();
        }
    }

    public List<UserFeedback> getAllUserFeedbackByUserId(Long userId) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        UserFeedbackMapper userFeedbackMapper = session.getMapper(UserFeedbackMapper.class);
        try {
            List<UserFeedback> userFeedbacks = userFeedbackMapper.getAllUserFeedbackByUserId(userId);
            return userFeedbacks;
        } finally {
            session.close();
        }
    }

    public void deleteUserFeedback(Long id) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        UserFeedbackMapper userFeedbackMapper = session.getMapper(UserFeedbackMapper.class);
        try {
            userFeedbackMapper.deleteUserFeedback(id);
            session.commit();
        } finally {
            session.close();
        }
    }
}
