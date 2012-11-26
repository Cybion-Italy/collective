package com.collective.recommender.utils;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.model.profile.ProjectProfile;
import com.collective.model.profile.UserProfile;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PersistenceDomainFixtures {

    public static List<WebResourceEnhanced> getResources()
            throws MalformedURLException, URISyntaxException {
        List<WebResourceEnhanced> resources = new ArrayList<WebResourceEnhanced>();
        WebResourceEnhanced r1 = new WebResourceEnhanced();
        r1.setId(new Integer(4635));
        r1.setTitolo("Just a test resource");
        r1.setDescrizione("Hi there, this is just a test article");
        r1.setUrl(new URL("http://davidepalmisano.com/hi-there.html"));
        r1.addTopic(new URI("http://dbpedia.org/resource/Semantic_Web"));
        r1.addTopic(new URI("http://dbpedia.org/resource/Web_of_data"));
        resources.add(r1);
        WebResourceEnhanced r2 = new WebResourceEnhanced();
        r2.setId(new Integer(15254));
        r2.setTitolo("Just another test resource");
        r2.setDescrizione("Hi there, in this document we talk about nothing. As usual.");
        r2.setUrl(new URL("http://davidepalmisano.com/about-nothing.html"));
        r2.addTopic(new URI("http://dbpedia.org/resource/London"));
        r2.addTopic(new URI("http://dbpedia.org/resource/Technology"));
        resources.add(r2);
        return resources;
    }

    public static List<ProjectProfile> getProjects()
            throws MalformedURLException, URISyntaxException {
        List<ProjectProfile> projects = new ArrayList<ProjectProfile>();
        ProjectProfile pp1 = new ProjectProfile();
        pp1.setId(Long.parseLong("32523623"));
        pp1.addManifestoConcept(new URI("http://dbpedia.org/resource/London"));
        pp1.addManifestoConcept(new URI("http://dbpedia.org/resource/Rome"));
        projects.add(pp1);

        ProjectProfile pp2 = new ProjectProfile();
        pp2.setId(Long.parseLong("32523624233"));
        pp2.addManifestoConcept(new URI("http://dbpedia.org/resource/Paris"));
        pp2.addManifestoConcept(new URI("http://dbpedia.org/resource/Tokio"));
        projects.add(pp2);
        return projects;
    }

    public static List<UserProfile> getUserProfiles()
            throws URISyntaxException {
        List<UserProfile> userProfiles = new ArrayList<UserProfile>();
        UserProfile pp1 = new UserProfile();
        pp1.setId(Long.parseLong("32523623"));
        pp1.addSkill(new URI("http://dbpedia.org/resource/London"));
        pp1.addInterest(new URI("http://dbpedia.org/resource/Rome"));
        userProfiles.add(pp1);

        UserProfile pp2 = new UserProfile();
        pp2.setId(Long.parseLong("32523624233"));
        pp2.addSkill(new URI("http://dbpedia.org/resource/Paris"));
        pp2.addInterest(new URI("http://dbpedia.org/resource/Tokio"));
        userProfiles.add(pp2);
        return userProfiles;
    }
}