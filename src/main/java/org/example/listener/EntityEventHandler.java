package org.example.listener;

import org.example.entity.log.LogItemChanged;
import org.example.enums.EntityChangesTypeEnum;
import org.example.enums.TypeOfChangesEnum;
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
        TypeOfChangesEnum kind = TypeOfChangesEnum.valueOf(event.getReason());
        if (kind.equals(TypeOfChangesEnum.CREATED_BY_ADMIN)||kind.equals(TypeOfChangesEnum.CREATED_BY_USER)) {
          logItemChanged.setType(EntityChangesTypeEnum.CREATE.getValue());
        }
        if(kind.equals(TypeOfChangesEnum.MANUAL_DELETED)||kind.equals(TypeOfChangesEnum.TIMEOUT_DELETED)){
            logItemChanged.setType(EntityChangesTypeEnum.DELETE.getValue());
        }
        if(kind.equals(TypeOfChangesEnum.UPDATED_PAYED)){
            logItemChanged.setType(EntityChangesTypeEnum.UPDATE.getValue());
        }
        logOfChangesRepository.save(logItemChanged);
    }
}
