package com.collective.activity.storage.model;

import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.activities.model.objects.PersonObject;
import org.joda.time.DateTime;

import static org.apache.abdera2.activities.model.Activity.makeActivity;
import static org.apache.abdera2.activities.model.Verb.MAKE_FRIEND;
import static org.apache.abdera2.activities.model.objects.PersonObject.makePerson;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class DomainFixtures {

    public static Activity getActivity(int i) {
        final IO io = IO.get();
        final PersonObject johnDoe = makePerson("johnDoe-" + i)
                .email("john@doe.com-" + i).get();

        Activity makeFriendActivity = makeActivity()
                .actor(johnDoe)
                .verb(MAKE_FRIEND)
                .object(makePerson("alex french-" + i).email("alex@french.com-" + i).get())
                .published(new DateTime())
                .id("id-35236-" + i)
                .get();
        makeFriendActivity.writeTo(io, System.out);
        return makeFriendActivity;
    }
}
