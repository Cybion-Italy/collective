package com.collective.resources.userfeedback;

import com.google.gson.annotations.Expose;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class CreatedUserFeedbackResponse {

    @Expose
    private Long userFeedbackId;

    public CreatedUserFeedbackResponse() {}

    public CreatedUserFeedbackResponse(Long userFeedbackId) {
        this.userFeedbackId = userFeedbackId;
    }

    public Long getUserFeedbackId() {
        return userFeedbackId;
    }

    public void setUserFeedbackId(Long userFeedbackId) {
        this.userFeedbackId = userFeedbackId;
    }
}
