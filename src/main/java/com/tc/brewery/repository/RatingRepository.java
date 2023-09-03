package com.tc.brewery.repository;

import com.tc.brewery.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByBeerIdAndUserId(int beerId, int userId);
}
