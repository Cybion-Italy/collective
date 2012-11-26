package com.collective.document.analyzer.enrichers.dbpedia;

import com.collective.analyzer.enrichers.dbpedia.model.DBpediaAPIResponse;
import com.collective.analyzer.enrichers.dbpedia.adapters.DBpediaAPIResponseAdapter;
import com.collective.analyzer.enrichers.dbpedia.model.DBpediaResource;
import com.collective.analyzer.enrichers.dbpedia.adapters.DBpediaResourceAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class GsonSerializationTest
{
    private Gson gson;

    private String normalJsonContent = "{\n" +
            "  \"@text\": \"President+Obama+called+Wednesday+on+Congress+to+extend+a+tax+break+for+students+included+in+last+year%27s+economic+stimulus+package%2C+arguing+that+the+policy+provides+more+generous+assistance\",\n" +
            "  \"@confidence\": \"0.5\",\n" +
            "  \"@support\": \"100\",\n" +
            "  \"@types\": \"\",\n" +
            "  \"@sparql\": \"\",\n" +
            "  \"@policy\": \"whitelist\",\n" +
            "  \"Resources\":   [\n" +
            "        {\n" +
            "      \"@URI\": \"http://dbpedia.org/resource/United_States_presidential_election,_2008\",\n" +
            "      \"@support\": \"1334\",\n" +
            "      \"@types\": \"DBpedia:Election,DBpedia:Event,Schema:Event,Freebase:/government/election,Freebase:/government,Freebase:/book/book_subject,Freebase:/book,Freebase:/time/event,Freebase:/time,DBpedia:TopicalConcept\",\n" +
            "      \"@surfaceForm\": \"President\",\n" +
            "      \"@offset\": \"0\",\n" +
            "      \"@similarityScore\": \"0.2241455465555191\",\n" +
            "      \"@percentageOfSecondRank\": \"-1.0\"\n" +
            "    },\n" +
            "        {\n" +
            "      \"@URI\": \"http://dbpedia.org/resource/Barack_Obama\",\n" +
            "      \"@support\": \"7546\",\n" +
            "      \"@types\": \"DBpedia:OfficeHolder,DBpedia:Person,Schema:Person,Freebase:/business/employer,Freebase:/business,Freebase:/people/person,Freebase:/people,Freebase:/architecture/building_occupant,Freebase:/architecture,Freebase:/event/public_speaker,Freebase:/event,Freebase:/media_common/quotation_subject,Freebase:/media_common,Freebase:/book/book_subject,Freebase:/book,Freebase:/government/political_appointer,Freebase:/government,Freebase:/film/film_subject,Freebase:/film,Freebase:/fictional_universe/person_in_fiction,Freebase:/fictional_universe,Freebase:/internet/social_network_user,Freebase:/internet,Freebase:/tv/tv_actor,Freebase:/tv,Freebase:/government/us_president,Freebase:/tv/tv_character,Freebase:/people/measured_person,Freebase:/award/award_winner,Freebase:/award,Freebase:/book/author,Freebase:/award/ranked_item,Freebase:/music/artist,Freebase:/music,Freebase:/government/politician,Freebase:/people/appointer,Freebase:/film/person_or_entity_appearing_in_film,Freebase:/organization/organization_member,Freebase:/organization,Freebase:/government/u_s_congressperson,Freebase:/broadcast/producer,Freebase:/broadcast,Freebase:/book/book_character,Freebase:/celebrities/celebrity,Freebase:/celebrities,Freebase:/visual_art/art_subject,Freebase:/visual_art,DBpedia:TopicalConcept\",\n" +
            "      \"@surfaceForm\": \"Obama\",\n" +
            "      \"@offset\": \"10\",\n" +
            "      \"@similarityScore\": \"0.2863398790359497\",\n" +
            "      \"@percentageOfSecondRank\": \"-1.0\"\n" +
            "    },\n" +
            "        {\n" +
            "      \"@URI\": \"http://dbpedia.org/resource/American_Recovery_and_Reinvestment_Act_of_2009\",\n" +
            "      \"@support\": \"342\",\n" +
            "      \"@types\": \"\",\n" +
            "      \"@surfaceForm\": \"stimulus\",\n" +
            "      \"@offset\": \"115\",\n" +
            "      \"@similarityScore\": \"0.30584201216697693\",\n" +
            "      \"@percentageOfSecondRank\": \"-1.0\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    private String missingResourcesJsonContent = "{\n" +
            "  \"@text\": \"President+Obama+called+Wednesday+on+Congress+to+extend+a+tax+break+for+students+included+in+last+year%27s+economic+stimulus+package%2C+arguing+that+the+policy+provides+more+generous+assistance\",\n" +
            "  \"@confidence\": \"0.5\",\n" +
            "  \"@support\": \"100\",\n" +
            "  \"@types\": \"\",\n" +
            "  \"@sparql\": \"\",\n" +
            "  \"@policy\": \"whitelist\"\n" +
            "}";

    private static final Logger logger = Logger.getLogger(GsonSerializationTest.class);

    @BeforeClass
    public void setUp()
    {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(DBpediaAPIResponse.class, new DBpediaAPIResponseAdapter())
                .registerTypeAdapter(DBpediaResource.class, new DBpediaResourceAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @AfterClass
    public void teardown()
    {
        this.gson = null;
    }

    @Test
    public void shouldDeserializeNormalJson()
    {
        DBpediaAPIResponse deserializedResponse = gson.fromJson(normalJsonContent,
                                                                DBpediaAPIResponse.class);
        logger.debug(deserializedResponse.toString());

        Assert.assertTrue(deserializedResponse.getResources().size() == 3);
        Assert.assertEquals("President+Obama+called+Wednesday+on+Congress+to+extend+a+tax+break+for+students+included+in+last+year%27s+economic+stimulus+package%2C+arguing+that+the+policy+provides+more+generous+assistance",
                            deserializedResponse.getText());

    }

    @Test
    public void shouldDeserializeMissingJson()
    {
        DBpediaAPIResponse deserializedResponse = gson.fromJson(missingResourcesJsonContent,
                                                                DBpediaAPIResponse.class);
        logger.debug(deserializedResponse.toString());

        Assert.assertTrue(deserializedResponse.getResources().size() == 0);
        Assert.assertEquals("President+Obama+called+Wednesday+on+Congress+to+extend+a+tax+break+for+students+included+in+last+year%27s+economic+stimulus+package%2C+arguing+that+the+policy+provides+more+generous+assistance",
                            deserializedResponse.getText());

    }
}
