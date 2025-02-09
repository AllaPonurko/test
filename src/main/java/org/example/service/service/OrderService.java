package org.example.service.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.example.dto.OrderReq;
import org.example.entity.order.Order;
import org.example.entity.order.OrderDetail;
import org.example.repository.OrderDetailRepository;
import org.example.repository.OrderRepository;
import org.example.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService extends BaseService<Order> implements IProductService<Order, OrderReq> {
    private List<Order> orders;
    @Autowired
    private final OrderRepository orderRepository;
    @Value("${order.data.file}")
    private String ordersDataFile;
    @Autowired
    private final OrderDetailService orderDetailService;

    public OrderService(OrderRepository orderRepository, OrderDetailService orderDetailService) {
        this.orderRepository = orderRepository;
        this.orderDetailService = orderDetailService;

    }

    @PostConstruct
    public void init() throws IOException, ClassNotFoundException {
        orders = readFromJsonFile(ordersDataFile, orderRepository);
    }

    @Override
    protected Class<Order> getEntityClass() {
        return null;
    }

    @Override
    @Transactional
    public Optional<Order> findById(String id) {
        UUID orderId = UUID.fromString(id);
        return orderRepository.findById(orderId);
    }

    @Override
    @Transactional
    public Order createProduct(OrderReq orderReq) throws IOException, ClassNotFoundException {
        if (orderReq != null) {
            Order order = new Order();
            return order;
        }
        return null;
    }
    @Transactional
    public boolean deleteOrder(OrderReq orderReq) {
        boolean isOrderDelete = false;
        Optional<Order> order = orderRepository.findById(UUID.fromString(orderReq.orderId()));
        if (orderDetailService.deleteOrderDetail(orderReq) == true) {
            //orderRepository.delete(order.get());
            isOrderDelete = true;
        }
        return isOrderDelete;
    }
}
