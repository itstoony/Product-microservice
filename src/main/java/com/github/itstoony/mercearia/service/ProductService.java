package com.github.itstoony.mercearia.service;

import com.github.itstoony.mercearia.model.Product.Product;
import com.github.itstoony.mercearia.model.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    public Product register(Product product) {
        return null;
    }

    public Product update(Product product, ProductDTO dto) {
        return null;
    }

    public Optional<Product> findById(Long id) {
        return Optional.empty();
    }

    public Page<Product> listAll(Pageable pageable) {
        return null;
    }

    public void delete(Product product) {

    }
}
