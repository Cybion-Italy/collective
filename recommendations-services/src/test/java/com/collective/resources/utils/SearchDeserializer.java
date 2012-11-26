package com.collective.resources.utils;

import com.collective.permanentsearch.model.LabelledURI;
import com.collective.permanentsearch.model.Search;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class SearchDeserializer implements JsonDeserializer<Search> {

    public Search deserialize(JsonElement jsonElement,
                              Type type,
                              JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

            String id = jsonElement.getAsJsonObject()
                    .get("id").getAsString();
            String title = jsonElement.getAsJsonObject()
                    .get("title").getAsString();

            Search search = new Search();
            search.setId(Long.parseLong(id));
            search.setTitle(title);

            //does not deserialize lastProfilationDate

            JsonArray commonURIsJson =
                    jsonElement.getAsJsonObject()
                            .get("commonUris").getAsJsonArray();

            ArrayList<LabelledURI> commonUris = new ArrayList<LabelledURI>();
            for (JsonElement labelledUriJson : commonURIsJson) {

                LabelledURI labelledURI = new LabelledURI();
                labelledURI.setLabel(labelledUriJson.getAsJsonObject()
                        .get("label").getAsString());
                String uriString = "";

                try {
                    uriString = labelledUriJson.getAsJsonObject()
                            .get("url").getAsString();
                    labelledURI.setUrl(new URI(uriString));
                } catch (URISyntaxException e) {
                    final String msg = "error while deserializing " +
                            "search with common uri: " + uriString;
                    throw new JsonParseException(msg, e);
                }
                commonUris.add(labelledURI);
            }
        search.setCommonUris(commonUris);

        JsonArray customURIsJson =
                    jsonElement.getAsJsonObject()
                            .get("customUris").getAsJsonArray();

            ArrayList<LabelledURI> customURIs = new ArrayList<LabelledURI>();
            for (JsonElement customUri : customURIsJson) {

                LabelledURI labelledURI = new LabelledURI();
                labelledURI.setLabel(customUri.getAsJsonObject()
                        .get("label").getAsString());

                String urlAsString = "";
                try {
                     urlAsString = customUri.getAsJsonObject()
                            .get("url").getAsString();
                    labelledURI.setUrl(new URI(urlAsString));
                } catch (URISyntaxException e) {
                    final String msg = "error while deserializing " +
                            "search with custom uri: " + urlAsString;
                    throw new JsonParseException(msg, e);
                }
                customURIs.add(labelledURI);
            }
        search.setCustomUris(customURIs);
        return search;
    }
}
