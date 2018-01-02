package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.Activity;
import models.Location;
import models.User;

import static models.Fixtures.locations;


public class ActivityTest {

    PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
    User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
    User harsh = new User("harsh","vardhan","harsh@gmail.com","secret");

    @Before
    public void setup() {
        pacemaker.deleteUsers();
        homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
        harsh = pacemaker.createUser(harsh.firstname, harsh.lastname, harsh.email, harsh.password);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreateActivity() {
        Activity activity = new Activity("walk", "shop", 2.5);
        Activity returnedActivity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
        assertEquals(activity.type, returnedActivity.type);
        assertEquals(activity.location, returnedActivity.location);
        assertEquals(activity.distance, returnedActivity.distance, 0.001);
        assertNotNull(returnedActivity.id);
    }

    @Test
    public void testGetActivity() {
        Activity activity = new Activity("run", "fridge", 0.5);
        Activity returnedActivity1 = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
        Activity returnedActivity2 = pacemaker.getActivity(homer.id, returnedActivity1.id);
        assertEquals(returnedActivity1, returnedActivity2);
    }

    @Test
    public void testDeleteActivity() {
        Activity activity = new Activity("sprint", "pub", 4.5);
        Activity returnedActivity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
        assertNotNull(returnedActivity);
        pacemaker.deleteActivities(homer.id);
        returnedActivity = pacemaker.getActivity(homer.id, returnedActivity.id);
        assertNull(returnedActivity);
    }

    @Test
    public void testCreateActivityWithSingleLocation() {
        pacemaker.deleteActivities(homer.id);
        Activity activity = new Activity("walk", "shop", 2.5);
        Location location = new Location(12.0, 33.0);

        Activity returnedActivity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
        pacemaker.addLocation(homer.id, returnedActivity.id, location.latitude, location.longitude);

        List<Location> locations = pacemaker.getLocations(homer.id, returnedActivity.id);
        assertEquals(locations.size(), 1);
        assertEquals(locations.get(0), location);
    }

    @Test
    public void testCreateActivityWithMultipleLocation() {
        pacemaker.deleteActivities(homer.id);
        Activity activity = new Activity("walk", "shop", 2.5);
        Activity returnedActivity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);

        locations.forEach(location -> pacemaker.addLocation(homer.id, returnedActivity.id, location.latitude, location.longitude));
        List<Location> returnedLocations = pacemaker.getLocations(homer.id, returnedActivity.id);
        assertEquals(locations.size(), returnedLocations.size());
        assertEquals(locations, returnedLocations);
    }

    @Test
    public void testGetActivities() {
        Activity activity1 = new Activity("walk", "shop", 2.5);
        Activity activity2 = new Activity("running", "gym", 3.5);
        pacemaker.createActivity(homer.id, activity1.type, activity1.location, activity1.distance);
        pacemaker.createActivity(homer.id, activity2.type, activity2.location, activity2.distance);
        Collection<Activity> activities = pacemaker.getActivities(homer.id);
        assertEquals(2,activities.size());
    }

    @Test
    public void testFriendActivityReport() {
        pacemaker.follow(homer.id, harsh.email);
        Activity activity1 = new Activity("walk", "shop", 2.5);
        Activity activity2 = new Activity("running", "gym", 3.5);
        pacemaker.createActivity(harsh.id, activity1.type, activity1.location, activity1.distance);
        pacemaker.createActivity(harsh.id, activity2.type, activity2.location, activity2.distance);
        Collection<Activity> activities = pacemaker.friendActivityReport(homer.id, harsh.email);
        assertEquals(2,activities.size());
    }

}
