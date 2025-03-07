package org.example.service.service;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.order.Order;
import org.example.entity.order.OrderDetail;
import org.example.enums.ReasonOfChanges;
import org.example.event.EntityChangedEvent;
import org.example.repository.OrderDetailRepository;
import org.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class CleanUpOrderService {
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final ApplicationEventPublisher eventPublisher;
    private static final Logger LOGGER = LogManager.getLogger();

    public CleanUpOrderService(OrderRepository orderRepository, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Scheduled(fixedRate = 3600000)
    public void removeExpiredOrders() {
        LocalDateTime currentTime = LocalDateTime.now().minusHours(3);
        List<Order> orderList = orderRepository.findUnpaidOrdersOlderThan(currentTime);
        if (!orderList.isEmpty()) {
            orderList.forEach(order -> eventPublisher.publishEvent(new EntityChangedEvent(order, ReasonOfChanges.TIMEOUT_DELETED.getValue())));
            orderRepository.deleteAll(orderList);
            LOGGER.info("Orders with time of creating older {}", currentTime + " are deleted successful.");
        }
    }
}
