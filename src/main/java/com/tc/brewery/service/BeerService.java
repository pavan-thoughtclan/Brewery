package com.tc.brewery.service;

import com.tc.brewery.entity.Beer;
import com.tc.brewery.entity.Pricing;
import com.tc.brewery.entity.Rating;
import com.tc.brewery.entity.User;
import com.tc.brewery.repository.BeerRepository;
import com.tc.brewery.repository.PricingRepository;
import com.tc.brewery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BeerService {
    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PricingRepository pricingRepository;

    public Set<String> getAllCategories() {
        List<Beer> beers = beerRepository.findAll();
        Set<String> categories = new HashSet<>();
        for (Beer beer : beers) {
            categories.add(beer.getCategory());
        }
        return categories;
    }


    public List<Beer> getAllBeers() {
        return beerRepository.findAll();
    }


    public List<Beer> getBeersByCategory(String category) {
        return beerRepository.findByCategory(category);
    }


    public Beer getBeerByCategoryAndId(String category, int beerId) {
        return beerRepository.findByIdAndCategory(beerId, category);
    }

    public Beer getBeerById(int beerId) {
        return beerRepository.findById(beerId).orElse(null);
    }


    public Beer addBeer(Beer beer) {
        // Add any additional validation or logic here

        // Save the Beer entity to get its ID
        Beer savedBeer = beerRepository.save(beer);

        // Iterate through the pricings list and set the beer_id for each pricing entry
        for (Pricing pricing : beer.getPricings()) {
            pricing.setBeer(savedBeer);
            // Set other pricing properties as needed
            // Save the Pricing entity
            pricingRepository.save(pricing);
        }

        return savedBeer;
    }



    public boolean isBeerNameUnique(String name) {
        // Check if a beer with the given name already exists
        return beerRepository.findByName(name) == null;
    }
}
