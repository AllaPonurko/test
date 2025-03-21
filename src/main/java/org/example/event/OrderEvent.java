package org.example.event;

import org.example.entity.order.OrderWarehouse;
import org.springframework.context.ApplicationEvent;

public class OrderEvent extends ApplicationEvent {
    private final String value;
    public OrderEvent(Object object, String value) {
        super(object);
        this.value=value;
    }
    public String getValue() {
        return value;
    }
}
