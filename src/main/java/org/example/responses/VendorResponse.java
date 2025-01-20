package org.example.responses;

import org.example.models.products.Vendor;

public class VendorResponse extends BaseResponse<Vendor> {

    public VendorResponse(String message, Vendor entity) {
        super(message, entity);
    }
}
