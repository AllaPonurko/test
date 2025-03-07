package org.example.service.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.OrderDTO;
import org.example.dto.OrderDetailReq;
import org.example.dto.OrderReq;
import org.example.dto.UserReq;
import org.example.entity.order.Order;
import org.example.entity.order.OrderDetail;
import org.example.entity.user.User;
import org.example.enums.ReasonOfChanges;
import org.example.event.EntityChangedEvent;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.example.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InvalidClassException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderService extends BaseService<Order> implements IProductService<Order, OrderReq> {
    private List<Order> orders;
    @Autowired
    private final OrderRepository orderRepository;
    @Value("${order.data.file}")
    private String ordersDataFile;
    @Autowired
    private final OrderDetailService orderDetailService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ApplicationEventPublisher eventPublisher;
    private static final Logger LOGGER = LogManager.getLogger();

    public OrderService(OrderRepository orderRepository, OrderDetailService orderDetailService, UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.orderDetailService = orderDetailService;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @PostConstruct
    public void init() throws IOException, ClassNotFoundException {
       // orders = readFromJsonFile(ordersDataFile, orderRepository);
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
    public Order createItem(OrderReq baseDTO) throws IOException, ClassNotFoundException, RuntimeException {
        return null;
    }

    /**
     *
     * @param orderReq
     * @return
     * @throws IOException
     */
    @Transactional
    public Order createOrder(OrderReq orderReq) throws IOException {
        try {
            if (orderReq != null) {
                LOGGER.info("Start of order's creating...");
                Order order = new Order();
                OrderDetail orderDetail = orderDetailService.createOrderDetail(orderReq.orderDetailReq());
                if (orderDetail != null) {
                    order.setOrderDetail(orderDetail);
                    order.setTotalPrice(BigDecimal.valueOf(getTotalPrice(orderDetail)));
                }
                Optional<User> existUser = userRepository.findById(UUID.fromString(orderReq.userId()));
                if (existUser.get() != null) {
                    order.setUser(existUser.get());
                }
                order.setPayed(false);
                order.setValid(true);
                orderRepository.save(order);
                LOGGER.info("Order with Id {} created successful",order.getId());
                eventPublisher.publishEvent(new EntityChangedEvent(order, ReasonOfChanges.CREATED_BY_USER.getValue()));
                return order;
            }
        } catch (Exception e) {
            throw new InvalidClassException(e.getMessage());
        }
        return null;
    }

    private double getTotalPrice(OrderDetail orderDetail) {
        return orderDetail.getItemList().stream()
                .mapToDouble(item -> item.getPrice())
                .sum();
    }

    @Transactional
    public boolean deleteOrder(UUID orderId) {
        boolean isOrderDelete = false;
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            eventPublisher.publishEvent(new EntityChangedEvent(order,ReasonOfChanges.MANUAL_DELETED.getValue()));
            orderRepository.delete(order.get());
            LOGGER.info("Order with Id {} deleted successful",order.get().getId());
            isOrderDelete = true;
        }
        return isOrderDelete;
    }
    @Transactional
    public List<OrderDTO> getOrderList(){
        List<OrderDTO> orderDTOList=new ArrayList<>();
        List<Order> orders=orderRepository.findAll();
        orders.forEach(order -> {
            orderDTOList.add(new OrderDTO(
                    order.getId(),
                    order.getDescription(),
                    order.getCreatedAt(),
                    order.isPayed(),
                    order.isValid(),
                    order.getTotalPrice().doubleValue(),
                    order.getUser().getId(),
                    order.getOrderDetail().getId()));
        });
        return orderDTOList;
    }
}
