package com.order.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.order.model.Order;
import com.order.model.OrderItem;
import com.order.model.OrderStatus;
import com.order.repository.OrderRepository;

import common.dto.ConfirmOrderClientResponse;
import common.dto.ConfirmOrderResponse;
import common.dto.CreateOrderResponse;
import common.dto.OrderItemDto;
import common.dto.StockCheckRequest;
import common.dto.StockCheckResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.reactor.timelimiter.TimeLimiterOperator;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
   
    @Autowired
    private final WebClient webClient;
    private final io.github.resilience4j.circuitbreaker.CircuitBreaker productCB;
    private final Retry retry;
    private final TimeLimiter timeLimiter;

    private final OrderRepository repository;

    public OrderService(WebClient.Builder builder,
                        CircuitBreakerRegistry registry,RetryRegistry retryRegistry,
        TimeLimiterRegistry timeLimiterRegistry, OrderRepository repository) {
        this.webClient = builder.build();
        this.productCB = registry.circuitBreaker("productServiceCB");
        this.retry = retryRegistry.retry("productServiceCB");
        this.timeLimiter = timeLimiterRegistry.timeLimiter("productServiceCB");
        this.repository = repository;
    }


    public Mono<StockCheckResponse> checkStock(StockCheckRequest request) {
    return webClient
        .post()
        .uri("/api/check-stock")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(StockCheckResponse.class)
         .transformDeferred(CircuitBreakerOperator.of(productCB))
         .transformDeferred(RetryOperator.of(retry))
         .transformDeferred(TimeLimiterOperator.of(timeLimiter))
            
            .onErrorResume(this::stockFallback);
} 
    private Mono<StockCheckResponse> stockFallback(Throwable ex) {
     
     StockCheckResponse response = new StockCheckResponse();
     response.setMessage("Fallback: product service non disponibile");
     response.setItems(List.of()); // o null

    return Mono.just(response);
}

  


public CreateOrderResponse createOrder(String userId, String username,String email, List<OrderItemDto> itemsDTO) {
   
 //dati utente per l'ordine
    Order order = new Order();
    order.setCustomerId(userId);
    order.setUsername(username);
    order.setCustomerEmail(email);
    order.setStatus(OrderStatus.PENDING);
    order.setCreatedAt(Instant.now());

    // mapping  DTO - ENTITY
    for (OrderItemDto dto : itemsDTO) {
        OrderItem item = new OrderItem();
        item.setProductId(dto.getProductId());
        
        item.setQuantity(dto.getQuantity());

        // relazione bidirezionale
        item.setOrder(order);
        order.getItems().add(item);
    }
    Order saved = repository.save(order);

    return new CreateOrderResponse(saved.getId(), "Ordine creato con successo");
 }


public ConfirmOrderClientResponse confirmOrder(Long orderId, String userId) {

    Order order = repository
        .findByIdAndCustomerId(orderId, userId)
        .orElseThrow(() -> new RuntimeException("Ordine non trovato"));
       // se l'ordine non è in stato pending 
    if (order.getStatus() != OrderStatus.PENDING) {
        return new ConfirmOrderClientResponse(
            orderId,
            order.getStatus().name(),
            "Ordine già processato",
            List.of()
        );
    }

    // mappa entity → DTO
    List<OrderItemDto> items = order.getItems().stream()
        .map(i -> new OrderItemDto(i.getProductId(), i.getQuantity()))
        .toList();

    ConfirmOrderResponse productResponse =confirmOrder(items);
        //productClient.confirmOrder(items);

    if (!productResponse.isSuccess()) {
        return new ConfirmOrderClientResponse(
            orderId,
            "PENDING",
            productResponse.getMessage(),
            productResponse.getUnavailableItems()
        );
    }

    order.setStatus(OrderStatus.CONFIRMED);

    return new ConfirmOrderClientResponse(
        orderId,
        "CONFIRMED",
        "Ordine confermato con successo",
        List.of()
    );
    }




    public ConfirmOrderResponse confirmOrder(List<OrderItemDto> items) {

    return webClient.post()
        .uri("/api/confirm")
        .bodyValue(items)
        .retrieve()
        .bodyToMono(ConfirmOrderResponse.class)
        .transformDeferred(CircuitBreakerOperator.of(productCB))
        .onErrorResume(ex -> {
            ConfirmOrderResponse fallback = new ConfirmOrderResponse();
            fallback.setSuccess(false);
            fallback.setMessage("Product service non disponibile");
            fallback.setUnavailableItems(items);  // --> inviare items vuoto da corregere
            return Mono.just(fallback);
        })
        .block();
   }



}

