package org.example.dto;

import org.example.models.products.Book;

import java.util.List;
import java.util.UUID;

public record OrderDetailDTO(List<UUID> books) {

}
