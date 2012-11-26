package com.collective.profiler.data;

import com.collective.model.Project;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
/* TODO (low) test is not useful, just an example. should be removed */
public class GsonDeserializationTestCase {

    private Gson gson;

    private String projectJson = "{\n" +
            "   \"id\":17,\n" +
            "   \"name\":\"another test project\",\n" +
            "   \"goals\":\"some, comma, separated, goals \",\n" +
            "   \"url\":\"\",\n" +
            "   \"description\":\"this is my project description\",\n" +
            "   \"members\":[" +
            "\"20\", \"15\", \"44\"]\n" +
            "}";

    private List<String> expectedMembersIds = new ArrayList<String>();

    private Logger logger = Logger.getLogger(GsonDeserializationTestCase.class);

    @BeforeClass
    public void setUp() {
        this.gson = new Gson();

        this.expectedMembersIds.add("20");
        this.expectedMembersIds.add("15");
        this.expectedMembersIds.add("44");
    }

    @AfterClass
    public void tearDown() {
        this.gson = null;
        this.expectedMembersIds = null;
    }

    @Test
    public void shouldDeserializeProjectFromJson() {
        Project deserializedProject = gson.fromJson(projectJson, Project.class);
//        logger.debug("projectJson: " + projectJson);
        logger.debug("deserializedProject: " + deserializedProject);
        Assert.assertNotNull(deserializedProject.getMembersIds());
        Assert.assertEquals(deserializedProject.getMembersIds(), expectedMembersIds);
    }
}
