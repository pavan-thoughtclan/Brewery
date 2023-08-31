package com.tc.brewery.repository;

import com.tc.brewery.entity.Beer;
import com.tc.brewery.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BeerRepository extends JpaRepository<Beer, Integer> {
    // Add any custom queries if needed

    List<Beer> findByCategory(String category);

    Beer findByIdAndCategory(int id, String category);

    // Add any custom queries if needed
    List<Beer> findByAverageRatingGreaterThanEqual(BigDecimal rating);
//    List<Beer> findByAverageRatingGreaterThanEqual(BigDecimal averageRating);

    List<Beer> findByAverageRatingBetween(BigDecimal lowerLimit, BigDecimal upperLimit);

}

