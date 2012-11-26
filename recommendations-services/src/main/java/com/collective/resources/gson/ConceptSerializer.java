package com.collective.resources.gson;

import com.collective.concepts.core.Concept;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ConceptSerializer implements JsonDeserializer<List<Concept>> {

    public List<Concept> deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonArray jsonConcepts = jsonElement.getAsJsonArray();
        List<Concept> concepts = new ArrayList<Concept>();
        for (JsonElement jsonConcept : jsonConcepts) {
            String company = jsonConcept.getAsJsonObject().get("company").getAsString();
            String owner = jsonConcept.getAsJsonObject().get("owner").getAsString();
            String name = jsonConcept.getAsJsonObject().get("name").getAsString();
            String visibility = jsonConcept.getAsJsonObject().get("visibility").getAsString();
            JsonArray keywords = jsonConcept.getAsJsonObject().get("keywords").getAsJsonArray();
            String label = jsonConcept.getAsJsonObject().get("label").getAsString();
            String description = jsonConcept.getAsJsonObject().get("description").getAsString();
            Concept concept = new Concept(company, owner, name, label);
            concept.setDescription(description);
            concept.setVisibility(Concept.Visibility.valueOf(visibility));
            for (JsonElement keyword : keywords) {
                concept.addKeyword(keyword.getAsString());
            }
            concepts.add(concept);
        }
        return concepts;
    }
}
