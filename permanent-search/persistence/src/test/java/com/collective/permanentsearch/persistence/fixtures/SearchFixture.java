package com.collective.permanentsearch.persistence.fixtures;

import com.collective.permanentsearch.model.LabelledURI;
import com.collective.permanentsearch.model.Search;
import org.joda.time.DateTime;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class SearchFixture {

    public static Search getSearch(DateTime dateTime) {

        Search search = new Search();
        LabelledURI commonURI = new LabelledURI();
        LabelledURI customURI = new LabelledURI();
        commonURI.setLabel("common URI fake label");
        try {
            commonURI.setUrl(new URI("http://common-fakeURI.com"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        customURI.setLabel("custom URI fake label");
        try {
            customURI.setUrl(new URI("http://custom-fakeURI.com"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        search.addCommonUri(commonURI);
        search.addCustomUri(customURI);

        search.setLastProfilationDate(dateTime);
        search.setTitle("fake search - created on " + search.getLastProfilationDate().toString());
        return search;

    }
}
