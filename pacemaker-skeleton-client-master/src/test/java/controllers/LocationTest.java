package controllers;

import static org.junit.Assert.*;
import models.Activity;
import models.Location;
import models.User;
import org.junit.Before;
import org.junit.Test;


public class LocationTest {

    PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
    User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
    Activity activity = new Activity("walk", "shop", 2.5);

    @Before
    public void setup() {
        pacemaker.deleteUsers();
        homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
        activity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
    }

    @Test
    public void testCreateLocation() {
        Location location = new Location(234.34, 23.5423);
        Location returnedLocation = pacemaker.addLocation(homer.id, activity.id, location.latitude, location.longitude);
        assertEquals(location, returnedLocation);
    }

    @Test
    public void mockCreateLocation() {

    }
}
