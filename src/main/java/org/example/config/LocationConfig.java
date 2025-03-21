package org.example.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocationConfig {
    @Value("${warehouse.location.min}")
    private int minWarehouseLocation;

    @Value("${warehouse.location.max}")
    private int maxWarehouseLocation;

    public int getMinWarehouseLocation() {
        return minWarehouseLocation;
    }

    public int getMaxWarehouseLocation() {
        return maxWarehouseLocation;
    }
}
