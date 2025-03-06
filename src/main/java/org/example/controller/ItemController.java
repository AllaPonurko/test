package org.example.controller;

import org.example.dto.BaseReq;
import org.example.dto.OrderDTO;
import org.example.entity.order.Order;
import org.example.entity.product.Book;
import org.example.entity.product.Vendor;
import org.example.response.BookResponse;
import org.example.response.ItemsResponse;
import org.example.response.VendorResponse;
import org.example.service.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/product")
public class ItemController {
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    private final BookService bookService;
    private final VendorService vendorService;
    private final OrderService orderService;
    private final ProductService productService;

    @Autowired
    public ItemController(BookService bookService, VendorService vendorService, OrderService orderService, ProductService productService) {
        this.bookService = bookService;
        this.vendorService = vendorService;
        this.orderService = orderService;
        this.productService = productService;
        logger.info("ItemController initialized!");
    }

    @GetMapping("/getBooks")
    public ResponseEntity<?> getListOfBooks() {
        List<Book> books = bookService.getList();
        ItemsResponse<Book> response = new ItemsResponse<>(books);
        if (books.isEmpty())
            return ResponseEntity.status(500).body("The list of products is not exist");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getOrders")
    public ResponseEntity<?> getOrders() {
        List<OrderDTO> orders = orderService.getOrderList();
        if (orders.isEmpty()) {
            return ResponseEntity.status(404).body("List of orders isn't found");
        }
        ItemsResponse<OrderDTO> response = new ItemsResponse<>(orders);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/createItem")
    public ResponseEntity<?> createItem(@RequestBody BaseReq baseReq) throws IOException, ClassNotFoundException {
        switch (baseReq.productType()) {
            case 1: {
                try {
                    Book book = bookService.createItem(baseReq);
                    if (book != null) {
                        BookResponse response = new BookResponse("Book is created successful!!!", book);
                        logger.info("Book with Id {} ", book.getId() + " was created successful");
                        return ResponseEntity.ok(String.valueOf(response));
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    return ResponseEntity.status(400).body("Failed to create a book" + e.getMessage());
                }
            }
            case 2: {
                try {
                    if (vendorService.createItem(baseReq) != null)
                        return
                                ResponseEntity.ok("Vendor was created successful!!!");
                    else
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("This vendor already exists.");
                } catch (IOException | ClassNotFoundException e) {
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
                } catch (Exception e) {
                    return ResponseEntity.status(400).body("Failed to create a vendor" + e.getMessage());
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

    @GetMapping("/getBooksByGenre")
    public ResponseEntity<?> getBooksByGenre(String genre) {
        List<Book> books = bookService.findByGenre(genre);
        ItemsResponse response = new ItemsResponse(books);
        return ResponseEntity.status(200).body(response);
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
