package org.example.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.service.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayController {
    @Autowired
    private final PayService payService;
    private static final Logger LOGGER = LogManager.getLogger();
    public PayController(PayService payService) {
        this.payService = payService;
    }
    @PostMapping("/paymentProcessing")
    public ResponseEntity<?> paymentProcessing(String orderId){
        if(payService.payProcess(orderId)){
            LOGGER.info("Payment by order with orderId {}",orderId+" is successful");
            return ResponseEntity.ok("Payment by order with orderId "+orderId+" is successful");
        }
        LOGGER.info("Payment by order with orderId {}",orderId+" is failed");
        return ResponseEntity.status(400).body("Payment is failed");
    }
}
