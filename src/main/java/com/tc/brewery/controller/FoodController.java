
package com.tc.brewery.controller;
import com.tc.brewery.entity.Beer;
import com.tc.brewery.entity.Food;
import com.tc.brewery.service.BeerService;
import com.tc.brewery.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class FoodController {
    @Autowired
    private FoodService foodService;

    @GetMapping("/foodcategories")
    public Set<String> getAllCategories() {
        return foodService.getAllCategories();
    }

    @GetMapping("/foods")
    public List<Food> getAllFoods() {
        return foodService.getAllFoods();
    }

    @GetMapping("/foodcategories/{category}")
    public List<Food> getFoodsByFood_category(@PathVariable String category) {
        category=category.replace("_"," ");
        return foodService.getFoodsByCategory(category);
    }

    @GetMapping("/foodcategories/{category}/{foodId}")
    public ResponseEntity<?> getFoodByCategoryAndId(@PathVariable String category, @PathVariable int foodId) {
        category=category.replace("_"," ");
        Food food = foodService.getFoodByCategoryAndId(foodId, category);

        if (food == null) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found
        } else {
            return ResponseEntity.ok(food); // Return the beer if found
        }
    }

    @GetMapping("/foods/{foodId}")
    public ResponseEntity<Food> getFoodById(@PathVariable int foodId) {
        Food food = foodService.getFoodById(foodId);

        if (food == null) {
            return ResponseEntity.notFound().build(); // Return 404 status code
        }
        return ResponseEntity.ok(food);
    }
    @PostMapping("/create_food")
    public ResponseEntity<Object> addFood(@RequestBody Food food) {
        try {
            Food savedFood = foodService.saveFood(food);
            return ResponseEntity.ok(savedFood);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

