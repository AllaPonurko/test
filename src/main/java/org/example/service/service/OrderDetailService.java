package org.example.service.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.example.dto.OrderDetailReq;
import org.example.dto.OrderReq;
import org.example.entity.order.OrderDetail;
import org.example.repository.BookRepository;
import org.example.repository.OrderDetailRepository;
import org.example.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service

public class OrderDetailService extends BaseService<OrderDetail>implements IProductService<OrderDetail, OrderDetailReq> {
    private List<OrderDetail> orderDetails;
    @Autowired
    private final OrderDetailRepository orderDetailRepository;
    private  final BookRepository bookRepository;
    @Value("${orderDetail.data.file}")
    private String orderDetailDataFile;
    public OrderDetailService(OrderDetailRepository orderDetailRepository, BookRepository bookRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.bookRepository = bookRepository;
    }
    @PostConstruct
    public void init() throws IOException, ClassNotFoundException {
        orderDetails=readFromJsonFile(orderDetailDataFile,orderDetailRepository);
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
    @Transactional
    public OrderDetail createProduct(OrderDetailReq orderDetailReq)
            throws IOException, ClassNotFoundException, RuntimeException {
        if (orderDetailReq != null && !orderDetailReq.books().isEmpty()) {
            OrderDetail orderDetail = new OrderDetail();
            for (UUID id : orderDetailReq.books()) {
                orderDetail.getbooksList()
                        .add(bookRepository.findById(id).get());
            }
            try {
                addEntity(orderDetail, orderDetailDataFile, orderDetailRepository);
                orderDetailRepository.save(orderDetail);
                return orderDetail;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error saving order_detail data to file: " + e.getMessage());
            }
        }
        return null;
    }
    @Transactional
    public boolean deleteOrderDetail(OrderReq orderReq ){
      boolean isOrderDetailDelete=false;
      OrderDetail orderDetail=orderDetailRepository.findByUuid(UUID.fromString(orderReq.orderId()));
      if(orderDetail!=null){
          orderDetailRepository.delete(orderDetail);
          isOrderDetailDelete=true;
      }
      return isOrderDetailDelete;
    }

    }
