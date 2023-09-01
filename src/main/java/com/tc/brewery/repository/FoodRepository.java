package com.tc.brewery.repository;

import com.tc.brewery.entity.Beer;
import com.tc.brewery.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findByCategory(String category);
    Food findByIdAndCategory(int id, String category);
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Food f WHERE f.food_name = ?1")
    boolean existsByFoodName(String foodName);
}
