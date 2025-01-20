package org.example.responses;

import java.util.List;

public class ProductsResponse<T>{
    private final int productCount;
    private final List<T> productList;

    public ProductsResponse(List<T>productList ) {
        this.productCount = productList.size();
        this.productList = productList;
    }

    public int getUserCount() {
        return productCount;
    }

    public List<T> getProducts() {
        return productList;
    }
}
