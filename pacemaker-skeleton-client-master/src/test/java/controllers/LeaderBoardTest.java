package controllers;

import static org.junit.Assert.assertEquals;
import models.Activity;
import models.LeaderBoard;
import models.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class LeaderBoardTest {

    PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
    User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
    User harsh = new User("harsh","vardhan","harsh@gmail.com","secret");
    Activity activity1 = new Activity("walk", "shop", 2.5);
    Activity activity2 = new Activity("running", "shop", 3.5);
    Activity activity3 = new Activity("running", "gym", 3.5);

    @Before
    public void setup() {
        pacemaker.deleteUsers();
        homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
        harsh = pacemaker.createUser(harsh.firstname, harsh.lastname, harsh.email, harsh.password);
        pacemaker.createActivity(harsh.id, activity1.type, activity1.location, activity1.distance);
        pacemaker.createActivity(harsh.id, activity2.type, activity2.location, activity2.distance);
        pacemaker.createActivity(homer.id, activity3.type, activity3.location, activity3.distance);
    }

    @Test
    public void testDistanceLeaderBoard() {
        List<LeaderBoard> leaderBoard = pacemaker.distanceLeaderBoard();
        assertEquals(harsh.email,leaderBoard.get(0).email);
    }

    @Test
    public void testDistanceLeaderBoardByType() {
        List<LeaderBoard> leaderBoard = pacemaker.distanceLeaderBoardByType(activity1.type);
        assertEquals(1,leaderBoard.size());
    }

    @Test
    public void testLocationLeaderBoard() {
        List<LeaderBoard> leaderBoard = pacemaker.locationLeaderBoard(activity1.location);
        assertEquals(2, leaderBoard.size());
    }

}
