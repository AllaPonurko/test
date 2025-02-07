package org.example.entity.product;

import jakarta.persistence.*;

@Entity
@Table(name="electronics")
public class Electronic extends Product{
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;
    //Constructor
    public Electronic(String name, double price, String description, Vendor vendor,int type)
    {
        super(name,  price, description);
        this.vendor=vendor;
    }

    public Electronic() {

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
