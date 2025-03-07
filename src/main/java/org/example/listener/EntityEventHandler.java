package org.example.listener;

import org.example.entity.log.LogOfChanges;
import org.example.enums.EntityChangesType;
import org.example.enums.ReasonOfChanges;
import org.example.event.EntityChangedEvent;
import org.example.repository.LogOfChangesRepository;
import org.example.service.log.LogOfChangesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class EntityEventHandler {
    @Autowired
    private LogOfChangesService logOfChangesService;
    @Autowired
    private LogOfChangesRepository logOfChangesRepository;

    @EventListener
    public void handleEntityChangedEvent(EntityChangedEvent event) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Object entity = event.getSource();
        String reason = event.getReason();
        LogOfChanges logOfChanges = logOfChangesService.writeLog(entity, reason);
        ReasonOfChanges kind = ReasonOfChanges.valueOf(event.getReason());
        if (kind.equals(ReasonOfChanges.CREATED_BY_ADMIN)||kind.equals(ReasonOfChanges.CREATED_BY_USER)) {
          logOfChanges.setType(EntityChangesType.CREATE.getValue());
        }
        if(kind.equals(ReasonOfChanges.MANUAL_DELETED)||kind.equals(ReasonOfChanges.TIMEOUT_DELETED)){
            logOfChanges.setType(EntityChangesType.DELETE.getValue());
        }
        if(kind.equals(ReasonOfChanges.UPDATED_PAY)){
            logOfChanges.setType(EntityChangesType.UPDATE.getValue());
        }
        logOfChangesRepository.save(logOfChanges);
    }
}
