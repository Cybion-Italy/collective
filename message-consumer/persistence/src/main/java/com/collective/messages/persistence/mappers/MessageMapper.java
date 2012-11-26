package com.collective.messages.persistence.mappers;

import com.collective.messages.persistence.model.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public interface MessageMapper {

    public void insertMessage(Message message);

    public Message selectMessage(Long id);

    public void deleteMessage(Long id);

    public List<Message> selectLastMessagesByExample(@Param("message") Message message,
                                                     @Param("maxItems") int maxItems);
    public void updateMessage(Message message);
}
