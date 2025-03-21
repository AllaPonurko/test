package org.example.service.service;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.order.Order;
import org.example.enums.OrderStatusEnum;
import org.example.enums.TypeOfChangesEnum;
import org.example.event.EntityChangedEvent;
import org.example.event.OrderEvent;
import org.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class CleanUpOrderService {
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final ApplicationEventPublisher eventPublisher;
    @Autowired
    private final StockItemService stockItemService;
    @Value("${cleanUpOrderService.time.of.expired}")
    private long TimeOfExpired;
    private static final Logger LOGGER = LogManager.getLogger();

    public CleanUpOrderService(OrderRepository orderRepository, ApplicationEventPublisher eventPublisher, StockItemService stockItemService) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
        this.stockItemService = stockItemService;
    }

    @Transactional
    @Scheduled(fixedRate = 7200000)
    public void removeExpiredOrders() {
        LocalDateTime currentTime = LocalDateTime.now().minusMinutes(TimeOfExpired);
        List<Order> orderList = orderRepository.findUnpaidOrdersOlderThan(currentTime);
        if (!orderList.isEmpty()) {
            orderList.forEach(order -> {
                stockItemService.withdrawProductAfterPayOrDelete(order, OrderStatusEnum.CANCELLED.getValue());
                eventPublisher.publishEvent(new OrderEvent(order, OrderStatusEnum.CANCELLED.getValue()));
            });
            orderRepository.deleteAll(orderList);
            LOGGER.info("Orders with time of creating older {}", currentTime + " are deleted successful.");
        }
    }
}
