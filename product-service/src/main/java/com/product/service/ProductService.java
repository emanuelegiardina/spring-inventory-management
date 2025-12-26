package com.product.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.product.model.Product;
import com.product.repository.ProductRepository;
import com.shared.OrderItemDto;
import com.shared.StockCheckRequest;
import com.shared.StockCheckResponse;
import com.shared.StockItemResponse;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }
     // insert
    public Product createProduct(Product product) {
        return repository.save(product);
    }
    // select 
    public List<Product> getProduct(){
         return repository.findAll();

    }

    public Mono<StockCheckResponse> checkStock(StockCheckRequest request) {

        List<StockItemResponse> result = new ArrayList<>();
        boolean allAvailable = true;

        for (OrderItemDto item : request.getItems()) {

            Product p = repository.findById(item.getProductId()).orElse(null);

            boolean available = p != null && p.getQuantita() >= item.getQuantity();

            if (!available) {
                allAvailable = false;
            }

            result.add(new StockItemResponse(
                item.getProductId(),
                available,
                p != null ? p.getQuantita() : 0
            ));
        }

        StockCheckResponse response =
                new StockCheckResponse(allAvailable, result);

        return Mono.just(response); 
    }


/*
    public Product getProduct(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public boolean isProductAvailable(Long productId, int requestedQty) {
        Product product = getProduct(productId);
        return product.getQuantita() >= requestedQty;
    }

    public void decreaseStock(Long productId, int qty) {
        Product product = getProduct(productId);
        if (product.getQuantita() < qty) {
            throw new IllegalStateException("Not enough stock");
        }
        product.setQuantita(product.getQuantita() - qty);
        repository.save(product);
    }*/
}
