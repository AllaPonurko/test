package org.example.listener;

import org.example.event.OrderEvent;
import org.example.service.log.LogOrderChangedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class OrderEventHandler {
    @Autowired
    private LogOrderChangedService logOrderChangedService;
    @EventListener
    public void handleLogOrderChangedEvent(OrderEvent event) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Object entity = event.getSource();
        String type = event.getValue();
        logOrderChangedService.writeLog(entity,type);
    }

}
