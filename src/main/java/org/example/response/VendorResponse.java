package org.example.response;

import org.example.entity.product.Vendor;

public class VendorResponse extends BaseResponse<Vendor> {

    public VendorResponse(String message, Vendor entity) {
        super(message, entity);
    }
}
