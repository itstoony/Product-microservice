package itstoony.com.github.mercearia.service;

import itstoony.com.github.mercearia.model.Product.Product;
import itstoony.com.github.mercearia.dto.ProductDTO;
import itstoony.com.github.mercearia.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Product register(Product product) {
        return repository.save(product);
    }

    public Product update(Product product, ProductDTO dto) {

        product.setProductValue(Optional.ofNullable(dto.getProductValue()).orElse(product.getProductValue()));
        product.setName(Optional.ofNullable(dto.getName()).orElse(product.getName()));
        product.setDescription(Optional.ofNullable(dto.getDescription()).orElse(product.getDescription()));
        product.setQuantity(Optional.ofNullable(dto.getQuantity()).orElse(product.getQuantity()));

        repository.save(product);

        return product;
    }

    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    public Page<Product> listAll(String name, Pageable pageable) {
        return repository.findByName(name, pageable);
    }

    public void delete(Product product) {
        if (product == null || product.getId() == null) {
                throw new IllegalArgumentException("Can't delete an unsaved product");
        }
        repository.delete(product);
    }
}
