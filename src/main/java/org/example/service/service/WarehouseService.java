package org.example.service.service;

import jakarta.transaction.Transactional;
import org.example.entity.warehouse.StockItem;
import org.example.entity.warehouse.Warehouse;
import org.example.enums.LocationEnum;
import org.example.repository.StockItemRepository;
import org.example.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarehouseService {
    @Autowired
    private final WarehouseRepository warehouseRepository;
    @Autowired
    private final StockItemRepository stockItemRepository;

    public WarehouseService(WarehouseRepository warehouseRepository, StockItemRepository stockItemRepository) {
        this.warehouseRepository = warehouseRepository;
        this.stockItemRepository = stockItemRepository;
    }

    @Transactional
    public int createWarehouse(int location) {
        Warehouse warehouse = new Warehouse();
        if (!warehouseRepository.findWarehouseByLocationNumber(location).isPresent()) {
            switch (location) {
                case 1: {
                    createValues(warehouse, LocationEnum.PRAHA_ZLICIN.getValue(),1);
                    break;
                }
                case 2: {
                    createValues(warehouse, LocationEnum.PRAHA_VYSEGRAD.getValue(),2);
                    break;
                }
                case 3: {
                    createValues(warehouse, LocationEnum.RUDNA.getValue(),3);
                    break;
                }
                case 4: {
                    createValues(warehouse, LocationEnum.PRUHONICE.getValue(),4);
                    break;
                }
                default:
                    return 0;
            }
            if (warehouseRepository.save(warehouse) != null) {
                return 1;
            }
        }
        return -1;
    }

    private void createValues(Warehouse warehouse, String value, int locationNumber) {
        warehouse.setLocation(value);
        warehouse.setLocationNumber(locationNumber);
        warehouse.setName(
                Arrays.stream(value.toLowerCase().replace("_", " ").split(" "))
                        .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                        .collect(Collectors.joining(" ")));
    }

    @Transactional
    public int deleteWarehouse(int location) {
        Warehouse warehouse = warehouseRepository.findWarehouseByLocationNumber(location).orElse(null);
        if (warehouse != null && stockItemRepository.findAll().contains(warehouse))
            return 0;
        if (warehouse != null && !stockItemRepository.findAll().contains(warehouse)) {
            warehouseRepository.delete(warehouse);
            return 1;
        } else return -1;
    }
}
