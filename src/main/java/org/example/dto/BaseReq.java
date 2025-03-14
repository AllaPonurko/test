package org.example.dto;


import java.math.BigDecimal;

public record BaseReq(String name,
                      BigDecimal price,
                      String description,
                      int productType,
                      String author,
                      int genre,
                      String country) {

}




