package com.collective.usertests.persistence.dao;

import com.collective.usertests.model.Reason;
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
public class ReasonDaoTestCase extends AbstractDaoTestCase {

    private ReasonDao reasonDao;
    private Logger logger = Logger.getLogger(ReasonDaoTestCase.class);


    public ReasonDaoTestCase() {
        super();
    }

    @BeforeClass
    public void setUpBefore() throws Exception {
        reasonDao = new ReasonDao(userFeedbackPersistenceConfiguration
                .getProperties());
    }

    @AfterClass
    public void tearDownAfter() throws Exception {
        reasonDao = null;
    }

    @Test
    public void shouldTestCRUD() throws Exception {
        Reason reasonInserted = DomainFixture.newTestReason();
        Reason reasonToBeRetrieved = null;

        try {
            // insert a new WebResource
            this.reasonDao.insertReason(reasonInserted);
            Assert.assertNotNull(reasonInserted.getId());
            // check it has been saved
            reasonToBeRetrieved = this.reasonDao
                    .getReason(reasonInserted.getId());

            Assert.assertNotNull(reasonToBeRetrieved);
            Assert.assertEquals(reasonToBeRetrieved.getId(),
                                reasonInserted.getId());
            Assert.assertNotNull(reasonToBeRetrieved.getDescription());

            //assert equals
            Assert.assertTrue(reasonToBeRetrieved.getDescription()
                    .equals(reasonInserted.getDescription()));

            Assert.assertTrue(reasonToBeRetrieved.equals(reasonInserted));

            //update it
            reasonToBeRetrieved.setDescription("new updated description");
            this.reasonDao.updateReason(reasonToBeRetrieved);
            Reason updatedReason = this.reasonDao
                                .getReason(reasonToBeRetrieved.getId());
            Assert.assertTrue(updatedReason.equals(reasonToBeRetrieved));
        } catch (Exception e) {
            logger.debug("exception during test: " + e);
            throw new Exception("error during reason CRUD tests", e);
        } finally {
            // delete it
            this.reasonDao.deleteReason(reasonToBeRetrieved.getId());
            // check that is no longer there
            reasonToBeRetrieved = this.reasonDao
                    .getReason(new Integer(reasonInserted.getId()));
            Assert.assertTrue(reasonToBeRetrieved == null);
        }
    }

    @Test
    public void shouldTestSelectAll() {

        List<Reason> reasons = this.reasonDao.getAllReasons();

        for (Reason reason : reasons) {
            logger.debug("reason: " + reason.toString());
        }
    }
}
