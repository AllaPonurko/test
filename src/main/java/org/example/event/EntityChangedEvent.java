package org.example.event;

import org.springframework.context.ApplicationEvent;

public class EntityChangedEvent extends ApplicationEvent {
    private final String reason;
    public EntityChangedEvent(Object source, String reason) {
        super(source);
        this.reason = reason;
    }
    public String getReason(){
        return reason;
    }
}
