package org.example.dto;

import org.jetbrains.annotations.NotNull;

public record StockItemReq (@NotNull String productId,@NotNull int warehouseLocation,@NotNull long quantity){

}
