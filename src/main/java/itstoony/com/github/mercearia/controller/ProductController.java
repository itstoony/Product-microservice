package itstoony.com.github.mercearia.controller;

import itstoony.com.github.mercearia.model.Product.Product;
import itstoony.com.github.mercearia.service.ProductService;
import itstoony.com.github.mercearia.dto.ProductDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{id}")
    public ProductDTO findById(@PathVariable Long id) {
        Product product = productService.findById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return modelMapper.map(product, ProductDTO.class);
    }

    @GetMapping("/list")
    public Page<Product> listAllProducts(@RequestParam String name,  Pageable pageable) {
        return productService.listAll(name, pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Product product = productService.findById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
        productService.delete(product);
    }
}
