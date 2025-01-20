package org.example.responses;

import org.example.models.User;

public class UserResponse extends BaseResponse<User> {

    public UserResponse(String message, User entity) {
        super(message, entity);
    }
}
