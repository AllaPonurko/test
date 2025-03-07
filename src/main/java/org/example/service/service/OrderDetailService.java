package org.example.service.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.example.dto.OrderDetailReq;
import org.example.entity.order.OrderDetail;
import org.example.entity.product.Product;
import org.example.enums.ReasonOfChanges;
import org.example.event.EntityChangedEvent;
import org.example.repository.BookRepository;
import org.example.repository.OrderDetailRepository;
import org.example.repository.ProductRepository;
import org.example.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
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
    @Value("${orderDetail.data.file}")
    private String orderDetailDataFile;
    public OrderDetailService(OrderDetailRepository orderDetailRepository, ApplicationEventPublisher eventPublisher, ProductRepository productRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.eventPublisher = eventPublisher;
        this.productRepository = productRepository;
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
    public OrderDetail createItem(OrderDetailReq baseDTO) throws IOException, ClassNotFoundException, RuntimeException {
        return null;
    }


    @Transactional
    public OrderDetail createOrderDetail(OrderDetailReq<Product> orderDetailReq) {
        if (orderDetailReq != null && !orderDetailReq.uuidList().isEmpty()) {
            OrderDetail orderDetail = new OrderDetail();
            for (UUID id : orderDetailReq.uuidList()) {
                Product item=productRepository.findById(id).get();
                orderDetail.getItemList().add(item);
            }
            try {
                addEntity(orderDetail, orderDetailRepository);
                eventPublisher.publishEvent(new EntityChangedEvent(orderDetail, ReasonOfChanges.CREATED_BY_USER.getValue()));
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
          eventPublisher.publishEvent(new EntityChangedEvent(orderDetail,ReasonOfChanges.MANUAL_DELETED.getValue()));
          orderDetailRepository.delete(orderDetail);
          isOrderDetailDelete=true;
      }
      return isOrderDetailDelete;
    }

    }
