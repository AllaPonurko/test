package org.example.service.log;

import jakarta.transaction.Transactional;
import org.example.entity.log.LogOfChanges;
import org.example.repository.LogOfChangesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LogOfChangesService<T> {
    @Autowired
    private final LogOfChangesRepository logOfChangesRepository;

    public LogOfChangesService(LogOfChangesRepository logOfChangesRepository) {
        this.logOfChangesRepository = logOfChangesRepository;
    }

    @Transactional
    public LogOfChanges writeLog(T object, String reason) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (object != null) {
            Method getIdMethod = object.getClass().getMethod("getId");
            UUID entityId = (UUID) getIdMethod.invoke(object);
            LogOfChanges logOfChanges = new LogOfChanges();
            logOfChanges.setName(object.getClass().getSimpleName());
            logOfChanges.setTimeOfChange(LocalDateTime.now());
            logOfChanges.setEntityId(entityId);
            logOfChanges.setReason(reason);
            logOfChangesRepository.save(logOfChanges);
            return logOfChanges;
        }
        return null;
    }
}
