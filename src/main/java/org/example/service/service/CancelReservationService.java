package org.example.service.service;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.order.Order;
import org.example.entity.warehouse.StockItem;
import org.example.enums.TypeOfActionWithStockItemEnum;
import org.example.event.StockItemChangedEvent;
import org.example.repository.OrderRepository;
import org.example.repository.StockItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CancelReservationService {
    @Autowired
    private final StockItemRepository stockItemRepository;
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final ApplicationEventPublisher eventPublisher;
    private static final Logger LOGGER = LogManager.getLogger();
    public CancelReservationService(StockItemRepository stockItemRepository, OrderRepository orderRepository, ApplicationEventPublisher eventPublisher) {
        this.stockItemRepository = stockItemRepository;
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Scheduled(fixedRate = 3000000)
    public void cancelReservation() {
        var listStockItems=getStockItemsIsReserved();
        if (getOrdersWithoutPay().isEmpty() && !listStockItems.isEmpty()){
            listStockItems.forEach(stockItem -> {
                stockItem.setReserved(0);
                stockItem.setIsReserved(false);
                stockItemRepository.save(stockItem);
                LOGGER.info("Reservation for stockItem with Id {} was canceled.", stockItem.getId());
                eventPublisher.publishEvent(new StockItemChangedEvent(stockItem,
                        TypeOfActionWithStockItemEnum.RESERVATION_CANCELLATION.getValue()));
            });
        }
    }

    private List<Order> getOrdersWithoutPay() {
        return orderRepository.findAllByIsPayedFalse();
    }

    private List<StockItem> getStockItemsIsReserved() {
        return stockItemRepository.findAllIsReserved();
    }
}
