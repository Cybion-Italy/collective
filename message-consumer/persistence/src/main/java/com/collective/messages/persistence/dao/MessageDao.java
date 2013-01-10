package com.collective.messages.persistence.dao;

import com.collective.messages.persistence.ConnectionFactory;
import com.collective.messages.persistence.mappers.MessageMapper;
import com.collective.messages.persistence.model.Message;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Properties;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class MessageDao extends ConfigurableDao {

    private static final Logger logger = Logger.getLogger(MessageDao.class);

    public MessageDao(Properties properties) {
        super(properties);
    }

    public Message selectMessage(Long id) {
        logger.info("select message by id: '" + id + "'");
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        MessageMapper messageMapper = session.getMapper(MessageMapper.class);
        try {
            Message message = messageMapper.selectMessage(id);
            return message;
        } finally {
            session.close();
        }
    }

    public void insertMessage(Message message) {
        logger.info("saving message: " + message);
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        MessageMapper messageMapper = session.getMapper(MessageMapper.class);
        try {
            messageMapper.insertMessage(message);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void deleteMessage(Long id) {
        logger.info("deleting message with id: " + id);
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        MessageMapper messageMapper = session.getMapper(MessageMapper.class);
        try {
            messageMapper.deleteMessage(id);
            session.commit();
        } finally {
            session.close();
        }
    }

    public List<Message> selectLastMessagesByExample(Message message, int maxItems) {
        //always ordered by date
        logger.info("selecting last " + maxItems + " messages like: " + message);
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        MessageMapper messageMapper = session.getMapper(MessageMapper.class);
        List<Message> messages;
        try {
            messages = messageMapper.selectLastMessagesByExample(message, maxItems);
            return messages;
        } finally {
            session.close();
        }
    }

    public void updateMessage(Message message) {
        logger.info("updating message: " + message);
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        MessageMapper messageMapper = session.getMapper(MessageMapper.class);
        try {
            messageMapper.updateMessage(message);
            session.commit();
        } finally {
            session.close();
        }
    }
}
