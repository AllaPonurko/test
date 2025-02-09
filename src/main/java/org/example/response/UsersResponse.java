package org.example.response;

import org.example.entity.user.User;

import java.util.List;

public class UsersResponse {
    private int userCount;
    private List<User> users;

    public UsersResponse(List<User> users) {
        this.userCount = users.size();
        this.users = users;
    }

    public int getUserCount() {
        return userCount;
    }

    public List<User> getUsers() {
        return users;
    }
}
