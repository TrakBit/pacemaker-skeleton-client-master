package controllers;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.User;

import static models.Fixtures.users;

public class UserTest {

    PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
    User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
    User harsh = new User("harsh","vardhan","harsh@gmail.com","secret");
    User tom = new User("tom","cruise","tom@gmail.com","secret");

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
        assertEquals("hello",msg);
    }

    @Test
    public void testListMessage() {
        homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
        harsh = pacemaker.createUser(harsh.firstname, harsh.lastname, harsh.email, harsh.password);
        pacemaker.follow(homer.id, harsh.email);
        pacemaker.messageFriend(homer.id, harsh.email, "hello");
        List<String> messages = pacemaker.listMessages(harsh.id);
        assertEquals(1,messages.size());
    }


}