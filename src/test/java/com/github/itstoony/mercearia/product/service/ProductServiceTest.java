package com.github.itstoony.mercearia.product.service;

import com.github.itstoony.mercearia.model.Product.Product;
import com.github.itstoony.mercearia.repository.ProductRepository;
import com.github.itstoony.mercearia.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ProductServiceTest {

    ProductService service;

    @MockBean
    ProductRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new ProductService(repository);
    }

    @Test
    @DisplayName("Should register a product")
    public void registerProductTest() {
        // scenery
        Product product = createValidProduct();

        BDDMockito.given( repository.save(product) ).willReturn(product);
        // execution
        Product savedProduct = service.register(product);

        // validation
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getQuantity()).isEqualTo(product.getQuantity());
        assertThat(savedProduct.getDescription()).isEqualTo(product.getDescription());
    }

    private static Product createValidProduct() {
        return Product.builder()
                .id(1L)
                .name("Refrigerante")
                .quantity(20)
                .description("Convenção Guaraná 2L")
                .productValue(new BigDecimal("10.0"))
                .build();
    }

}
