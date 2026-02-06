package com.foodcloud.gateway.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/restaurants")
    public Mono<Map<String, String>> restaurantFallback() {
        return Mono.just(Map.of("message", "Restaurant service is temporarily unavailable. Please try again later."));
    }

    @GetMapping("/orders")
    public Mono<Map<String, String>> ordersFallback() {
        return Mono.just(Map.of("message", "Orders service is temporarily unavailable. Please try again later."));
    }

    @GetMapping("/deliveries")
    public Mono<Map<String, String>> deliveryFallback() {
        return Mono.just(Map.of("message", "Delivery service is temporarily unavailable. Please try again later."));
    }


}
