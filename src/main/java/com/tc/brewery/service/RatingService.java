package com.tc.brewery.service;

import com.tc.brewery.entity.Beer;
import com.tc.brewery.entity.Food;
import com.tc.brewery.entity.Rating;
import com.tc.brewery.entity.User;
import com.tc.brewery.repository.BeerRepository;
import com.tc.brewery.repository.FoodRepository;
import com.tc.brewery.repository.RatingRepository;
import com.tc.brewery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private UserRepository userRepository;

    private static final BigDecimal MIN_RATING = new BigDecimal("4.5");

    public ResponseEntity<Void> addBeerRating(int beerId, long userId, BigDecimal ratingValue, String review) {
        Beer beer = beerRepository.findById(beerId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (beer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Rating rating = new Rating();
        rating.setBeer(beer);
        rating.setUser(user);
        rating.setRating(ratingValue);
        rating.setReview(review);

        ratingRepository.save(rating);

        updateAverageRatingsForAllBeers();

        return ResponseEntity.ok().build();
    }

    private void updateAverageRatingsForAllBeers() {
        List<Beer> allBeers = beerRepository.findAll();
        for (Beer beer : allBeers) {
            calculateAverageRatingBeer(beer);
            beerRepository.save(beer);
        }
    }

    private void calculateAverageRatingBeer(Beer beer) {
        if (beer.getRatings() != null && !beer.getRatings().isEmpty()) {
            BigDecimal totalRating = BigDecimal.ZERO;
            for (Rating rating : beer.getRatings()) {
                totalRating = totalRating.add(rating.getRating());
            }
            beer.setAverageRating(totalRating.divide(BigDecimal.valueOf(beer.getRatings().size()), 2, RoundingMode.HALF_UP));
        }
        else {
            beer.setAverageRating(null);
        }
    }


    public ResponseEntity<Void> addFoodRating(int foodId, long userId, BigDecimal ratingValue, String review) {
        Food food = foodRepository.findById(foodId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (food == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Rating rating = new Rating();
        rating.setFood(food);
        rating.setUser(user);
        rating.setRating(ratingValue);
        rating.setReview(review);

        ratingRepository.save(rating);

        updateAverageRatingsForAllFoods();

        return ResponseEntity.ok().build();
    }

    private void updateAverageRatingsForAllFoods() {
        List<Food> allFoods = foodRepository.findAll();
        for (Food food : allFoods) {
            calculateAverageRatingFood(food);
            foodRepository.save(food);
        }
    }

    private void calculateAverageRatingFood(Food food) {
        if (food.getRatings() != null && !food.getRatings().isEmpty()) {
            BigDecimal totalRating = BigDecimal.ZERO;
            for (Rating rating : food.getRatings()) {
                totalRating = totalRating.add(rating.getRating());
            }
            food.setAverageRating(totalRating.divide(BigDecimal.valueOf(food.getRatings().size()), 2, RoundingMode.HALF_UP));
        } else {
            food.setAverageRating(null);
        }
    }

    public List<Beer> getBeersWithHighRatings() {
        return beerRepository.findByAverageRatingGreaterThanEqual(MIN_RATING);
    }

    public List<Beer> getBeersWithinManualRatingsRange() {
        BigDecimal lowerLimit = new BigDecimal("4.0");
        BigDecimal upperLimit = new BigDecimal("4.4");

        return beerRepository.findByAverageRatingBetween(lowerLimit, upperLimit);
    }

}

