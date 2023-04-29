package com.github.itstoony.mercearia.product.service;

import com.github.itstoony.mercearia.model.Product.Product;
import com.github.itstoony.mercearia.model.dto.ProductDTO;
import com.github.itstoony.mercearia.repository.ProductRepository;
import com.github.itstoony.mercearia.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static com.github.itstoony.mercearia.product.utils.Utils.createValidProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class ProductServiceTest {

    ProductService service;

    @MockBean
    ProductRepository repository;

    @BeforeEach
    void setUp() {
        this.service = new ProductService(repository);
    }

    @Test
    @DisplayName("Should register a product")
    void registerProductTest() {
        // scenery
        Product product = createValidProduct();
        product.setId(1L);

        BDDMockito.given( repository.save(product) ).willReturn(product);
        // execution
        Product savedProduct = service.register(product);

        // validation
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getQuantity()).isEqualTo(product.getQuantity());
        assertThat(savedProduct.getDescription()).isEqualTo(product.getDescription());
    }

    @Test
    @DisplayName("Should update a product")
    void updateProductTest() {
        // scenery
        Product product = createValidProduct();

        ProductDTO dto = ProductDTO.builder()
                .name("Refrigerante light")
                .quantity(15)
                .productValue(new BigDecimal("9.0"))
                .build();

        // execution
        Product updatedProduct = service.update(product, dto);

        // validation
        assertThat( updatedProduct.getId() ).isEqualTo(product.getId());
        assertThat( updatedProduct.getName() ).isEqualTo(product.getName());
        assertThat( updatedProduct.getQuantity() ).isEqualTo(product.getQuantity());
        assertThat( updatedProduct.getDescription() ).isEqualTo(product.getDescription());

    }

    @Test
    @DisplayName("Should find a product by it's product")
    void findByIdTest() {
        // scenery
        long id = 1L;
        Product product = createValidProduct();
        product.setId(id);

        BDDMockito.given(repository.findById(id)).willReturn(Optional.of(product));

        // execution
        Optional<Product> foundProduct = service.findById(id);

        // validation
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getId()).isEqualTo(id);
        assertThat(foundProduct.get().getProductValue()).isEqualTo(product.getProductValue());
        assertThat(foundProduct.get().getName()).isEqualTo(product.getName());
        assertThat(foundProduct.get().getQuantity()).isEqualTo(product.getQuantity());

    }

    @Test
    @DisplayName("Should return empty optional when trying to find a product by an invalid id")
    void findByInvalidIdTest() {
        // scenery
        Long id = 1L;

        // execution
        Optional<Product> foundBook = service.findById(id);

        // validation
        assertThat(foundBook).isNotPresent();
    }

    @Test
    @DisplayName("Should delete a product")
    void deleteProductTest() {
        // scenery
        Product product = createValidProduct();
        product.setId(1L);

        // execution
        service.delete(product);

        // validation
        verify(repository, Mockito.times(1)).delete(product);

    }

    @Test
    @DisplayName("Should not delete an unsaved product")
    void deleteUnsavedProductTest() {
        // scenery
        Product product = createValidProduct();
        product.setId(null);

        String message = "Can't delete an unsaved product";

        // execution
        Throwable ex = catchThrowable(() -> service.delete(product));

        // validation
        assertThat(ex)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(message);

        verify(repository, Mockito.never()).delete(product);
    }

    @Test
    @DisplayName("Should return a page of products filtering by it's name")
    void findAllTest() {
        // scenery
        Product product = createValidProduct();
        product.setId(1L);

        String name = "Refrigerante";
        PageRequest pageable = PageRequest.of(0, 10);
        PageImpl<Product> page = new PageImpl<>(Collections.singletonList(product), pageable, 1);

        when( repository.findByName(Mockito.any(String.class), Mockito.any(Pageable.class)) )
                .thenReturn(page);

        // execution
        Page<Product> result = service.listAll(name, pageable);

        // validation
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(Collections.singletonList(product));
        assertThat(result.getPageable().getPageNumber()).isZero();
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);

    }

}
