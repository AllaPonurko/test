package org.example.service.service;

import jakarta.transaction.Transactional;
import org.example.dto.BaseReq;
import org.example.entity.product.Electronic;
import org.example.enums.ProductTypeEnum;
import org.example.enums.TypeOfChangesEnum;
import org.example.event.EntityChangedEvent;
import org.example.repository.ElectronicsRepository;
import org.example.repository.ProductRepository;
import org.example.repository.TypeItemRepository;
import org.example.repository.VendorRepository;
import org.example.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService<Product> extends BaseService<org.example.entity.product.Product> implements IProductService<org.example.entity.product.Product, BaseReq> {
    @Value("${product.data.file}")

    private String productDataFile;

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final ElectronicsRepository electronicsRepository;
    @Autowired
    private final TypeItemRepository typeItemRepository;
    @Autowired
    private final ApplicationEventPublisher eventPublisher;

    public ProductService(ProductRepository productRepository,
                          ElectronicsRepository electronicsRepository, TypeItemRepository typeItemRepository,
                          ApplicationEventPublisher eventPublisher) {

        this.productRepository = productRepository;
        this.electronicsRepository = electronicsRepository;
        this.typeItemRepository = typeItemRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public org.example.entity.product.Product createItem(BaseReq baseReq) throws IOException, ClassNotFoundException {
        if (!baseReq.name().isEmpty() && !(baseReq.price() != null) && !(baseReq.productType() == 0)) {
            org.example.entity.product.Product product = new org.example.entity.product.Product(baseReq.name(), baseReq.price(),
                    baseReq.description());
            var type =typeItemRepository.findById(Long.valueOf(baseReq.productType())) ;
            product.setProductType(type.get().getEnumValue());
            product.setTypeItem(type.get());
            product.setAvailable(false);
            try {
                addEntity(product, productRepository);
                eventPublisher.publishEvent(new EntityChangedEvent(product, TypeOfChangesEnum.CREATED_BY_ADMIN.getValue()));
                return product;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error saving item data to db: " + e.getMessage());
            }
        }
        return null;
    }

    public Optional<Product> findProductById(String id) {

        return Optional.empty();

    }

    public List<Electronic> findByBrand(String vendorIdString) {
        try {
            UUID vendorId = UUID.fromString(vendorIdString);
            return electronicsRepository.findByVendorId(vendorId);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }


    public List<org.example.entity.product.Product> getProducts() {
        return productRepository.findAll();
    }


    public Optional<org.example.entity.product.Product> findById(String id) {
        return Optional.empty();
    }

    @Override
    protected Class<org.example.entity.product.Product> getEntityClass() {
        return null;
    }
}
