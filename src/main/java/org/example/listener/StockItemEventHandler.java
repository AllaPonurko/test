package org.example.listener;

import org.example.entity.log.LogStockItemChanged;
import org.example.event.StockItemChangedEvent;
import org.example.repository.LogStockItemChangedRepository;
import org.example.service.log.LogStockItemChangedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class StockItemEventHandler {
    @Autowired
    private LogStockItemChangedService logStockItemChangedService;
    @Autowired
    private LogStockItemChangedRepository logStockItemChangedRepository;
    @EventListener
    public void handleStockItemChangedEvent(StockItemChangedEvent event) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Object entity = event.getSource();
        String type = event.getType();
        logStockItemChangedService.writeLog(entity,type);
    }
}
