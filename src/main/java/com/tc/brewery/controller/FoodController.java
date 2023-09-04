
package com.tc.brewery.controller;
import com.tc.brewery.entity.Beer;
import com.tc.brewery.entity.Food;
import com.tc.brewery.service.BeerService;
import com.tc.brewery.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class FoodController {
    @Autowired
    private FoodService foodService;

    @GetMapping("/foodcategories")
    @PreAuthorize("hasRole('USER')")
    public Set<String> getAllCategories() {
        return foodService.getAllCategories();
    }

    @GetMapping("/foods")
    @PreAuthorize("hasRole('USER')")
    public List<Food> getAllFoods() {
        return foodService.getAllFoods();
    }

    @GetMapping("/foodcategories/{category}")
    @PreAuthorize("hasRole('USER')")
    public List<Food> getFoodsByFood_category(@PathVariable String category) {
        category=category.replace("_"," ");
        return foodService.getFoodsByCategory(category);
    }

    @GetMapping("/foodcategories/{category}/{foodId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getFoodByCategoryAndId(@PathVariable String category, @PathVariable int foodId) {
        category=category.replace("_"," ");
        Food food = foodService.getFoodByCategoryAndId(foodId, category);

        if (food == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(food);
        }
    }

    @GetMapping("/foods/{foodId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Food> getFoodById(@PathVariable int foodId) {
        Food food = foodService.getFoodById(foodId);

        if (food == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(food);
    }
    @PostMapping("/create_food")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> addFood(@RequestBody Food food) {
        try {
            Food savedFood = foodService.saveFood(food);
            return ResponseEntity.ok(savedFood);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

