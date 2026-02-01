package com.foodcloud.restaurant.controller;

import com.foodcloud.restaurant.entity.MenuItem;
import com.foodcloud.restaurant.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class MenuItemController {
    private final MenuItemService menuItemService;

    @GetMapping("/{restaurantId}/menu-items")
    public ResponseEntity<List<MenuItem>> getMenuItems(@PathVariable Long restaurantId) {
        List<MenuItem> menuItems = menuItemService.getMenuItemsByRestaurantId(restaurantId);
        return ResponseEntity.ok(menuItems);
    }

    @PostMapping("/{restaurantId}/menu-items")
    public ResponseEntity<MenuItem> addMenuItem(@PathVariable Long restaurantId, @RequestBody MenuItem menuItem) {
        Long id = menuItemService.addMenuItem(restaurantId, menuItem);
        menuItem.setId(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItem);
    }
}
