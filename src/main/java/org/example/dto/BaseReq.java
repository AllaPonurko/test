package org.example.dto;


import java.math.BigDecimal;

public record BaseReq(String name,
                      BigDecimal price,
                      String description,
                      Long productType,
                      String author,
                      Long genre,
                      String country) {

}




