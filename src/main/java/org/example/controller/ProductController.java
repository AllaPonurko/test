package org.example.controller;

import org.example.dto.BaseDTO;
import org.example.dto.OrderDetailDTO;
import org.example.models.orders.OrderDetail;
import org.example.models.products.Book;
import org.example.models.products.Vendor;
import org.example.repositories.OrderDetailRepository;
import org.example.responses.BookResponse;
import org.example.responses.ProductsResponse;
import org.example.responses.VendorResponse;
import org.example.services.services.BookService;
import org.example.services.services.OrderDetailService;
import org.example.services.services.ProductService;
import org.example.services.services.VendorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final BookService bookService;
    private final VendorService vendorService;
    private final OrderDetailService orderDetailService;
    private  final ProductService productService;
    @Autowired
    public ProductController(BookService bookService, VendorService vendorService, OrderDetailRepository orderDetailRepository, OrderDetailService orderDetailService, ProductService productService) {
        this.bookService = bookService;
        this.vendorService = vendorService;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
        logger.info("ProductController initialized!");
    }

    @GetMapping("/getBooks")
    public ResponseEntity<?> getListOfBooks() {
        List<Book> productList = bookService.getList();
        ProductsResponse<Book> response = new ProductsResponse<>(productList);
        if (productList.isEmpty())
            return ResponseEntity.status(500).body("The list of products is not exist");
        return ResponseEntity.ok(response);
    }
    @PostMapping("/createOrderDetail")
    public  ResponseEntity<?> createOrderDetail(@RequestBody OrderDetailDTO detailDTO) throws IOException, ClassNotFoundException {
        OrderDetail orderDetail=orderDetailService.createProduct(detailDTO);
        if(orderDetail!=null) {
            return ResponseEntity.ok(orderDetail);
        }
        return ResponseEntity.status(500).body("Order isn't created");
    }

    @PostMapping("/createItem")
    public ResponseEntity<?> createProduct(@RequestBody BaseDTO baseDTO) throws IOException, ClassNotFoundException {
        switch (baseDTO.productType) {
            case "book": {
                try {
                    Book book=bookService.createProduct(baseDTO);
                    if(book!=null)
                    {
                        BookResponse response=new BookResponse("Book is created successful!!!",book);
                        return ResponseEntity.ok(String.valueOf(response));
                    }
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create a book" + e.getMessage());
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            case "vendor": {
                try {
                    if(vendorService.createProduct(baseDTO)!=null)
                    return
                            ResponseEntity.ok("Vendor was created successful!!!");
                    else
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("This vendor already exists.");
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create a vendor" + e.getMessage());
                } catch (IOException | ClassNotFoundException e) {
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
                }
            }
        }
        return ResponseEntity.status(404).body("The page doesn't find");
    }

    @GetMapping("/getVendorByBrand")
    public ResponseEntity <String> getVendorByBrand(@RequestParam String brand) {
            Optional<Vendor> vendor = vendorService.findByBrand(brand);
            if (vendor.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("This vendor doesn't exist");
            }
            VendorResponse response = new VendorResponse("Vendor",vendor.get());
            return ResponseEntity.ok(String.valueOf(response));
    }

}
