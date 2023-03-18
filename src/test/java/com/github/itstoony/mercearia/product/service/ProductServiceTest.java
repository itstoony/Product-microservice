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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;

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
    public void updateProductTest() {
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
    public void findByIdTest() {
        // scenery
        long id = 1L;
        Product product = createValidProduct();
        product.setId(id);

        BDDMockito.given(repository.findById(id)).willReturn(Optional.of(product));

        // execution
        Optional<Product> foundProduct = service.findById(id);

        // validation
        assertThat(foundProduct.isPresent()).isTrue();
        assertThat(foundProduct.get().getId()).isEqualTo(id);
        assertThat(foundProduct.get().getProductValue()).isEqualTo(product.getProductValue());
        assertThat(foundProduct.get().getName()).isEqualTo(product.getName());
        assertThat(foundProduct.get().getQuantity()).isEqualTo(product.getQuantity());

    }

    @Test
    @DisplayName("Should return empty optional when trying to find a product by an invalid id")
    public void findByInvalidIdTest() {
        // scenery
        Long id = 1L;

        // execution
        Optional<Product> foundBook = service.findById(id);

        // validation
        assertThat(foundBook.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Should delete a product")
    public void deleteProductTest() {
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
    public void deleteUnsavedProductTest() {
        // scenery
        Product product = createValidProduct();
        String message = "Can't delete an unsaved product";

        // execution
        Throwable ex = catchThrowable(() -> service.delete(product));

        // validation
        assertThat(ex).isInstanceOf(IllegalArgumentException.class);
        assertThat(ex).hasMessage(message);
        verify(repository, Mockito.never()).delete(product);

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
