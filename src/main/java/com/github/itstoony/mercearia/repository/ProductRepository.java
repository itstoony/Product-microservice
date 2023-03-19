package com.github.itstoony.mercearia.repository;

import com.github.itstoony.mercearia.model.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByName(String name, Pageable pageable);
}
