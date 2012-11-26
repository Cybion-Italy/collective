package com.collective.usertests.persistence.utils;

import com.collective.usertests.model.Reason;
import com.collective.usertests.model.UserFeedback;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class DomainFixture {



    public static Reason newTestReason() {
        Reason reason = new Reason();
        reason.setDescription("fake reason description");
        return reason;
    }

    public static UserFeedback newTestUserFeedbackComplete() {
        UserFeedback userFeedback = new UserFeedback();
        userFeedback.setLike(true);
        userFeedback.setUrlResource("http://www.testresource.com/index.html");
        userFeedback.setProjectId(498584L);
        userFeedback.setUserId(532523L);
        userFeedback.setDate(new DateTime());
        userFeedback.setReasonId(3);
        return userFeedback;
    }

    public static UserFeedback newTestUserFeedbackInComplete() {
        UserFeedback userFeedback = new UserFeedback();
        userFeedback.setLike(false);
        userFeedback.setUrlResource("http://www.test.new.resource.com/index.html");
//        userFeedback.setProjectId(498584L);
        userFeedback.setUserId(5325123L);
        userFeedback.setDate(new DateTime());
        userFeedback.setReasonId(2);
        return userFeedback;
    }
    public static List<UserFeedback> getNUserFeedbacks(Long userId, int n) {

        List<UserFeedback> userFeedbacks = new ArrayList<UserFeedback>();

        for (int i = 0; i < n; i++) {
            UserFeedback userFeedback = newUserFeedbackForUser(userId);
            userFeedbacks.add(userFeedback);
        }
        return userFeedbacks;
    }

    private static UserFeedback newUserFeedbackForUser(Long userId) {
    UserFeedback userFeedback = new UserFeedback();
        userFeedback.setLike(true);
        userFeedback.setUrlResource("http://www.testresource.com/index.html");
        userFeedback.setProjectId(498584L);
        userFeedback.setUserId(userId);
        userFeedback.setDate(new DateTime());
        userFeedback.setReasonId(5);
        return userFeedback;    }
}
