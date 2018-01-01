package controllers;

import static org.junit.Assert.assertEquals;
import models.Friend;
import models.User;
import org.junit.Before;
import org.junit.Test;

public class FriendTest {

    PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
    User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
    User harsh = new User("harsh","vardhan","harsh@gmail.com","secret");

    @Before
    public void setup() {
        pacemaker.deleteUsers();
        homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
        harsh = pacemaker.createUser(harsh.firstname, harsh.lastname, harsh.email, harsh.password);
    }

    @Test
    public void testCreateFriend() {
        Friend returnedFriend = pacemaker.follow(homer.id, harsh.email);
        assertEquals(returnedFriend.friend, harsh);
    }
}
