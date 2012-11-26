package com.collective.usertests.persistence.dao;

import com.collective.usertests.model.UserFeedback;
import com.collective.usertests.persistence.utils.DomainFixture;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class UserFeedbackTestCase extends AbstractDaoTestCase {

    private UserFeedbackDao userFeedbackDao;
    private Logger logger = Logger.getLogger(ReasonDaoTestCase.class);


    public UserFeedbackTestCase() {
        super();
    }

    @BeforeClass
    public void setUpBefore() throws Exception {
        userFeedbackDao = new UserFeedbackDao(userFeedbackPersistenceConfiguration
                .getProperties());
    }

    @AfterClass
    public void tearDownAfter() throws Exception {
        userFeedbackDao = null;
    }

   @Test
   public void shouldTestCRUD() {

       UserFeedback userFeedbackToInsert = null;

       try {
           // insert a new WebResource
           userFeedbackToInsert = DomainFixture.newTestUserFeedbackComplete();

           this.userFeedbackDao.insertUserFeedback(userFeedbackToInsert);
           Assert.assertNotNull(userFeedbackToInsert.getId());

           //check it is on db
           UserFeedback userFeedback = this.userFeedbackDao
                   .getUserFeedback(userFeedbackToInsert.getId());

           logger.debug("to insert: " + userFeedbackToInsert.toString());
           logger.debug("got from db: " + userFeedback.toString());

           Assert.assertTrue(userFeedback.equals(userFeedbackToInsert));

       } catch (Exception e) {
           logger.error("exception during CRUD tests: " + e.getMessage());
       } finally {
           // delete it
           this.userFeedbackDao.deleteUserFeedback(userFeedbackToInsert.getId());
           // check that is no longer there
       }
   }

    @Test
    public void shouldTestIncompleteObjectInsert() throws Exception {

       UserFeedback userFeedbackToInsert = null;

       try {
           // insert a new WebResource
           userFeedbackToInsert = DomainFixture.newTestUserFeedbackInComplete();

           this.userFeedbackDao.insertUserFeedback(userFeedbackToInsert);
           Assert.assertNotNull(userFeedbackToInsert.getId());

           //check it is on db
           UserFeedback userFeedback = this.userFeedbackDao
                   .getUserFeedback(userFeedbackToInsert.getId());

           Assert.assertNotNull(userFeedback.getId());
           Assert.assertTrue(userFeedback.equals(userFeedbackToInsert));

       } catch (Exception e) {
           logger.error("exception during CRUD tests: " + e.getMessage());
           throw new Exception("error during userfeedback CRUD tests", e);
       } finally {
           // delete it
           this.userFeedbackDao.deleteUserFeedback(userFeedbackToInsert.getId());
           // check that is no longer there
       }
    }

    @Test
    public void getAllUserFeedbackByUserId() throws Exception {

        Long userId = 95325262L;
        List<UserFeedback> userFeedbackListToInsert =
                DomainFixture.getNUserFeedbacks(userId,5);

       try {
           // insert
           for (UserFeedback userFeedback : userFeedbackListToInsert) {
               this.userFeedbackDao.insertUserFeedback(userFeedback);
           }

           //check they are on db
           List<UserFeedback> userFeedbackList = this.userFeedbackDao
                   .getAllUserFeedbackByUserId(userId);

           Assert.assertTrue(userFeedbackList.size() == userFeedbackListToInsert.size());

       } catch (Exception e) {
           logger.error("exception during CRUD tests: " + e.getMessage());
           throw new Exception("error during userfeedback CRUD tests", e);
       } finally {
           // delete
           for (UserFeedback userFeedback : userFeedbackListToInsert) {
               this.userFeedbackDao.deleteUserFeedback(userFeedback.getId());
           }
       }

    }
}
