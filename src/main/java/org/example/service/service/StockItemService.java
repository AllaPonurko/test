package org.example.service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.StockItemReq;
import org.example.entity.product.Product;
import org.example.entity.warehouse.StockItem;
import org.example.entity.warehouse.Warehouse;
import org.example.enums.TypeOfActionWithStockItem;
import org.example.event.StockItemChangedEvent;
import org.example.repository.ProductRepository;
import org.example.repository.StockItemRepository;
import org.example.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StockItemService {
    @Autowired
    private final StockItemRepository stockItemRepository;
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final WarehouseRepository warehouseRepository;
    @Autowired
    private final WarehouseService warehouseService;
    private static final Logger LOGGER = LogManager.getLogger();
    @Autowired
    private final ApplicationEventPublisher eventPublisher;
    public StockItemService(StockItemRepository stockItemRepository, ProductRepository productRepository, WarehouseRepository warehouseRepository, WarehouseService warehouseService, ApplicationEventPublisher eventPublisher) {
        this.stockItemRepository = stockItemRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.warehouseService = warehouseService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public StockItem createStockItem(StockItemReq stockItemReq) {
        if (stockItemReq != null) {
            Warehouse warehouse = warehouseRepository.findWarehouseByLocationNumber(stockItemReq.warehouseLocation()).orElse(null);
            Product product = productRepository.findById(UUID.fromString(stockItemReq.productId())).orElse(null);
            if (warehouse != null && product != null) {
                StockItem stockItem = stockItemRepository.findByProductAndWarehouse(UUID.fromString(stockItemReq.productId()), stockItemReq.warehouseLocation()).orElse(null);
                if (stockItem == null) {
                    stockItem=new StockItem();
                    stockItem.setProduct(product);
                    stockItem.setWarehouse(warehouse);
                    stockItem.setQuantity(stockItemReq.quantity());
                    stockItem.setTotalValue(stockItem.getTotalValue());
                }
                else{
                    stockItem.setQuantity(stockItem.getQuantity()+stockItemReq.quantity());
                    stockItem.setTotalValue(stockItem.getTotalValue().add(stockItem.getNewTotalValue(product.getPrice(),stockItemReq.quantity())));
                }
                StockItem saveStockItem=stockItemRepository.save(stockItem);
                eventPublisher.publishEvent(new StockItemChangedEvent(saveStockItem, TypeOfActionWithStockItem.RESTOCK.getValue()));
                return saveStockItem;
            }
        }
        return null;
    }
}
