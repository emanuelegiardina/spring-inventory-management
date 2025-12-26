package com.order.controller;

import com.order.service.OrderService;
import com.shared.StockCheckRequest;
import com.shared.StockCheckResponse;
import reactor.core.publisher.Mono;
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
    


} 
