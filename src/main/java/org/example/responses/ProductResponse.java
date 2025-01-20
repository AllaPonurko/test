package org.example.responses;

import org.example.models.products.Product;

public class ProductResponse extends BaseResponse<Product> {
    public ProductResponse(String message, Product entity) {
        super(message, entity);
    }
}
