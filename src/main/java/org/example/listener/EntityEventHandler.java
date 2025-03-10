package org.example.listener;

import org.example.entity.log.LogItemChanged;
import org.example.enums.EntityChangesTypeEnum;
import org.example.enums.ReasonOfChangesEnum;
import org.example.event.EntityChangedEvent;
import org.example.repository.LogOfChangesRepository;
import org.example.service.log.LogItemChangedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class EntityEventHandler {
    @Autowired
    private LogItemChangedService logItemChangedService;
    @Autowired
    private LogOfChangesRepository logOfChangesRepository;

    @EventListener
    public void handleEntityChangedEvent(EntityChangedEvent event) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Object entity = event.getSource();
        String reason = event.getReason();
        LogItemChanged logItemChanged = logItemChangedService.writeLog(entity, reason);
        ReasonOfChangesEnum kind = ReasonOfChangesEnum.valueOf(event.getReason());
        if (kind.equals(ReasonOfChangesEnum.CREATED_BY_ADMIN)||kind.equals(ReasonOfChangesEnum.CREATED_BY_USER)) {
          logItemChanged.setType(EntityChangesTypeEnum.CREATE.getValue());
        }
        if(kind.equals(ReasonOfChangesEnum.MANUAL_DELETED)||kind.equals(ReasonOfChangesEnum.TIMEOUT_DELETED)){
            logItemChanged.setType(EntityChangesTypeEnum.DELETE.getValue());
        }
        if(kind.equals(ReasonOfChangesEnum.UPDATED_PAY)){
            logItemChanged.setType(EntityChangesTypeEnum.UPDATE.getValue());
        }
        logOfChangesRepository.save(logItemChanged);
    }
}
