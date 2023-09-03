package com.tc.brewery.controller;

import com.tc.brewery.entity.Beer;
import com.tc.brewery.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping("/beerratings/{beerId}/{userId}")
    public ResponseEntity<Void> addBeerRating(
            @PathVariable int beerId,
            @PathVariable int userId,
            @RequestBody Map<String, Object> ratingData) {

        String ratingValueStr = ratingData.get("ratingValue").toString();
        BigDecimal ratingValue = new BigDecimal(ratingValueStr);

        String review = (String) ratingData.get("review");

        return ratingService.addBeerRating(beerId, userId, ratingValue, review);
    }

    @PostMapping("/foodratings/{foodId}/{userId}")
    public ResponseEntity<Void> addFoodRating(
            @PathVariable int foodId,
            @PathVariable int userId,
            @RequestBody Map<String, Object> ratingData) {

        String ratingValueStr = ratingData.get("ratingValue").toString();
        BigDecimal ratingValue = new BigDecimal(ratingValueStr);

        String review = (String) ratingData.get("review");

        return ratingService.addFoodRating(foodId, userId, ratingValue, review);
    }

    @GetMapping("/beers/Highrated")
    public List<Beer> getHighRatedBeers() {
        return ratingService.getBeersWithHighRatings();
    }

    @GetMapping("/beers/Moderaterated")
    public List<Beer> getBeersWithinManualRatingsRange() {
        return ratingService.getBeersWithinManualRatingsRange();
    }

}
