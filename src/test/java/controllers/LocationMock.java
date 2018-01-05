package controllers;

import models.Activity;
import models.Location;
import models.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocationMock {

    @Test
    public void mockCreateLocation() {
        User homer = new User("1", "homer", "simpson", "homer@simpson.com", "secret");
        Activity activity = new Activity("2", "walk", "shop", 2.5);
        Location location = new Location("1", 23.34, 234.325);
        PacemakerAPI pacemakerMock = mock(PacemakerAPI.class);
        when(pacemakerMock.addLocation(homer.id, activity.id, location.latitude, location.longitude))
                .thenReturn(location);
        assertEquals("1",location.id);
    }
}
