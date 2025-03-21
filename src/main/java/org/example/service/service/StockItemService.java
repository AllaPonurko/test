package org.example.service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.ProductListDTO;
import org.example.dto.StockItemReq;
import org.example.entity.order.Order;
import org.example.entity.order.OrderWarehouse;
import org.example.entity.product.Product;
import org.example.entity.warehouse.StockItem;
import org.example.entity.warehouse.Warehouse;
import org.example.enums.*;
import org.example.event.EmailSendEvent;
import org.example.event.StockItemChangedEvent;
import org.example.repository.OrderWarehouseRepository;
import org.example.repository.ProductRepository;
import org.example.repository.StockItemRepository;
import org.example.repository.WarehouseRepository;
import org.example.service.notification.BookNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class StockItemService {
    @Autowired
    private final StockItemRepository stockItemRepository;
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final WarehouseRepository warehouseRepository;
    @Autowired
    private final BookNotificationService bookNotificationService;
    private static final Logger LOGGER = LogManager.getLogger();
    @Autowired
    private final ApplicationEventPublisher eventPublisher;
    @Autowired
    private final OrderWarehouseRepository orderWarehouseRepository;


    public StockItemService(StockItemRepository stockItemRepository, ProductRepository productRepository, WarehouseRepository warehouseRepository, BookNotificationService bookNotificationService, ApplicationEventPublisher eventPublisher, OrderWarehouseRepository orderWarehouseRepository) {
        this.stockItemRepository = stockItemRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.bookNotificationService = bookNotificationService;
        this.eventPublisher = eventPublisher;
        this.orderWarehouseRepository = orderWarehouseRepository;
    }

    @Transactional
    public StockItem createOrAddStockItem(StockItemReq stockItemReq) {
        if (stockItemReq == null) {
            throw new IllegalArgumentException("StockItemReq cannot be null");
        }
        Warehouse warehouse = warehouseRepository.findWarehouseByLocationNumber(stockItemReq.warehouseLocation())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found for location: " + stockItemReq.warehouseLocation()));
        Product product = productRepository.findById(UUID.fromString(stockItemReq.productId()))
                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + stockItemReq.productId()));
        StockItem stockItem = stockItemRepository.findByProductAndWarehouse(product.getId(), warehouse.getLocationNumber()).orElse(null);
        if (stockItem == null) {
            stockItem = new StockItem();
            stockItem.setProduct(product);
            stockItem.setWarehouse(warehouse);
            stockItem.setQuantity(stockItemReq.quantity());
            stockItem.setTotalValue(stockItem.getTotalValue());
        } else {
            stockItem.setQuantity(stockItem.getQuantity() + stockItemReq.quantity());
            stockItem.setTotalValue(stockItem.getTotalValue().add(stockItem.getNewTotalValue(product.getPrice(), stockItemReq.quantity())));
        }
        StockItem saveStockItem = stockItemRepository.save(stockItem);
        product.setAvailable(true);
        productRepository.save(product);
        eventPublisher.publishEvent(new StockItemChangedEvent(saveStockItem, TypeOfActionWithStockItemEnum.RESTOCK.getValue()));
        bookNotificationService.sendNotificationToUsers(product);
        return saveStockItem;
    }

    @Transactional
    public boolean isProductReserved(StockItemReq stockItemReq) {
        StockItem stockItem = getStockItem(stockItemReq);
        return isSetStockItemQuantityForReserve(stockItem, stockItemReq.quantity());
    }

    private boolean isSetStockItemQuantityForReserve(StockItem stockItem, long quantityForReserved) {
        if (stockItem.getIsReserved()) {
            long freeCount = stockItem.getQuantity() - (stockItem.getReserved() + quantityForReserved);
            if (freeCount >= 0) {
                stockItem.setReserved(stockItem.getReserved() + quantityForReserved);
                stockItem.setIsReserved(true);
                stockItemRepository.save(stockItem);
                eventPublisher.publishEvent(new StockItemChangedEvent(stockItem, TypeOfActionWithStockItemEnum.RESERVED.getValue()));
                return true;
            } else return false;
        }
        if (stockItem.getQuantity() >= quantityForReserved) {
            stockItem.setReserved(quantityForReserved);
            stockItem.setIsReserved(true);
            stockItemRepository.save(stockItem);
            eventPublisher.publishEvent(new StockItemChangedEvent(stockItem, TypeOfActionWithStockItemEnum.RESERVED.getValue()));
            return true;
        }
        return false;
    }

    @Transactional
    public void withdrawProductAfterPayOrDelete(Order order, String value) {
        List<ProductListDTO> productListDTOS = new ArrayList<>();
        order.getOrderDetail().getOrderProducts().forEach(orderProduct -> {
            productListDTOS.add(new ProductListDTO(orderProduct.getProduct().getId(), orderProduct.getQuantity()));
        });
        List<StockItem> stockItems = getStockItems(order);
        stockItems.forEach(stockItem -> {
            AtomicLong quantity = new AtomicLong(0);
            productListDTOS.forEach(productListDTO -> {
                if (productListDTO.idProduct().equals(stockItem.getProduct().getId())) {
                    quantity.set(productListDTO.quantity());
                }
                if (stockItem.getQuantity() < quantity.get()) {
                    LOGGER.warn("Not enough stock for product: " + stockItem.getProduct().getId());
                }
            });
            if (value == OrderStatusEnum.PAYED.getValue()) {
                updateStockItemQuantityAfterPay(stockItem, quantity.get());
                eventPublisher.publishEvent(new StockItemChangedEvent(stockItem, TypeOfActionWithStockItemEnum.WITHDRAW.getValue()));
            }
            if (value == OrderStatusEnum.CANCELLED.getValue()) {
                updateStockItemQuantityAfterDelete(stockItem, quantity.get());
                eventPublisher.publishEvent(new StockItemChangedEvent(stockItem, TypeOfActionWithStockItemEnum.RESERVATION_CANCELLATION.getValue()));
            }
            stockItemRepository.save(stockItem);
        });
    }

    private List<StockItem> getStockItems(Order order) {
        List<OrderWarehouse> orderWarehouses = orderWarehouseRepository.findAllByOrder_Id(order.getId());
        List<StockItem> stockItems = new ArrayList<>();
        orderWarehouses.forEach(orderWarehouse -> {
            stockItems.add(stockItemRepository.findByProductAndWarehouse(orderWarehouse.getProduct().getId(),
                    orderWarehouse.getWarehouse().getLocationNumber()).get());
        });
        return stockItems;
    }


    private StockItem getStockItem(StockItemReq stockItemReq) {
        if (stockItemReq == null) {
            throw new IllegalArgumentException("StockItemReq cannot be null");
        }
        Warehouse warehouse = warehouseRepository.findWarehouseByLocationNumber(stockItemReq.warehouseLocation())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found for location: " + stockItemReq.warehouseLocation()));
        Product product = productRepository.findById(UUID.fromString(stockItemReq.productId()))
                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + stockItemReq.productId()));
        StockItem stockItem = stockItemRepository.findByProductAndWarehouse(product.getId(), warehouse.getLocationNumber())
                .orElseThrow(() -> new EntityNotFoundException("StockItem not found for product and warehouse location"));
        return stockItem;
    }

    private void updateStockItemQuantityAfterPay(StockItem stockItem, long quantity) {
        long newQuantity = stockItem.getQuantity() - quantity;
        stockItem.setQuantity(newQuantity);
        if (stockItem.getIsReserved()) {
            stockItem.setReserved(stockItem.getReserved() - quantity);
        }
        if (stockItem.getReserved() == 0) {
            stockItem.setIsReserved(false);
        }
        BigDecimal newTotalValue = BigDecimal.valueOf(newQuantity).multiply(stockItem.getProduct().getPrice());
        stockItem.setTotalValue(newTotalValue);
        stockItemRepository.save(stockItem);
    }

    private void updateStockItemQuantityAfterDelete(StockItem stockItem, long quantity) {
        if (stockItem.getIsReserved()) {
            stockItem.setReserved(stockItem.getReserved() - quantity);
        }
        stockItemRepository.save(stockItem);
    }

    @Transactional
    public boolean cancelReserve(StockItemReq stockItemReq) {
        StockItem stockItem = getStockItem(stockItemReq);
        if (stockItem == null) {
            return false;
        }
        var newQuantity = stockItem.getReserved() - stockItemReq.quantity();
        if (newQuantity < 0) {
            return false;
        }
        stockItem.setReserved(newQuantity);
        if (stockItem.getReserved() == 0) {
            stockItem.setIsReserved(false);
        }
        stockItemRepository.save(stockItem);
        return true;
    }
}
