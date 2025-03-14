package org.example.service.service;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.BaseReq;
import org.example.entity.product.Vendor;
import org.example.enums.TypeOfChangesEnum;
import org.example.event.EntityChangedEvent;
import org.example.repository.VendorRepository;
import org.example.service.interfaces.IProductService;
import org.example.service.interfaces.IVendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
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
    @Autowired
    private final ApplicationEventPublisher eventPublisher;
    @Value("${vendor.data.file}")
    private String vendorDataFile;
    private List<Vendor> vendorList;
    private static final Logger LOGGER = LogManager.getLogger();
    public VendorService(VendorRepository vendorRepository, ApplicationEventPublisher eventPublisher) {
        this.vendorRepository = vendorRepository;
        this.eventPublisher = eventPublisher;
    }

    @PostConstruct
    public void init() throws IOException, ClassNotFoundException {
        //vendorList = readFromJsonFile(vendorDataFile, vendorRepository);
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
    public Vendor createItem(BaseReq baseReq) throws IOException, ClassNotFoundException {
        if (!baseReq.name().isEmpty()
                && !baseReq.country().isEmpty()) {
            try {
                if(vendorRepository.findByBrand(baseReq.name()).isPresent()) {
                 LOGGER.info("This vendor with name {} is already exist",baseReq.name());
                    return null;
                }
                Vendor vendor = new Vendor();
                vendor.setName(baseReq.name());
                vendor.setCountry(baseReq.country());
                addEntity(vendor, vendorRepository);
                LOGGER.info("Vendor with name {} was created successful",vendor.getName());
                eventPublisher.publishEvent(new EntityChangedEvent(vendor, TypeOfChangesEnum.CREATED_BY_ADMIN.getValue()));
                return vendor;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error saving vendor data to file: " + e.getMessage());
            }
        }
        return null;
    }
}
