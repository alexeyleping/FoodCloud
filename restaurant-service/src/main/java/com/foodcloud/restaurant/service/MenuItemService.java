package com.foodcloud.restaurant.service;

import com.foodcloud.restaurant.entity.MenuItem;
import com.foodcloud.restaurant.entity.Restaurant;
import com.foodcloud.restaurant.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    private final RestaurantService restaurantService;

    public Long addMenuItem(Long restaurantId, MenuItem menuItem) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        menuItem.setRestaurant(restaurant);
        return menuItemRepository.save(menuItem).getId();
    }

    public List<MenuItem> getMenuItemsByRestaurantId(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }
}
