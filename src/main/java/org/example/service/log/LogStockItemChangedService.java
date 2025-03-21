package org.example.service.log;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.log.LogStockItemChanged;
import org.example.repository.LogStockItemChangedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

@Service
public class LogStockItemChangedService {
    @Autowired
    private final LogStockItemChangedRepository logStockItemChangedRepository;
    private static final Logger LOGGER = LogManager.getLogger();

    public LogStockItemChangedService(LogStockItemChangedRepository logStockItemChangedRepository) {
        this.logStockItemChangedRepository = logStockItemChangedRepository;
    }

    @Transactional
    public void writeLog(Object object, String type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (object != null) {
            LogStockItemChanged logStockItemChanged = new LogStockItemChanged();
            Method getIdMethod = object.getClass().getMethod("getId");
            long stockItemId = (long) getIdMethod.invoke(object);
            logStockItemChanged.setStockItemId(stockItemId);
            Method getProduct = object.getClass().getMethod("getProduct");
            Object product = getProduct.invoke(object);
            Method getUUIDMethod = product.getClass().getMethod("getId");
            UUID productId = (UUID) getUUIDMethod.invoke(product);
            logStockItemChanged.setProductId(productId);

            logStockItemChanged.setType(type);
            logStockItemChangedRepository.save(logStockItemChanged);
        }

    }
}
