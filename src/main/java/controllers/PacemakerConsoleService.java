package controllers;

import com.google.common.base.Optional;
import asg.cliche.Command;
import asg.cliche.Param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.*;
import parsers.AsciiTableParser;
import parsers.Parser;

public class PacemakerConsoleService {

    private PacemakerAPI paceApi = new PacemakerAPI("http://localhost:7000");
    private Parser console = new AsciiTableParser();
    private User loggedInUser = null;

    public PacemakerConsoleService() {
    }

    // Starter Commands

    @Command(name = "ru",
             description = "Register: Create an account for a new user")
    public void register(@Param(name = "first name") String firstName,
                         @Param(name = "last name") String lastName,
                         @Param(name = "email") String email,
                         @Param(name = "password") String password) {
        console.renderUser(paceApi.createUser(firstName, lastName, email, password));
    }

    @Command(name = "gu",
             description = "List Users: List all users emails, first and last names")
    public void listUsers() {
        console.renderUsers(paceApi.getUsers());
    }

    @Command(name = "lu",
             description = "Login: Log in a registered user in to pacemaker")
    public void login(@Param(name = "email") String email,
                      @Param(name = "password") String password) {
        Optional<User> user = Optional.fromNullable(paceApi.getUserByEmail(email));
        if (user.isPresent()) {
            if (user.get().password.equals(password)) {
                loggedInUser = user.get();
                console.println("Logged in " + loggedInUser.email);
                console.println("ok");
            } else {
                console.println("Error on login");
            }
        }
    }

    @Command(name = "l",
             description = "Logout: Logout current user")
    public void logout() {
        console.println("Logging out " + loggedInUser.email);
        console.println("ok");
        loggedInUser = null;
    }

    @Command(name = "aa",
             description = "Add activity: create and add an activity for the logged in user")
    public void addActivity(@Param(name = "type") String type,
                            @Param(name = "location") String location,
                            @Param(name = "distance") double distance) {
        Optional<User> user = Optional.fromNullable(loggedInUser);
        if (user.isPresent()) {
            console.renderActivity(paceApi.createActivity(user.get().id, type, location, distance));
        }
    }

    @Command(name = "la",
             description = "List Activities: List all activities for logged in user")
    public void listActivities() {
        Optional<User> user = Optional.fromNullable(loggedInUser);
        if (user.isPresent()) {
            console.renderActivities(paceApi.getActivities(user.get().id));
        }
    }

    // Baseline Commands

    @Command(name = "al",
             description = "Add location: Append location to an activity")
    public void addLocation(@Param(name = "activity-id") String id,
                            @Param(name = "longitude") double longitude,
                            @Param(name = "latitude") double latitude) {
        Optional<Activity> activity = Optional.fromNullable(paceApi.getActivity(loggedInUser.getId(), id));
        if (activity.isPresent()) {
            paceApi.addLocation(loggedInUser.getId(), activity.get().id, latitude, longitude);
            console.println("ok");
        } else {
            console.println("not found");
        }
    }

    @Command(name = "ar",
             description = "ActivityReport: List all activities for logged in user," +
                    " sorted alphabetically by type")
    public void activityReport() {
        Optional<User> user = Optional.fromNullable(loggedInUser);
        if (user.isPresent()) {
            console.renderActivities(paceApi.listActivities(user.get().id, "type"));
        }
    }

    @Command(
            name = "ar",
            description = "Activity Report: List all activities for logged in user by type. " +
                    "Sorted longest to shortest distance")
    public void activityReport(@Param(name = "byType: type") String type) {
        Optional<User> user = Optional.fromNullable(loggedInUser);
        if (user.isPresent()) {
            List<Activity> reportActivities = new ArrayList<>();
            Collection<Activity> usersActivities = paceApi.getActivities(user.get().id);
            usersActivities.forEach(a -> {
                if (a.type.equals(type))
                    reportActivities.add(a);
            });
            reportActivities.sort((a1, a2) -> {
                if (a1.distance >= a2.distance)
                    return -1;
                else
                    return 1;
            });
            console.renderActivities(reportActivities);
        }
    }

    @Command(name = "lal",
             description = "List all locations for a specific activity")
    public void listActivityLocations(@Param(name = "activity-id") String id) {
        Optional<Activity> activity = Optional.fromNullable(paceApi.getActivity(loggedInUser.getId(), id));
        if (activity.isPresent()) {
            List<Location> locations = paceApi.getLocations(loggedInUser.getId(), id);
            console.renderLocations(locations);
        }
    }

    @Command(name = "f", description = "Follow Friend: Follow a specific friend")
    public void follow(@Param(name = "email") String email) {
        paceApi.follow(loggedInUser.id, email);
    }

    @Command(name = "lf", description = "List Friends: List all of the friends of the logged in user")
    public void listFriends() {
        List<Friend> friends = paceApi.listFriends(loggedInUser.id);
        console.renderFriends(friends);
    }

    @Command(name = "far",
             description = "Friend Activity Report: List all activities of specific friend," +
                    " sorted alphabetically by type)")
    public void friendActivityReport(@Param(name = "email") String email) {
        List<Activity> activities = paceApi.friendActivityReport(loggedInUser.id, email);
        console.renderActivities(activities);
    }

    // Good Commands

    @Command(name="uf",
             description = "Unfollow Friends: Stop following a friend")
    public void unfollowFriend(@Param(name="email") String email) {
        paceApi.unfollow(loggedInUser.id, email);
    }

    @Command(name = "mf",
            description = "Message Friend: send a message to a friend")
    public void messageFriend(@Param(name = "email") String email,
                              @Param(name = "message") String message) {
        paceApi.messageFriend(loggedInUser.id, email, message);
    }

    @Command(name = "lm",
            description = "List Messages: List all messages for the logged in user")
    public void listMessages() {
        List<String> messages = paceApi.listMessages(loggedInUser.id);
        console.renderMessages(messages);
    }

    @Command(name = "dlb",
            description = "Distance Leader Board: list summary distances of all friends, sorted longest to shortest")
    public void distanceLeaderBoard() {
        console.renderLeaderBoard(paceApi.distanceLeaderBoard());
    }

    // Excellent Commands

    @Command(name="dlbbt",
             description = "Distance Leader Board: distance leader board refined by type")
    public void distanceLeaderBoardByType(@Param(name = "byType: type") String type) {
        console.renderLeaderBoard(paceApi.distanceLeaderBoardByType(type));
    }

    @Command(name = "maf",
             description = "Message All Friends: send a message to all friends")
    public void messageAllFriends(@Param(name = "message") String message) {
        paceApi.messageAllFriends(loggedInUser.id, message);
    }

    @Command(name = "llb",
             description = "Location Leader Board: list sorted summary distances of all friends in named location")
    public void locationLeaderBoard(@Param(name = "location") String location) {
        console.renderLeaderBoard(paceApi.locationLeaderBoard(location));
    }

    // Outstanding Commands

    // Todo
}
