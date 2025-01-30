package org.example.services.services;

import jakarta.annotation.PostConstruct;
import org.example.dto.OrderDetailDTO;
import org.example.models.orders.OrderDetail;
import org.example.models.products.Book;
import org.example.repositories.BookRepository;
import org.example.repositories.OrderDetailRepository;
import org.example.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service

public class OrderDetailService extends BaseService<OrderDetail>implements IProductService<OrderDetail, OrderDetailDTO> {
    private List<OrderDetail> orderDetails;
    @Autowired
    private final OrderDetailRepository orderDetailRepository;
    private  final BookRepository bookRepository;
    @Value("${orderDetail.data.file}")
    private String orderDetailDataFile;
    public OrderDetailService(OrderDetailRepository orderDetailRepository, BookRepository bookRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.bookRepository = bookRepository;
    }
    @PostConstruct
    public void init() throws IOException, ClassNotFoundException {
        orderDetails=readFromJsonFile(orderDetailDataFile,orderDetailRepository);
    }
    @Override
    protected Class<OrderDetail> getEntityClass() {
        return OrderDetail.class;
    }

    @Override
    public Optional<OrderDetail> findById(String id) {
        UUID orderDetailId=UUID.fromString(id);
        return orderDetailRepository.findById(orderDetailId);
    }

    @Override
    public OrderDetail createProduct(OrderDetailDTO orderDetailDTO)
            throws IOException, ClassNotFoundException, RuntimeException {
        if (orderDetailDTO != null && !orderDetailDTO.books().isEmpty()) {
            OrderDetail orderDetail = new OrderDetail();
            for (UUID id : orderDetailDTO.books()) {
                orderDetail.getbooksList()
                        .add(bookRepository.findById(id).get());
            }
            try {
                addEntity(orderDetail, orderDetailDataFile, orderDetailRepository);
                orderDetailRepository.save(orderDetail);
                return orderDetail;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error saving order_detail data to file: " + e.getMessage());
            }
        }
        return null;
    }
    }
