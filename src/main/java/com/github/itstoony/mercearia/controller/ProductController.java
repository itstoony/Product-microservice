package com.github.itstoony.mercearia.controller;

import com.github.itstoony.mercearia.model.Product.Product;
import com.github.itstoony.mercearia.model.dto.ProductDTO;
import com.github.itstoony.mercearia.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ModelMapper modelMapper;

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO register(@RequestBody @Valid ProductDTO dto) {
        Product product = modelMapper.map(dto, Product.class);
        Product savedProduct = productService.register(product);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        Product product = productService.findById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );

        Product updatedProduct = productService.update(product, dto);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }
}
