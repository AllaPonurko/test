package org.example.service.service;

import jakarta.transaction.Transactional;
import org.example.entity.log.LogOrderChanged;
import org.example.entity.order.Order;
import org.example.enums.OrderStatusEnum;
import org.example.enums.TypeOfChangesEnum;
import org.example.repository.LogOrderChangedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class LogOrderChangedService {
    @Autowired
    private final LogOrderChangedRepository orderWarehouseRepository;

    public LogOrderChangedService(LogOrderChangedRepository orderWarehouseRepository) {
        this.orderWarehouseRepository = orderWarehouseRepository;
    }

    @Transactional
    public void writeLog(Object object, String type) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        LogOrderChanged logOrderChanged = createLogOrderChanged(object);
        if (type == OrderStatusEnum.PENDING.getValue()) {
            logOrderChanged.setOrderStatus(OrderStatusEnum.PENDING);
            logOrderChanged.setTypeOfChanges(TypeOfChangesEnum.CREATED_BY_USER);
        }
        if (type == OrderStatusEnum.PAYED.getValue()) {
            logOrderChanged.setOrderStatus(OrderStatusEnum.PAYED);
            logOrderChanged.setTypeOfChanges(TypeOfChangesEnum.UPDATED_PAYED);
            logOrderChanged.setTimeOfChanges(LocalDateTime.now());
        }
        if (type == OrderStatusEnum.CANCELLED.getValue()) {
            logOrderChanged.setOrderStatus(OrderStatusEnum.CANCELLED);
            logOrderChanged.setTypeOfChanges(TypeOfChangesEnum.TIMEOUT_DELETED);
            logOrderChanged.setTimeOfChanges(LocalDateTime.now());
        }
        orderWarehouseRepository.save(logOrderChanged);
    }

    private LogOrderChanged createLogOrderChanged(Object object) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (object != null) {
            LogOrderChanged logOrderChanged = new LogOrderChanged();
            Method getOrder = object.getClass().getMethod("getOrder");
            Order order = (Order) getOrder.invoke(object);
            logOrderChanged.setOrder(order);
            Method getTotalPrice = order.getClass().getMethod("getTotalPrice");
            logOrderChanged.setTotalPrice((BigDecimal) getTotalPrice.invoke(order)) ;
            return orderWarehouseRepository.save(logOrderChanged);
        }
        return null;
    }
}
