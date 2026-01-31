package com.foodcloud.restaurant.service;

import com.foodcloud.restaurant.entity.Restaurant;
import com.foodcloud.restaurant.exception.RestaurantNotFoundException;
import com.foodcloud.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public Long createRestaurant(Restaurant restaurant) {
       return restaurantRepository.save(restaurant).getId();
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id));
    }

    public void updateRestaurant(Long id, Restaurant restaurant) {
        Restaurant updatedRestaurant = restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id));
        updatedRestaurant.setName(restaurant.getName());
        updatedRestaurant.setAddress(restaurant.getAddress());
        updatedRestaurant.setPhone(restaurant.getPhone());
        updatedRestaurant.setActive(restaurant.isActive());
        restaurantRepository.save(updatedRestaurant);
    }

    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }
}
