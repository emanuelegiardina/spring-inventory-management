package com.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.product.model.Product;
import com.product.repository.ProductRepository;

import common.dto.ConfirmOrderResponse;
import common.dto.OrderItemDto;
import common.dto.StockCheckRequest;
import common.dto.StockCheckResponse;
import common.dto.StockItemResponse;
import jakarta.transaction.Transactional;
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
    // controlla stock e prodotti
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
    
@Transactional
public ConfirmOrderResponse confirmStock(List<OrderItemDto> items) {

    List<OrderItemDto> unavailable = new ArrayList<>();

    for (OrderItemDto item : items) {
        Optional<Product> product = repository.findById(item.getProductId());

        if (product.isEmpty() || product.get().getQuantita() < item.getQuantity()) {
            unavailable.add(item);
        }
    }

    if (!unavailable.isEmpty()) {
        ConfirmOrderResponse res = new ConfirmOrderResponse();
        res.setSuccess(false);
        res.setMessage("Prodotti non disponibili o stock insufficiente");
        res.setUnavailableItems(unavailable);
        return res;
    }

    // decremento stock
    for (OrderItemDto item : items) {
        Product product = repository.findById(item.getProductId()).get();
        product.setQuantita(product.getQuantita() - item.getQuantity());
    }

    ConfirmOrderResponse res = new ConfirmOrderResponse();
    res.setSuccess(true);
    res.setMessage("Stock aggiornato");
    res.setUnavailableItems(List.of());
    return res;
}




}
