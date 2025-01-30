package org.example.services.services;

import jakarta.transaction.Transactional;
import org.example.dto.BaseDTO;
import org.example.models.products.Electronics;
import org.example.repositories.BookRepository;
import org.example.repositories.ElectronicsRepository;
import org.example.repositories.ProductRepository;
import org.example.repositories.VendorRepository;
import org.example.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService<Product> extends BaseService<org.example.models.products.Product> implements IProductService<org.example.models.products.Product,BaseDTO> {
    @Value("${product.data.file}")

    private String productDataFile;
    private List<Electronics> electronics;

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
    public org.example.models.products.Product createProduct(BaseDTO baseDTO) throws IOException, ClassNotFoundException {
        if(!baseDTO.name().isEmpty() && !(baseDTO.price() ==0)){
            org.example.models.products.Product product = new org.example.models.products.Product(baseDTO.name(),baseDTO.price(),
                    baseDTO.description());
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

    public List<Electronics> findByBrand(String vendorIdString) {
        try{
        UUID vendorId = UUID.fromString(vendorIdString);
            return electronicsRepository.findByVendorId(vendorId);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }


    public List<org.example.models.products.Product> getProducts() {
        return productRepository.findAll();
    }


    public Optional<org.example.models.products.Product> findById(String id) {
        return Optional.empty();
    }

    @Override
    protected Class<org.example.models.products.Product> getEntityClass() {
        return null;
    }
}
