package org.example.controller;

import org.example.entity.warehouse.Warehouse;
import org.example.repository.WarehouseRepository;
import org.example.service.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WarehouseController {
    @Autowired
    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping("/createWarehouse")
    public ResponseEntity createWarehouse(int location) {
        int res = warehouseService.createWarehouse(location);
        if (res == 1)
            return ResponseEntity.ok("Warehouse with location " + location + " was created successful.");
        if (res == 0)
            return ResponseEntity.status(400).body("This location " + location + " is not exist.");
        return ResponseEntity.badRequest().body("Warehouse with location " + location + " is already exist.");
    }

    @DeleteMapping("/deleteWarehouse")
    public ResponseEntity deleteWarehouse(int id) {
        int res = warehouseService.deleteWarehouse(id);
        if (res == 1) {
            return ResponseEntity.status(200).body("Warehouse with Id " + id + "  was deleted successful.");
        }
        if (res == 0) {
            return ResponseEntity.status(400).body("Warehouse with Id " + id + " can not deleted. StockItems are existed.");
        }
        if (res == -1)
            return ResponseEntity.status(404).body("Warehouse with Id " + id + "  not exist.");
        return ResponseEntity.notFound().build();
    }
}
