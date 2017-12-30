package controllers;

import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface PacemakerInterface {
    @GET("/users")
    Call<List<User>> getUsers();

    @DELETE("/users")
    Call<String> deleteUsers();

    @DELETE("/users/{id}")
    Call<User> deleteUser(@Path("id") String id);

    @GET("/users/{id}")
    Call<User> getUser(@Path("id") String id);

    @POST("/users")
    Call<User> registerUser(@Body User User);

    @GET("/users/{id}/activities")
    Call<List<Activity>> getActivities(@Path("id") String id);

    @GET("/users/{id}/activityReport")
    Call<List<Activity>> getActivityReport(@Path("id") String id);

    @POST("/users/{id}/follow/{email}")
    Call<List<User>> follow(@Path("id") String id,
                            @Path("email") String email);

    @POST("/users/{id}/activities")
    Call<Activity> addActivity(@Path("id") String id,
                               @Body Activity activity);

    @DELETE("/users/{id}/activities")
    Call<String> deleteActivities(@Path("id") String id);

    @GET("/users/{id}/activities/{activityId}")
    Call<Activity> getActivity(@Path("id") String id,
                               @Path("activityId") String activityId);

    @POST("/users/{id}/activities/{activityId}/locations")
    Call<Location> addLocation(@Path("id") String id,
                               @Path("activityId") String activityId,
                               @Body Location location);

    @GET("/users/{id}/activities/{activityId}/locations")
    Call<List<Location>> getLocations(@Path("id") String id,
                                      @Path("activityId") String activityId);

    @GET("/users/{id}/friends")
    Call<List<Friend>> listFriends(@Path("id") String id);

    @POST("users/{id}/friend/{email}")
    Call<List<String>> messageFriend(@Path("id") String id,
                                     @Path("email") String email,
                                     @Body String message);

    @GET("/users/{id}/friendActivityReport/{email}")
    Call<List<Activity>> friendActivityReport(@Path("id") String id,
                                              @Path("email") String email);

    @GET("/users/{id}/listMessages")
    Call<List<String>> listMessages(@Path("id") String id);
}


public class PacemakerAPI {

    PacemakerInterface pacemakerInterface;

    public PacemakerAPI(String url) {
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        pacemakerInterface = retrofit.create(PacemakerInterface.class);
    }

    public Collection<User> getUsers() {
        Collection<User> users = null;
        try {
            Call<List<User>> call = pacemakerInterface.getUsers();
            Response<List<User>> response = call.execute();
            users = response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public User createUser(String firstName, String lastName, String email, String password) {
        User returnedUser = null;
        try {
            Call<User> call =
                    pacemakerInterface.registerUser(new User(firstName, lastName, email, password));
            Response<User> response = call.execute();
            returnedUser = response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return returnedUser;
    }

    public Activity createActivity(String id, String type, String location, double distance) {
        Activity returnedActivity = null;
        try {
            Call<Activity> call =
                    pacemakerInterface.addActivity(id, new Activity(type, location, distance));
            Response<Activity> response = call.execute();
            returnedActivity = response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return returnedActivity;
    }

    public Collection<Activity> getActivities(String id) {
        Collection<Activity> activities = null;
        try {
            Call<List<Activity>> call = pacemakerInterface.getActivities(id);
            Response<List<Activity>> response = call.execute();
            activities = response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return activities;
    }

    public List<Activity> listActivities(String userId, String sortBy) {
        List<Activity> activities = null;
        try {
            Call<List<Activity>> call = pacemakerInterface.getActivityReport(userId);
            Response<List<Activity>> response = call.execute();
            activities = response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return activities;
    }

    public Activity getActivity(String userId, String activityId) {
        Activity activity = null;
        try {
            Call<Activity> call = pacemakerInterface.getActivity(userId, activityId);
            Response<Activity> response = call.execute();
            activity = response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return activity;
    }

    public void deleteActivities(String id) {
        try {
            Call<String> call = pacemakerInterface.deleteActivities(id);
            call.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addLocation(String id, String activityId, double latitude, double longitude) {
        try {
            Call<Location> call =
                    pacemakerInterface.addLocation(id, activityId, new Location(latitude, longitude));
            call.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Location> getLocations(String id, String activityId) {
        List<Location> locations = null;
        try {
            Call<List<Location>> call = pacemakerInterface.getLocations(id, activityId);
            Response<List<Location>> response = call.execute();
            locations = response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return locations;
    }

    public User getUserByEmail(String email) {
        Collection<User> users = getUsers();
        User foundUser = null;
        for (User user : users) {
            if (user.email.equals(email)) {
                foundUser = user;
            }
        }
        return foundUser;
    }

    public User getUser(String id) {
        User user = null;
        try {
            Call<User> call = pacemakerInterface.getUser(id);
            Response<User> response = call.execute();
            user = response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    public void deleteUsers() {
        try {
            Call<String> call = pacemakerInterface.deleteUsers();
            call.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public User deleteUser(String id) {
        User user = null;
        try {
            Call<User> call = pacemakerInterface.deleteUser(id);
            Response<User> response = call.execute();
            user = response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    public void follow(String id, String email) {
        try {
            Call<List<User>> call = pacemakerInterface.follow(id, email);
            Response<List<User>> response = call.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Friend> listFriends(String id) {
        List<Friend> friends = null;
        try {
            Call<List<Friend>> call = pacemakerInterface.listFriends(id);
            Response<List<Friend>> response = call.execute();
            friends = response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return friends;
    }

    public List<Activity> friendActivityReport(String id, String email) {
        List<Activity> activities = null;
        try {
            Call<List<Activity>> call = pacemakerInterface.friendActivityReport(id, email);
            Response<List<Activity>> response = call.execute();
            activities = response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return activities;
    }

    public List<String> messageFriend(String id, String email, String message) {
        List<String> messages = null;
        try {
            Call<List<String>> call = pacemakerInterface.messageFriend(id, email, message);
            Response<List<String>> response = call.execute();
            messages = response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public List<String> listMessages(String id) {
        List<String> messages = null;
        try {
            Call<List<String>> call = pacemakerInterface.listMessages(id);
            Response<List<String>> response = call.execute();
            messages = response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
