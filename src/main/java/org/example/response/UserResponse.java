package org.example.response;

import org.example.entity.user.User;

public class UserResponse extends BaseResponse<User> {

    public UserResponse(String message, User entity) {
        super(message, entity);
    }
}
