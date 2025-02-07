package org.example.entity.product;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="vendors")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String brand;
    private String country;

    public String getName() {
        return brand;
    }

    public void setName(String name) {
        this.brand = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    @Override
    public String toString(){
        return brand;
    }
}
