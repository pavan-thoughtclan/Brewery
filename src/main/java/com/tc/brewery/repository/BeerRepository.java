package com.tc.brewery.repository;

import com.tc.brewery.entity.Beer;
import com.tc.brewery.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BeerRepository extends JpaRepository<Beer, Integer> {

    List<Beer> findByCategory(String category);

    Beer findByIdAndCategory(int id, String category);

    List<Beer> findByAverageRatingGreaterThanEqual(BigDecimal rating);

    List<Beer> findByAverageRatingBetween(BigDecimal lowerLimit, BigDecimal upperLimit);


    Beer findByName(String name);

}

