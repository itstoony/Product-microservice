package com.github.itstoony.mercearia.repository;

import com.github.itstoony.mercearia.model.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT P FROM Product P WHERE LOWER(P.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> findByName(@Param("name") String name, Pageable pageable);

}
