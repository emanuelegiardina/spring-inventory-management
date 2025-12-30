package com.order.controller;

import com.order.service.OrderService;

import common.dto.ConfirmOrderClientResponse;
import common.dto.CreateOrderRequest;
import common.dto.CreateOrderResponse;
import common.dto.StockCheckRequest;
import common.dto.StockCheckResponse;
import reactor.core.publisher.Mono;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
   
    @PostMapping("/check-stock")
    public Mono<StockCheckResponse> checkStock(
            @RequestBody StockCheckRequest request) {

        return orderService.checkStock(request);
    }

    @PostMapping("/create")
    public ResponseEntity<CreateOrderResponse> createOrder(
            @RequestBody CreateOrderRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        // debug jwt
        System.out.println("userdid" + userId + "name "+ username );

        CreateOrderResponse response = orderService.createOrder(userId, username,email, request.getItems());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm/{orderId}")
    public ResponseEntity<ConfirmOrderClientResponse> confirmOrder(
        @PathVariable Long orderId,
        @AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();

        ConfirmOrderClientResponse response =
            orderService.confirmOrder(orderId, userId);

    return ResponseEntity.ok(response);
    }

    


} 
