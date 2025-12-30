package com.product.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.model.Product;
import com.product.service.ProductService;


import common.dto.ConfirmOrderResponse;
import common.dto.OrderItemDto;
import common.dto.StockCheckRequest;
import common.dto.StockCheckResponse;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api")
public class ControllerProduct {

    private final ProductService service;

     public ControllerProduct(ProductService service) {
        this.service = service;
    }


   // @PreAuthorize("hasRole('admin') and hasAuthority('product-service:product_create')") 
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/create")
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product saved = service.createProduct(product);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(saved);
    }

    @GetMapping("/getProduct")
    public ResponseEntity<List<Product>>getAllProduct(){
        List<Product> product = service.getProduct();
        return ResponseEntity.status(HttpStatus.OK).body(product);

    }

   // @PreAuthorize("hasRole('admin') and hasAuthority('product-service:product_check-stock')")
    @PreAuthorize("hasAuthority('product_check-stock')")
    @PostMapping("/check-stock")
    public Mono<StockCheckResponse> checkStock(
            @RequestBody StockCheckRequest request) {

        return service.checkStock(request);
    }
    
    @PreAuthorize("hasAuthority('product_confirm')")
    @PostMapping("/confirm")
    public ConfirmOrderResponse confirmStock(
        @RequestBody List<OrderItemDto> items) {

    return service.confirmStock(items);
    }


 }