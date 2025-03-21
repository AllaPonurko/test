package org.example.service.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.config.LocationConfig;
import org.example.dto.OrderDetailReq;
import org.example.dto.StockItemReq;
import org.example.entity.order.OrderDetail;
import org.example.entity.order.OrderProduct;
import org.example.entity.product.Product;
import org.example.enums.TypeOfChangesEnum;
import org.example.event.EntityChangedEvent;
import org.example.event.OrderEvent;
import org.example.repository.OrderDetailRepository;
import org.example.repository.ProductRepository;
import org.example.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service

public class OrderDetailService extends BaseService<OrderDetail>implements IProductService<OrderDetail, OrderDetailReq> {

    @Autowired
    private final OrderDetailRepository orderDetailRepository;
    @Autowired
    private final ApplicationEventPublisher eventPublisher;
    @Autowired
    private  final ProductRepository productRepository;
    @Autowired
    private final StockItemService stockItemService;
    @Autowired
    private LocationConfig locationConfig;
    @Value("${orderDetail.data.file}")
    private String orderDetailDataFile;
    public OrderDetailService(OrderDetailRepository orderDetailRepository, ApplicationEventPublisher eventPublisher, ProductRepository productRepository, StockItemService stockItemService) {
        this.orderDetailRepository = orderDetailRepository;
        this.eventPublisher = eventPublisher;
        this.productRepository = productRepository;
        this.stockItemService = stockItemService;
    }
    @PostConstruct
    public void init() throws IOException, ClassNotFoundException {
        //orderDetails=readFromJsonFile(orderDetailDataFile,orderDetailRepository);
    }
    @Override
    protected Class<OrderDetail> getEntityClass() {
        return OrderDetail.class;
    }

    @Override
    @Transactional
    public Optional<OrderDetail> findById(String id) {
        UUID orderDetailId=UUID.fromString(id);
        return orderDetailRepository.findById(orderDetailId);
    }

    @Override
    public OrderDetail createItem(OrderDetailReq baseDTO) throws  RuntimeException {
        return null;
    }


    @Transactional
    public OrderDetail createOrderDetail(OrderDetailReq orderDetailReq, int warehouseLocation) {
        if (orderDetailReq == null || warehouseLocation < locationConfig.getMinWarehouseLocation()
                || warehouseLocation > locationConfig.getMaxWarehouseLocation()){
            return null;
        }
        if (orderDetailReq != null && !orderDetailReq.productList().isEmpty()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetailRepository.save(orderDetail);
            for (var prod : orderDetailReq.productList()) {
                Product item=productRepository.findById(prod.idProduct()).orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + prod.idProduct()));
                StockItemReq stockItemReq = new StockItemReq(item.getId().toString(),warehouseLocation,(int)prod.quantity());
                if(stockItemService.isProductReserved(stockItemReq))
                orderDetail.addOrderProduct(new OrderProduct(orderDetail,item,prod.quantity()));
            }
            try {
                addEntity(orderDetail, orderDetailRepository);
                return orderDetail;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error saving order_detail data to file: " + e.getMessage());
            }
        }
        return null;
    }
    @Transactional
    public boolean deleteOrderDetail(String orderId){
      boolean isOrderDetailDelete=false;
      OrderDetail orderDetail=orderDetailRepository.findByUuid(UUID.fromString(orderId));
      if(orderDetail!=null){
          eventPublisher.publishEvent(new EntityChangedEvent(orderDetail, TypeOfChangesEnum.MANUAL_DELETED.getValue()));
          orderDetailRepository.delete(orderDetail);
          isOrderDetailDelete=true;
      }
      return isOrderDetailDelete;
    }

    }
