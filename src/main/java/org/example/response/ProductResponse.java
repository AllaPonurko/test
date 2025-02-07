package org.example.response;

import org.example.entity.product.Product;

public class ProductResponse extends BaseResponse<Product> {
    public ProductResponse(String message, Product entity) {
        super(message, entity);
    }
}
