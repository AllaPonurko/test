package org.example.event;

import org.springframework.context.ApplicationEvent;

public class StockItemChangedEvent extends ApplicationEvent {
    private final String type;
    public StockItemChangedEvent(Object source, String type) {
        super(source);
        this.type = type;
    }
    public String getType() {
        return type;
    }

}
