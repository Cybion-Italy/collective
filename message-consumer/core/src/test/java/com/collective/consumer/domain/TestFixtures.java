package com.collective.consumer.domain;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class TestFixtures {

    public static final String jsonMessage = "{" +
            "\"action\":\"update\"," +
            "\"type\":\"user\"," +
            "\"object\":" +
                "{" +
                "   \"id\":\"20\"," +
                "   \"interests\":\"marketing, information retrieval, data mining\"," +
                "   \"skills\":\"java, programming, semantic web, folksonomies, machine learning, artificial intelligence, search\"" +
                "}"+
            "}";

    String oldObject = "{" +
            "\"id\":\"1\"," +
            "\"skills\":\"skills1, skills2\"," +
            "\"interests\":\"interest1, interest2\"," +
            "\"education\":[" +
            "{" +
            "\"school\":\"NTUA\"," +
            "\"country\":\"Greece\"," +
            "\"degree\":\"Master\"," +
            "\"fields\":\"Fields\"," +
            "\"activities\":\"activities\"," +
            "\"notes\":\"notes\"," +
            "\"period\":{" +
            "\"start\":{" +
            "\"year\":\"1937\"" +
            "}," +
            "\"end\":{" +
            "\"year\":\"1994\"" +
            "}" +
            "}" +
            "}]," +
            "\"experience\":[" +
            "{" +
            "\"company\":\"Company name\"," +
            "\"is_current\":\"0\"," +
            "\"title\":\"Software Engineer\"," +
            "\"description\":\"description\"," +
            "\"period\":{" +
            "\"start\":{" +
            "\"month\":\"11\"," +
            "\"year\":\"1932\"" +
            "}," +
            "\"end\":{" +
            "\"month\":\"0\"," +
            "\"year\":\"0\"" +
            "}" +
            "}" +
            "}]" +
            "}";

    public static final String jsonUser = "{" +
            "\"id\":\"20\"," +
            "\"interests\":\"marketing, information retrieval, data mining\"," +
            "\"skills\":\"java, programming, semantic web, folksonomies, machine learning, artificial intelligence, search\"" +
            "}";

    public static final String jsonProject = "{\n" +
            "   \"id\":18,\n" +
            "   \"name\":\"another test project\",\n" +
            "   \"goals\":\"some, comma, separated, goals \",\n" +
            "   \"url\":\"\",\n" +
            "   \"description\":\"this is my project description\",\n" +
            "   \"members\":[\n" +
            "      \"20\"\n" +
            "   ]\n" +
            "}";
}
