package com.github.itstoony.mercearia.product.utils;

import com.github.itstoony.mercearia.model.Product.Product;
import com.github.itstoony.mercearia.model.dto.ProductDTO;

import java.math.BigDecimal;

public class Utils {

    public static Product createValidProduct() {
        return Product.builder()
                .id(1L)
                .name("Refrigerante")
                .quantity(20)
                .description("Convenção Guaraná 2L")
                .productValue(new BigDecimal("10.0"))
                .build();
    }

    public static ProductDTO createValidProductDTO() {
        return ProductDTO.builder()
                .name("Refrigerante")
                .quantity(20)
                .description("Convenção Guaraná 2L")
                .productValue(new BigDecimal("10.0"))
                .build();
    }

}
