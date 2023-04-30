package itstoony.com.github.mercearia.controller;

import itstoony.com.github.mercearia.model.Product.Product;
import itstoony.com.github.mercearia.service.ProductService;
import itstoony.com.github.mercearia.dto.ProductDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ModelMapper modelMapper;

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> register(@RequestBody @Valid ProductDTO dto) {
        Product product = modelMapper.map(dto, Product.class);
        Product savedProduct = productService.register(product);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedProduct.getId()).toUri();

        ProductDTO updatedDTO = modelMapper.map(savedProduct, ProductDTO.class);

        return ResponseEntity.created(uri).body(updatedDTO) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        Product product = productService.findById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );

        Product updatedProduct = productService.update(product, dto);

        ProductDTO updatedDTO = modelMapper.map(updatedProduct, ProductDTO.class);

        return ResponseEntity.ok(updatedDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        Product product = productService
                .findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ProductDTO dto = modelMapper.map(product, ProductDTO.class);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/list")
    public ResponseEntity<PageImpl<ProductDTO>> listAllProducts(@RequestParam String name, Pageable pageable) {
        Page<Product> page = productService.listAll(name, pageable);

        List<ProductDTO> listDTO = page
                .getContent()
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        return ResponseEntity.ok(new PageImpl<>(listDTO, pageable, page.getTotalElements()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Product product = productService.findById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
        productService.delete(product);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/storage/add/{quantity}")
    public ResponseEntity<ProductDTO> addStorage(@PathVariable(value = "id") Long id,
                                                 @PathVariable(value = "quantity") Integer quantity) {
        Product updatingProduct = productService.findById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );

        Product updatedProduct = productService.addStorage(updatingProduct, quantity);

        ProductDTO dto = modelMapper.map(updatedProduct, ProductDTO.class);

        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}/storage/remove/{quantity}")
    public ResponseEntity<ProductDTO> removeStorage(@PathVariable(value = "id") Long id,
                                                    @PathVariable(value = "quantity") Integer quantity) {
        Product updatingProduct = productService.findById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );

        Product updatedProduct = productService.removeStorage(updatingProduct, quantity);

        ProductDTO dto = modelMapper.map(updatedProduct, ProductDTO.class);

        return ResponseEntity.ok(dto);
    }
}
