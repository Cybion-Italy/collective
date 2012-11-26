package com.collective.usertests.persistence.mappers;

import com.collective.usertests.model.UserFeedback;

import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public interface UserFeedbackMapper {

    public void insertUserFeedback(UserFeedback userFeedback);
    public UserFeedback getUserFeedback(Long id);
    public void updateUserFeedback(UserFeedback userFeedback);
    public void deleteUserFeedback(Long id);
    public List<UserFeedback> getAllUserFeedbackByUserId(Long userId);
}
