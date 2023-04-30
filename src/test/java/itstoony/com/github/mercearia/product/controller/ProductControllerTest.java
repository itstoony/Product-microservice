package itstoony.com.github.mercearia.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import itstoony.com.github.mercearia.dto.ProductDTO;
import itstoony.com.github.mercearia.model.Product.Product;
import itstoony.com.github.mercearia.exception.BusinessException;
import itstoony.com.github.mercearia.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static itstoony.com.github.mercearia.product.utils.Utils.createValidProduct;
import static itstoony.com.github.mercearia.product.utils.Utils.createValidProductDTO;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest
@ActiveProfiles("test")
class ProductControllerTest {

   static String PRODUCT_API = "/api/product";

   @MockBean
   ProductService productService;

   @Autowired
   MockMvc mvc;


   @Test
   @DisplayName("Should register a product")
   void registerProductTest() throws Exception {
      // scenery
      Product product = createValidProduct();

      ProductDTO dto = createValidProductDTO();

      String json = new ObjectMapper().writeValueAsString(dto);

      given( productService.register(any(Product.class)) ).willReturn(product);
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
              .andExpect(jsonPath("productValue").value(dto.getProductValue()))
              .andExpect(jsonPath("quantity").value(dto.getQuantity()))
              .andExpect(jsonPath("description").value(dto.getDescription()));
   }

   @Test
   @DisplayName("Should return 400 Bad Request when there are not enough information")
   void registerProductWithInsufficientDataTest() throws Exception {
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

   @Test
   @DisplayName("Should update a product")
   void updateProductTest() throws Exception {
      // scenery
      Long id = 1L;
      Product product = createValidProduct();

      ProductDTO dto = ProductDTO.builder()
              .name("Refrigerante light")
              .quantity(15)
              .productValue(new BigDecimal("9.0"))
              .build();

      Product updatedProduct = Product.builder()
              .id(id)
              .name(dto.getName())
              .description(dto.getDescription())
              .quantity(dto.getQuantity())
              .productValue(dto.getProductValue())
              .build();

      String json = new ObjectMapper().writeValueAsString(dto);

      given( productService.findById(id) ).willReturn(Optional.of(product));
      given( productService.update(any(Product.class), any(ProductDTO.class)) ).willReturn(updatedProduct);

      // execution
      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
              .put(PRODUCT_API.concat("/" + id))
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON)
              .content(json);

      // validation
      mvc
              .perform(request)
              .andExpect(status().isOk())
              .andExpect(jsonPath("name").value(dto.getName()))
              .andExpect(jsonPath("quantity").value(dto.getQuantity()))
              .andExpect(jsonPath("description").value(dto.getDescription()))
              .andExpect(jsonPath("productValue").value(dto.getProductValue()));
   }

   @Test
   @DisplayName("Should return 404 not found with passing an invalid id")
   void updateProductWithInvalidIDTest() throws Exception {
      // scenery
      Long id = 1L;

      given( productService.findById(id) ).willReturn(Optional.empty());
      ProductDTO dto = createValidProductDTO();

      String json = new ObjectMapper().writeValueAsString(dto);

      // execution
      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
              .put(PRODUCT_API.concat("/" + id))
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON)
              .content(json);

      // validation
      mvc
              .perform(request)
              .andExpect(status().isNotFound());
   }

   @Test
   @DisplayName("Should find a product by it's id")
   void findByIdTest() throws Exception {
      // scenery
      Long id = 1L;
      Product product = createValidProduct();

      given( productService.findById(id) ).willReturn(Optional.of(product) );

      // execution
      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
              .get(PRODUCT_API.concat("/" + id));

      // validation
      mvc
              .perform(request)
              .andExpect(jsonPath("id").value(id))
              .andExpect(jsonPath("name").value(product.getName()))
              .andExpect(jsonPath("quantity").value(product.getQuantity()))
              .andExpect(jsonPath("description").value(product.getDescription()))
              .andExpect(jsonPath("productValue").value(product.getProductValue()));

   }

   @Test
   @DisplayName("Should return 404 not found when passed ID is invalid")
   void findByInvalidIdTest() throws Exception {
      // scenery
      Long id = 1L;

      given( productService.findById(id) ).willReturn(Optional.empty());

      // execution
      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
              .get(PRODUCT_API.concat("/" + id));

      // validation
      mvc
              .perform(request)
              .andExpect(status().isNotFound());
   }

   @Test
   @DisplayName("Should list all products")
   void listProductsTest() throws Exception {
      // scenery
      Product product = createValidProduct();
      String name = "Refrig";
      given( productService.listAll(any(String.class), any(Pageable.class) ) )
              .willReturn(new PageImpl<>(Collections.singletonList(product), Pageable.ofSize(100), 1) );

      // execution
      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
              .get(PRODUCT_API.concat("/list?name="+name));

      // validation
      mvc
              .perform(request)
              .andExpect(status().isOk())
              .andExpect(jsonPath("content", hasSize(1)))
              .andExpect(jsonPath("totalElements").value(1))
              .andExpect( jsonPath("pageable.pageSize").value(100))
              .andExpect( jsonPath("pageable.pageNumber").value(0));
   }

   @Test
   @DisplayName("Should delete a product by its id")
   void deleteProductTest() throws Exception {
      // scenery
      long id = 1L;
      Product product = createValidProduct();

      given( productService.findById(id) ).willReturn(Optional.of(product));

      // execution
      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
              .delete(PRODUCT_API.concat("/" + id));

      // validation
      mvc
              .perform(request)
              .andExpect(status().isNoContent());

   }

   @Test
   @DisplayName("Should return 404 not found when trying to delete a product by an invalid ID")
   void deleteProductWithInvalidIDTest() throws Exception {
      // scenery
      long id = 1L;

      given( productService.findById(id) ).willReturn(Optional.empty());

      // execution
      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
              .delete(PRODUCT_API.concat("/" + id));

      // validation
      mvc
              .perform(request)
              .andExpect(status().isNotFound());

   }

   @Test
      @DisplayName("Should add quantity to a products storage")
   void addStorageTest() throws Exception {
      // scenery
      int quantity = 5;

      long id = 1L;
      Product updatingProduct = createValidProduct();
      updatingProduct.setId(id);

      Product updatedProduct = createValidProduct();
      updatingProduct.setId(id);
      updatedProduct.setQuantity(updatingProduct.getQuantity() + quantity);

      given(productService.findById(id)).willReturn(Optional.of(updatingProduct));
      given(productService.addStorage(any(Product.class), anyInt()))
              .willReturn(updatedProduct);

      // execution
      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
              .patch(PRODUCT_API.concat("/" + id + "/storage/add/" + quantity))
              .accept(MediaType.APPLICATION_JSON);

      // validation
      mvc
              .perform(request)
              .andExpect(status().isOk())
              .andExpect(jsonPath("id").value(id))
              .andExpect(jsonPath("quantity").value(updatedProduct.getQuantity()));
   }

   @Test
   @DisplayName("Should return Bad Request when passed quantity is equal or lower than 0")
   void addInvalidQuantityTest() throws Exception {
      // scenery
      int quantity = 5;

      long id = 1L;
      Product updatingProduct = createValidProduct();
      updatingProduct.setId(id);

      String errorMessage = "Passed quantity should be equal or higher than 1";

      given(productService.findById(id)).willReturn(Optional.of(updatingProduct));
      given(productService.addStorage(any(Product.class), anyInt()))
              .willThrow(new BusinessException(errorMessage));

      // execution
      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
              .patch(PRODUCT_API.concat("/" + id + "/storage/add/" + quantity))
              .accept(MediaType.APPLICATION_JSON);

      // validation
      mvc
              .perform(request)
              .andExpect(status().isBadRequest())
              .andExpect(jsonPath("errors", hasSize(1)))
              .andExpect(jsonPath("errors[0]").value(errorMessage));

   }

   @Test
   @DisplayName("Should remove quantity from a product")
   void removeQuantityTest() throws Exception {
      // scenery
      int quantity = 5;

      long id = 1L;
      Product updatingProduct = createValidProduct();
      updatingProduct.setId(id);

      Product updatedProduct = createValidProduct();
      updatingProduct.setId(id);
      updatedProduct.setQuantity(updatingProduct.getQuantity() - quantity);

      given(productService.findById(id)).willReturn(Optional.of(updatingProduct));
      given(productService.removeStorage(any(Product.class), anyInt()))
              .willReturn(updatedProduct);

      // execution
      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
              .patch(PRODUCT_API.concat("/" + id + "/storage/remove/" + quantity))
              .accept(MediaType.APPLICATION_JSON);

      // validation
      mvc
              .perform(request)
              .andExpect(status().isOk())
              .andExpect(jsonPath("id").value(id))
              .andExpect(jsonPath("quantity").value(updatedProduct.getQuantity()));
   }

   @Test
   @DisplayName("Should return Bad Request when passed quantity is less then products current quantity")
   void removeInvalidStorageTest() throws Exception {
      // scenery
      int quantity = 5;

      long id = 1L;
      Product updatingProduct = createValidProduct();
      updatingProduct.setId(id);

      String errorMessage = "Product's current quantity is less than passed quantity";

      given(productService.findById(id)).willReturn(Optional.of(updatingProduct));
      given(productService.removeStorage(any(Product.class), anyInt()))
              .willThrow(new BusinessException(errorMessage));

      // execution
      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
              .patch(PRODUCT_API.concat("/" + id + "/storage/remove/" + quantity))
              .accept(MediaType.APPLICATION_JSON);

      // validation
      mvc
              .perform(request)
              .andExpect(status().isBadRequest())
              .andExpect(jsonPath("errors", hasSize(1)))
              .andExpect(jsonPath("errors[0]").value(errorMessage));

   }

   @Test
   @DisplayName("Should return Bad Request when passed quantity is equal or lower than 0")
   void removeInvalidQuantityTest() throws Exception {
      // scenery
      int quantity = 5;

      long id = 1L;
      Product updatingProduct = createValidProduct();
      updatingProduct.setId(id);

      String errorMessage = "Passed quantity should be equal or higher than 1";

      given(productService.findById(id)).willReturn(Optional.of(updatingProduct));
      given(productService.removeStorage(any(Product.class), anyInt()))
              .willThrow(new BusinessException(errorMessage));

      // execution
      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
              .patch(PRODUCT_API.concat("/" + id + "/storage/remove/" + quantity))
              .accept(MediaType.APPLICATION_JSON);

      // validation
      mvc
              .perform(request)
              .andExpect(status().isBadRequest())
              .andExpect(jsonPath("errors", hasSize(1)))
              .andExpect(jsonPath("errors[0]").value(errorMessage));

   }

}
