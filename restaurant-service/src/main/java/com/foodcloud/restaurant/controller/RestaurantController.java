package com.foodcloud.restaurant.controller;

import com.foodcloud.restaurant.entity.Restaurant;
import com.foodcloud.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurant = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurant);
    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        Long newRestaurantId = restaurantService.createRestaurant(restaurant);
        restaurant.setId(newRestaurantId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(restaurant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        restaurantService.updateRestaurant(id, restaurant);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
