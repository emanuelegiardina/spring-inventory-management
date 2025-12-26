package com.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //Optional<Product> findByName(String nome);
    Optional<Product> findByNome(String nome);
}

