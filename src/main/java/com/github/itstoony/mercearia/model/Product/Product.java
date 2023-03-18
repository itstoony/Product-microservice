package com.github.itstoony.mercearia.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    private Long id;

    private String name;

    private String description;

    private BigDecimal value;

    private Integer quantity;

}
