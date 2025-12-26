package com.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.shared.StockCheckRequest;
import com.shared.StockCheckResponse;

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

    public OrderService(WebClient.Builder builder,
                        CircuitBreakerRegistry registry,RetryRegistry retryRegistry,
        TimeLimiterRegistry timeLimiterRegistry) {
        this.webClient = builder.build();
        this.productCB = registry.circuitBreaker("productServiceCB");
        this.retry = retryRegistry.retry("productServiceCB");
        this.timeLimiter = timeLimiterRegistry.timeLimiter("productServiceCB");
    }

    public Mono<String> callProductService() {
        return webClient
            .get()
            .uri("http://localhost:8080/api/delete")
            .retrieve()
            .bodyToMono(String.class)
            .transformDeferred(CircuitBreakerOperator.of(productCB))
            .transformDeferred(RetryOperator.of(retry))
            .transformDeferred(TimeLimiterOperator.of(timeLimiter))
            
            .onErrorResume(ex -> {
                // 🔥 QUI assorbi l’errore
                
                return Mono.just("Fallback Order: product non disponibile");
            });

    }

    public Mono<StockCheckResponse> checkStock(StockCheckRequest request) {
    return webClient
        .post()
        .uri("http://localhost:8080/api/check-stock")
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
     response.setMessage("Fallback: impossibile verificare lo stock");
     response.setItems(List.of()); // o null

    return Mono.just(response);
}

  

    public Mono<StockCheckResponse> checkStock1(StockCheckRequest request) {
    return webClient
        .post()
        .uri("http://localhost:8080/api/check-stock")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(StockCheckResponse.class);
}


}

