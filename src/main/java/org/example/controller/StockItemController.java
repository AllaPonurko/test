package org.example.controller;


import jakarta.transaction.Transactional;
import org.example.dto.StockItemReq;
import org.example.entity.warehouse.StockItem;
import org.example.service.service.StockItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockItemController {
    @Autowired
    private final StockItemService stockItemService;

    public StockItemController(StockItemService stockItemService) {
        this.stockItemService = stockItemService;
    }
    @PostMapping("/createStockItem")
    public ResponseEntity createOrUpdateStockItem(@RequestBody StockItemReq stockItemReq){
        StockItem stockItem=stockItemService.createStockItem(stockItemReq);
        if(stockItem!=null){
            return ResponseEntity.ok("StokItem with productId "+stockItem.getProduct().getId()+
                    " and warehouse location "+stockItem.getWarehouse().getLocation()+" " +
                    "was created successful.");
        }
        return ResponseEntity.badRequest().body("Failed to create stockItem");
    }
}
