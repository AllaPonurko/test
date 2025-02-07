package org.example.event;

import org.example.entity.user.User;
import org.springframework.context.ApplicationEvent;

public class UserCreateEvent extends ApplicationEvent {
    private  final User user;
    public UserCreateEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
