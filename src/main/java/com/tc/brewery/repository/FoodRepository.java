package com.tc.brewery.repository;

import com.tc.brewery.entity.Beer;
import com.tc.brewery.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findByCategory(String category);
    Food findByIdAndCategory(int id, String category);

}
