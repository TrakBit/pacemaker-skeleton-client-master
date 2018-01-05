package controllers;

import models.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserMock {

    User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
    User harsh = new User("harsh", "vardhan", "harsh@gmail.com", "secret");

    PacemakerAPI pacemaker = mock(PacemakerAPI.class);

    @Test
    public void mockCreateUser() {

        User returnedHarsh = new User("1","harsh", "vardhan", "harsh@gmail.com", "secret");
        when(pacemaker.createUser(harsh.firstname, harsh.lastname, harsh.email, harsh.password))
                .thenReturn(returnedHarsh);
        assertEquals(harsh,pacemaker.createUser(harsh.firstname, harsh.lastname, harsh.email, harsh.password));
    }


    @Test
    public void mockMessage() {
        homer = new User("1","homer", "simpson", "homer@simpson.com", "secret");
        harsh = new User("2","harsh", "vardhan", "harsh@gmail.com", "secret");
        pacemaker.follow(homer.id, harsh.email);
        String message = "hello";
        harsh.messages = new ArrayList<>();
        harsh.messages.add(message);
        PacemakerAPI pacemakerMock = mock(PacemakerAPI.class);
        when(pacemakerMock.messageFriend(homer.id,harsh.email,message))
                .thenReturn(harsh.messages.get(0));
        assertEquals(message, harsh.messages.get(0));
    }

    @Test
    public void mockListMessage() {
        homer = new User("1","homer", "simpson", "homer@simpson.com", "secret");
        harsh = new User("2","harsh", "vardhan", "harsh@gmail.com", "secret");
        pacemaker.follow(homer.id, harsh.email);
        String message = "hello";
        pacemaker.messageFriend(homer.id, harsh.email, message);
        List<String> messages = new ArrayList<>();
        messages.add(message);
        PacemakerAPI pacemakerMock = mock(PacemakerAPI.class);
        when(pacemakerMock.listMessages(harsh.id))
                .thenReturn(messages);
        assertEquals(1,messages.size());
    }
}
