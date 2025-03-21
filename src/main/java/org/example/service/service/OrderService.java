package org.example.service.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.OrderDTO;
import org.example.dto.OrderReq;
import org.example.entity.order.Order;
import org.example.entity.order.OrderDetail;
import org.example.entity.order.OrderWarehouse;
import org.example.entity.user.User;
import org.example.entity.warehouse.Warehouse;
import org.example.enums.OrderStatusEnum;
import org.example.enums.TypeOfChangesEnum;
import org.example.event.EntityChangedEvent;
import org.example.event.OrderEvent;
import org.example.repository.OrderRepository;
import org.example.repository.OrderWarehouseRepository;
import org.example.repository.UserRepository;
import org.example.repository.WarehouseRepository;
import org.example.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InvalidClassException;
import java.math.BigDecimal;
import java.util.*;

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
    @Autowired
    private final StockItemService stockItemService;
    @Autowired
    private final WarehouseRepository warehouseRepository;
    @Autowired
    private final OrderWarehouseRepository orderWarehouseRepository;

    private static final Logger LOGGER = LogManager.getLogger();

    public OrderService(OrderRepository orderRepository, OrderDetailService orderDetailService, UserRepository userRepository, ApplicationEventPublisher eventPublisher, StockItemService stockItemService, WarehouseRepository warehouseRepository, OrderWarehouseRepository orderWarehouseRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailService = orderDetailService;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.stockItemService = stockItemService;
        this.warehouseRepository = warehouseRepository;
        this.orderWarehouseRepository = orderWarehouseRepository;
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
    public Order createItem(OrderReq baseDTO) throws  RuntimeException {
        return null;
    }

    /**
     *
     * @param orderReq
     * @return
     * @throws IOException
     */
    @Transactional
    public Order createOrder(OrderReq orderReq ) throws IOException {
        try {

            if (orderReq != null ) {
                LOGGER.info("Start of order's creating...");
                Order order = new Order();
                OrderDetail orderDetail = orderDetailService.createOrderDetail(orderReq.orderDetailReq(),orderReq.warehouseLocation());
                if (orderDetail != null) {
                    order.setOrderDetail(orderDetail);
                    order.setTotalPrice(getTotalPrice(orderDetail));
                }
                Optional<User> existUser = userRepository.findById(UUID.fromString(orderReq.userId()));
                if (existUser.isPresent()) {
                    order.setUser(existUser.get());
                } else {
                    LOGGER.info("User not found with id: " + orderReq.userId());
                    return null;
                }
                order.setPayed(false);
                order.setValid(true);
                orderRepository.save(order);
                Warehouse warehouse=warehouseRepository.findWarehouseByLocationNumber(orderReq.warehouseLocation()).orElse(null);
                orderDetail.getOrderProducts().forEach(orderProduct -> {
                    OrderWarehouse orderWarehouse=new OrderWarehouse(orderProduct.getProduct(),
                            warehouse,order, orderProduct.getQuantity());
                    orderWarehouseRepository.save(orderWarehouse);
                    LOGGER.info("OrderWarehouse with Id {} created successful",orderWarehouse.getId());
                });
                LOGGER.info("Order with Id {} created successful",order.getId());
                eventPublisher.publishEvent(new OrderEvent(order, OrderStatusEnum.PENDING.getValue()));
                return order;
            }
        } catch (Exception e) {
            throw new InvalidClassException(e.getMessage());
        }
        return null;
    }

    private BigDecimal getTotalPrice(OrderDetail orderDetail) {
        BigDecimal totalAmount = orderDetail.getOrderProducts().stream()
                .map(orderProduct -> orderProduct.getProduct()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(orderProduct.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalAmount;
    }

    @Transactional
    public boolean deleteOrder(UUID orderId) {
        boolean isOrderDelete = false;
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()&&!order.get().isPayed()) {
            eventPublisher.publishEvent(new OrderEvent(order, OrderStatusEnum.CANCELLED.getValue()));
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
