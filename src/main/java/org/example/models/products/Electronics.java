package org.example.models.products;

import jakarta.persistence.*;
import org.example.models.User;

import java.math.BigDecimal;

@Entity
@Table(name="electronics")
public class Electronics extends Product{
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;
    //Constructor
    public Electronics(String name, double price, String description,Vendor vendor)
    {
        super(name,  price, description);
        this.vendor=vendor;
    }

    public Electronics() {

    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
    @Override
    public String toString() {
        return super.toString() +
                ", brand='" + vendor.toString();
    }
}
