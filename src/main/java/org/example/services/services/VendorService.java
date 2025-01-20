package org.example.services.services;

import jakarta.annotation.PostConstruct;
import org.example.dto.BaseDTO;
import org.example.models.products.Vendor;
import org.example.repositories.VendorRepository;
import org.example.services.interfaces.IProductService;
import org.example.services.interfaces.IVendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class VendorService extends BaseService<Vendor>
        implements
        IProductService<Vendor,BaseDTO>,
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
    public Vendor createProduct(BaseDTO baseDTO) throws IOException, ClassNotFoundException {
        if (!baseDTO.name.isEmpty()
                && !baseDTO.country.isEmpty()) {
            try {
                if(vendorRepository.findByBrand(baseDTO.name).isPresent()) {
                    return null;
                }
                Vendor vendor = new Vendor();
                vendor.setName(baseDTO.name);
                vendor.setCountry(baseDTO.country);
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
