package models;

import java.io.Serializable;

public class Friend implements Serializable {
    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Friend friend1 = (Friend) o;

        return getFriend() != null ? getFriend().equals(friend1.getFriend()) : friend1.getFriend() == null;
    }

    @Override
    public int hashCode() {
        return getFriend() != null ? getFriend().hashCode() : 0;
    }

    public String toString() {
        return friend.firstname + friend.lastname;
    }

    public User friend;

    public Friend() {
    }


}
