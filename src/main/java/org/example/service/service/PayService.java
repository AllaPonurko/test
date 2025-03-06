package org.example.service.service;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.order.Order;
import org.example.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PayService {
    private static final Logger LOGGER = LogManager.getLogger();
    private final OrderRepository orderRepository;

    public PayService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     *
     * @param orderId
     * @return
     */
    @Transactional
    public boolean payProcess(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId)).orElse(null);
        if (order != null && !order.isPayed()) {
            if (simulatePayment()) {
                order.setPayed(true);
                orderRepository.save(order);
                LOGGER.info("Order " + orderId + " was paid successfully (simulated).");
                return true;
            }
            else {
                LOGGER.error("Payment failed for order " + orderId + " (simulated).");
                return false;
            }
        }
        return false;
    }

    /**
     * simulation of pay process
     * @return
     */
    private boolean simulatePayment() {
        double random =0.9+ Math.random();
        return random > 0.9;
    }
}
