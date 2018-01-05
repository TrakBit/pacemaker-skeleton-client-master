package controllers;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javafx.beans.binding.When;
import models.Friend;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.User;
import org.mockito.stubbing.OngoingStubbing;

import static models.Fixtures.users;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserTest {

    PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
    User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
    User harsh = new User("harsh", "vardhan", "harsh@gmail.com", "secret");

    @Before
    public void setup() {
        pacemaker.deleteUsers();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreateUser() {
        User user = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
        assertEquals(user, homer);
        User user2 = pacemaker.getUserByEmail(homer.email);
        assertEquals(user2, homer);
    }

    @Test
    public void mockCreateUser() {
        PacemakerAPI pacemaker = mock(PacemakerAPI.class);
        User returnedHarsh = new User("1","harsh", "vardhan", "harsh@gmail.com", "secret");
        when(pacemaker.createUser(harsh.firstname, harsh.lastname, harsh.email, harsh.password))
                .thenReturn(returnedHarsh);
        assertEquals(harsh,pacemaker.createUser(harsh.firstname, harsh.lastname, harsh.email, harsh.password));
    }

    @Test
    public void testCreateUsers() {
        users.forEach(
                user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
        Collection<User> returnedUsers = pacemaker.getUsers();
        assertEquals(users.size(), returnedUsers.size());
    }

    @Test
    public void testMessage() {
        homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
        harsh = pacemaker.createUser(harsh.firstname, harsh.lastname, harsh.email, harsh.password);
        pacemaker.follow(homer.id, harsh.email);
        String msg = pacemaker.messageFriend(homer.id, harsh.email, "hello");
        assertEquals("hello", msg);
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
    public void testListMessage() {
        homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
        harsh = pacemaker.createUser(harsh.firstname, harsh.lastname, harsh.email, harsh.password);
        pacemaker.follow(homer.id, harsh.email);
        pacemaker.messageFriend(homer.id, harsh.email, "hello");
        List<String> messages = pacemaker.listMessages(harsh.id);
        assertEquals(1, messages.size());
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