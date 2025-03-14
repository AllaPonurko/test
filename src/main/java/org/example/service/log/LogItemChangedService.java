package org.example.service.log;

import jakarta.transaction.Transactional;
import org.example.entity.log.LogItemChanged;
import org.example.repository.LogOfChangesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class LogItemChangedService<T> {
    @Autowired
    private final LogOfChangesRepository logOfChangesRepository;

    public LogItemChangedService(LogOfChangesRepository logOfChangesRepository) {
        this.logOfChangesRepository = logOfChangesRepository;
    }

    @Transactional
    public LogItemChanged writeLog(T object, String reason) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (object != null) {
            if (object instanceof Optional<?>) {
                Optional<?> optionalObject = (Optional<?>) object;
                if (optionalObject.isPresent()) {
                    object = (T) optionalObject.get();  // Розпаковуємо об'єкт
                }
            }
                Method getIdMethod = object.getClass().getMethod("getId");
                UUID entityId = (UUID) getIdMethod.invoke(object);
                LogItemChanged logItemChanged = new LogItemChanged();
                logItemChanged.setName(object.getClass().getSimpleName());
                logItemChanged.setTimeOfChange(LocalDateTime.now());
                logItemChanged.setEntityId(entityId);
                logItemChanged.setReason(reason);
                logOfChangesRepository.save(logItemChanged);
                return logItemChanged;
        }
        return null;
    }
}
