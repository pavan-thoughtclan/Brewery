package com.tc.brewery.service;

import com.tc.brewery.entity.Beer;
import com.tc.brewery.entity.Food;
import com.tc.brewery.repository.FoodRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FoodService {
    @Autowired
    private FoodRepository foodRepository;

    public Set<String> getAllCategories() {
        List<Food> foods = foodRepository.findAll();
        Set<String> categories = new HashSet<>();
        for (Food food : foods) {
            categories.add(food.getCategory());
        }
        return categories;
    }


    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }


    public List<Food> getFoodsByCategory(String category) {
        return foodRepository.findByCategory(category);
    }


    public Food getFoodByCategoryAndId(int foodId , String category) {
        return foodRepository.findByIdAndCategory(foodId, category);
    }

    public Food getFoodById(int foodId) {
        return foodRepository.findById(foodId).orElse(null);
    }
    public Food saveFood(Food food) {
        String foodName = food.getFood_name();
        if (!StringUtils.isEmpty(foodName) && foodRepository.existsByFoodName(foodName)) {
            throw new RuntimeException("Food with the same name already exists");
        }

        return foodRepository.save(food);
    }
}
