package org.example.service.service;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.order.Order;
import org.example.entity.order.OrderDetail;
import org.example.repository.OrderDetailRepository;
import org.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class CleanUpOrderService {
    @Autowired
    private final OrderRepository orderRepository;
    private static final Logger LOGGER = LogManager.getLogger();
   public CleanUpOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;

    }
    @Transactional
    @Scheduled(fixedRate =720000)
    public void removeExpiredOrders() {
        LocalDateTime currentTime = LocalDateTime.now().minusHours(3);
        List<Order> orderList = orderRepository.findUnpaidOrdersOlderThan(currentTime);
        if (!orderList.isEmpty()) {
            orderRepository.deleteAll(orderList);
            LOGGER.info("Orders with time of creating older {}",currentTime+" are deleted successful.");
        }
    }
}
