package com.github.itstoony.mercearia.product.repository;

import com.github.itstoony.mercearia.model.Product.Product;
import com.github.itstoony.mercearia.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ProductRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProductRepository repository;

    @Test
    @DisplayName("Should return a page of products filtering by name")
    public void findByNameTest() {
        // scenery
        Product product = createValidProduct();
        String name = "Refrigeran";
        PageRequest pageable = PageRequest.of(0, 10);

        entityManager.persist(product);
        // execution
        Page<Product> result = repository.findByName(name, pageable);

        // validation
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).contains(product);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);

    }

    private static Product createValidProduct() {
        return Product.builder()
                .name("Refrigerante")
                .quantity(20)
                .description("Convenção Guaraná 2L")
                .productValue(new BigDecimal("10.0"))
                .build();
    }
}
