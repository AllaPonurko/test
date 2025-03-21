package org.example.event;

import org.springframework.context.ApplicationEvent;

public class EmailSendEvent extends ApplicationEvent {
    private final String type;
    private final String status;

    public EmailSendEvent(Object source, String status, String type) {
        super(source);
        this.status = status;
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }
}
