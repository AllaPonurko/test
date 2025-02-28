package org.example.service.service;

import jakarta.annotation.PostConstruct;
import org.example.dto.BaseReq;
import org.example.entity.product.Vendor;
import org.example.repository.VendorRepository;
import org.example.service.interfaces.IProductService;
import org.example.service.interfaces.IVendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VendorService extends BaseService<Vendor>
        implements
        IProductService<Vendor, BaseReq>,
        IVendorService<Vendor> {
    @Autowired
    private final VendorRepository vendorRepository;
    @Value("${vendor.data.file}")
    private String vendorDataFile;
    private List<Vendor> vendorList;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @PostConstruct
    public void init() throws IOException, ClassNotFoundException {
        vendorList = readFromJsonFile(vendorDataFile, vendorRepository);
    }

    @Override
    protected Class<Vendor> getEntityClass() {
        return Vendor.class;
    }

    public List<Vendor> getVendors() {
        return vendorRepository.findAll();
    }

    @Override
    public Optional<Vendor> findById(String id) {
        try {
            UUID vendorId = UUID.fromString(id);
            return vendorRepository.findById(vendorId);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid UUID format: " + id);
            return Optional.empty();
        }
    }
    @Override
    public Optional<Vendor> findByBrand(String brand) {
        if(brand.length() > 3)
            return vendorRepository.findByBrand(brand);
        return Optional.empty();
    }

    @Override
    public Vendor createProduct(BaseReq baseReq) throws IOException, ClassNotFoundException {
        if (!baseReq.name().isEmpty()
                && !baseReq.country().isEmpty()) {
            try {
                if(vendorRepository.findByBrand(baseReq.name()).isPresent()) {
                    return null;
                }
                Vendor vendor = new Vendor();
                vendor.setName(baseReq.name());
                vendor.setCountry(baseReq.country());

                addEntity(vendor, vendorDataFile, vendorRepository);
                return vendor;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error saving vendor data to file: " + e.getMessage());
            }
        }
        return null;
    }
}
