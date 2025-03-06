package org.example.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.OrderDetailReq;
import org.example.dto.OrderReq;
import org.example.entity.order.Order;
import org.example.entity.order.OrderDetail;
import org.example.response.OrderResponse;
import org.example.service.service.OrderDetailService;
import org.example.service.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
public class SaleController {
    private final OrderDetailService orderDetailService;
    private final OrderService orderService;
    private static final Logger LOGGER = LogManager.getLogger();

    public SaleController(OrderDetailService orderDetailService, OrderService orderService) {
        this.orderDetailService = orderDetailService;
        this.orderService = orderService;
    }

    @PostMapping("/createOrderDetail")
    public ResponseEntity<?> createOrderDetail(@RequestBody OrderDetailReq detailDTO) throws IOException, ClassNotFoundException {
        OrderDetail orderDetail = orderDetailService.createOrderDetail(detailDTO);
        if (orderDetail != null) {
            return ResponseEntity.ok(orderDetail);
        }
        return ResponseEntity.status(500).body("Order isn't created");
    }

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody OrderReq orderReq) throws IOException, ClassNotFoundException {
        Order order = orderService.createOrder(orderReq);
        if (order != null) {
            OrderResponse response = new OrderResponse(order.toString(), " was created.");
            LOGGER.info("Order with {} ", order.getId() + " was created successful.");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body("");
    }

    @DeleteMapping("/deleteOrder")
    public ResponseEntity<String> deleteOrder(@RequestBody UUID orderId) {
        if (orderService.deleteOrder(orderId) == true) {
            LOGGER.info("Order with {} ", orderId + " was deleted successful.");
            return ResponseEntity.ok("Order is deleted successful.");
        } else return ResponseEntity.status(404).body("Order with {} " + orderId + " does not exist");
    }
}
