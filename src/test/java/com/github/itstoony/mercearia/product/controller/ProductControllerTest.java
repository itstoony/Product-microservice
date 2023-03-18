package com.github.itstoony.mercearia.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.itstoony.mercearia.model.Product.Product;
import com.github.itstoony.mercearia.model.dto.ProductDTO;
import com.github.itstoony.mercearia.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest
@ActiveProfiles("test")
public class ProductControllerTest {

    static String PRODUCT_API = "/api/product";

    @MockBean
    ProductService productService;

    @Autowired
    MockMvc mvc;


    @Test
    @DisplayName("Should register a product")
    public void registerProductTest() throws Exception {
        // scenery
        Product product = createValidProduct();

        ProductDTO dto = createValidProductDTO();

        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given( productService.register(Mockito.any(Product.class)) ).willReturn(product);
        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PRODUCT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // implementation
        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value(dto.getName()))
                .andExpect(jsonPath("value").value(dto.getValue()))
                .andExpect(jsonPath("quantity").value(dto.getQuantity()))
                .andExpect(jsonPath("description").value(dto.getDescription()));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when there are not enough information")
    public void registerProductWithInsufficientDataTest() throws Exception {
        // scenery
        ProductDTO dto = ProductDTO.builder().build();

        String json = new ObjectMapper().writeValueAsString(dto);

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PRODUCT_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        // validation
        mvc
                .perform(request)
                .andExpect(status().isBadRequest());

    }

    private static Product createValidProduct() {
        return Product.builder()
                .id(1L)
                .name("Refrigerante")
                .quantity(20)
                .description("Convenção Guaraná 2L")
                .value(new BigDecimal("10.0"))
                .build();
    }

    private static ProductDTO createValidProductDTO() {
        return ProductDTO.builder()
                .name("Refrigerante")
                .quantity(20)
                .description("Convenção Guaraná 2L")
                .value(new BigDecimal("10.0"))
                .build();
    }


}
