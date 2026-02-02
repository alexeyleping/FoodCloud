package com.foodcloud.order.service.client;

import com.foodcloud.order.service.dto.RestaurantDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "RESTAURANT-SERVICE")  // имя из Eureka
public interface RestaurantClient {

    @GetMapping("/api/restaurants/{id}")    // эндпоинт restaurant-service
    RestaurantDto getRestaurantById(@PathVariable Long id);

}
