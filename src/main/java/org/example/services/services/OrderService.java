package org.example.services.services;

import jakarta.annotation.PostConstruct;
import org.example.dto.OrderReq;
import org.example.models.orders.Order;
import org.example.repositories.OrderRepository;
import org.example.services.interfaces.IProductService;
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
    private  String ordersDataFile;
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
@PostConstruct
public  void init() throws IOException, ClassNotFoundException {
    orders=readFromJsonFile(ordersDataFile,orderRepository);
}
    @Override
    protected Class<Order> getEntityClass() {
        return null;
    }

    @Override
    public Optional<Order> findById(String id) {
        UUID orderId=UUID.fromString(id);
        return orderRepository.findById(orderId);
    }
@Override
    public Order createProduct(OrderReq orderReq) throws IOException, ClassNotFoundException {
        if(orderReq !=null)
        {
            Order order=new Order();
            return order;
        }
        return null;
    }
}
