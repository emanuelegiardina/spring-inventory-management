package com.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class GatewayFallbackController {

    @GetMapping("/fallback/productMono")
    public Mono<ResponseEntity<String>> productFallback() {
        return Mono.just(
            ResponseEntity.status(503)
                .body("Product service non disponibile")
        );
    }

    @RequestMapping(
    value = "/fallback/orderMono",
    method = {RequestMethod.GET, RequestMethod.POST}
    )
    public Mono<ResponseEntity<String>> orderFallback() {
        return Mono.just(
            ResponseEntity.status(503)
                .body("Order service non disponibile")
        );
    }

/*
    @GetMapping("/fallback/orderMono")
    public Mono<ResponseEntity<String>> orderFallback() {
        return Mono.just(
            ResponseEntity.status(503)
                .body("Order service non disponibile")
        );
    }
*/
    @GetMapping("/fallback/product")
    public String productFallbackString() {
        return "Product service non disponibile";
    }

    @GetMapping("/fallback/order")
    public String orderFallbackString() {
        return "Order service non disponibile";
    }


}

