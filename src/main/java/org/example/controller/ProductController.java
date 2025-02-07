package org.example.controller;

import org.example.dto.BaseReq;
import org.example.dto.OrderDetailReq;
import org.example.dto.OrderReq;
import org.example.entity.order.Order;
import org.example.entity.order.OrderDetail;
import org.example.entity.product.Book;
import org.example.entity.product.Vendor;
import org.example.response.BookResponse;
import org.example.response.ProductsResponse;
import org.example.response.VendorResponse;
import org.example.service.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final BookService bookService;
    private final VendorService vendorService;
    private final OrderDetailService orderDetailService;
    private final OrderService orderService;
    private final ProductService productService;

    @Autowired
    public ProductController(BookService bookService, VendorService vendorService, OrderDetailService orderDetailService, OrderService orderService, ProductService productService) {
        this.bookService = bookService;
        this.vendorService = vendorService;
        this.orderDetailService = orderDetailService;
        this.orderService = orderService;
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
    public ResponseEntity<?> createOrderDetail(@RequestBody OrderDetailReq detailDTO) throws IOException, ClassNotFoundException {
        OrderDetail orderDetail = orderDetailService.createProduct(detailDTO);
        if (orderDetail != null) {
            return ResponseEntity.ok(orderDetail);
        }
        return ResponseEntity.status(500).body("Order isn't created");
    }
@DeleteMapping("/deleteOrder")
public ResponseEntity<String> deleteOrder(@RequestBody OrderReq orderReq){
        if(orderService.deleteOrder(orderReq)==true)
            return ResponseEntity.ok("Order is deleted successful.");
        else return ResponseEntity.status(404).body("Order with "+orderReq.orderId()+" does not exist");
}
    @PostMapping("/createItem")
    public ResponseEntity<?> createProduct(@RequestBody BaseReq baseReq) throws IOException, ClassNotFoundException {
        switch (baseReq.productType()) {
            case 1: {
                try {
                    Book book = bookService.createProduct(baseReq);
                    if (book != null) {
                        BookResponse response = new BookResponse("Book is created successful!!!", book);
                        return ResponseEntity.ok(String.valueOf(response));
                    }
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create a book" + e.getMessage());
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            case 2: {
                try {
                    if (vendorService.createProduct(baseReq) != null)
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

    @DeleteMapping("/deleteBook")
    public ResponseEntity<String> deleteBook(UUID uuid_Book) {
        if (bookService.deleteBook(uuid_Book) == true) {
            return ResponseEntity.ok("Book is deleted successful.");
        } else return ResponseEntity.status(404).body("Book with " + uuid_Book + " was not found.");
    }

    @GetMapping("/getVendorByBrand")
    public ResponseEntity<String> getVendorByBrand(@RequestParam String brand) {
        Optional<Vendor> vendor = vendorService.findByBrand(brand);
        if (vendor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("This vendor doesn't exist");
        }
        VendorResponse response = new VendorResponse("Vendor", vendor.get());
        return ResponseEntity.ok(String.valueOf(response));
    }

}
