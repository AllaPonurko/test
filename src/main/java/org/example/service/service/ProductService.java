package org.example.service.service;

import jakarta.transaction.Transactional;
import org.example.dto.BaseReq;
import org.example.entity.product.Electronic;
import org.example.enums.ProductType;
import org.example.repository.BookRepository;
import org.example.repository.ElectronicsRepository;
import org.example.repository.ProductRepository;
import org.example.repository.VendorRepository;
import org.example.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService<Product> extends BaseService<org.example.entity.product.Product> implements IProductService<org.example.entity.product.Product, BaseReq> {
    @Value("${product.data.file}")

    private String productDataFile;
    private List<Electronic> electronics;

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final ElectronicsRepository electronicsRepository;
    @Autowired
    private final VendorRepository vendorRepository;

    public ProductService(ProductRepository productRepository,
                          BookRepository bookRepository,
                          ElectronicsRepository electronicsRepository,
                          VendorRepository vendorRepository){

        this.productRepository=productRepository;
        this.electronicsRepository=electronicsRepository;
        this.vendorRepository = vendorRepository;
    }
    @Transactional
    public org.example.entity.product.Product createProduct(BaseReq baseReq) throws IOException, ClassNotFoundException {
        if(!baseReq.name().isEmpty() && !(baseReq.price() ==0)&&!(baseReq.productType()==0)){
            org.example.entity.product.Product product = new org.example.entity.product.Product(baseReq.name(), baseReq.price(),
                    baseReq.description());
            int kod=baseReq.productType();
            switch ( kod){
                case 1:
                    product.setProductType(ProductType.BOOK);
                    break;
                case 2:
                    product.setProductType(ProductType.VENDOR);
                    break;
                case 3:
                    product.setProductType(ProductType.ELECTRONIC);
                    break;
            }
            product.setAvailable(true);
            try {
                addEntity( product,productDataFile,  productRepository);
                return product;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error saving book data to file: " + e.getMessage());
            }
        }
        return null;
    }

    public Optional<Product> findProductById(String id) {

            return Optional.empty();

    }

    public List<Electronic> findByBrand(String vendorIdString) {
        try{
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
