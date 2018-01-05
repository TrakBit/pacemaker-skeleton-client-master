package controllers;

import models.Friend;
import models.User;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FriendMock {

    @Test
    public void mockCreateFriend() {
        PacemakerAPI pacemaker = mock(PacemakerAPI.class);
        User harsh = new User("1","harsh", "vardhan", "harsh@gmail.com", "secret");
        User homer = new User("2","homer", "simpson", "homer@simpson.com", "secret");
        Friend friend = new Friend();
        friend.friend = harsh;
        homer.friends = new ArrayList<>();
        homer.friends.add(friend);
        when(pacemaker.follow(homer.id, harsh.email)).thenReturn(friend);
        assertEquals(friend,pacemaker.follow(homer.id, harsh.email));
    }
}
