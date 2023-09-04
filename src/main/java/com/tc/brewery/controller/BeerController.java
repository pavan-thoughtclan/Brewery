package com.tc.brewery.controller;
import com.tc.brewery.entity.Beer;
import com.tc.brewery.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class BeerController {
    @Autowired
    private BeerService beerService;

    @GetMapping("/beers/categories")
    @PreAuthorize("hasRole('USER')")
    public Set<String> getAllCategories() {
        return beerService.getAllCategories();
    }

    @GetMapping("/beers")
    @PreAuthorize("hasRole('USER')")
    public List<Beer> getAllBeers() {
        return beerService.getAllBeers();
    }

    @GetMapping("/beers/categories/{category}")
    @PreAuthorize("hasRole('USER')")
    public List<Beer> getBeersByCategory(@PathVariable String category) {
        category=category.replace("_"," ");
        return beerService.getBeersByCategory(category);
    }

    @GetMapping("/beers/categories/{category}/{beerId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getBeerByCategoryAndId(@PathVariable String category, @PathVariable int beerId) {
        category=category.replace("_"," ");
        Beer beer = beerService.getBeerByCategoryAndId(category, beerId);

        if (beer == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(beer);
        }
    }

    @GetMapping("/beers/{beerId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Beer> getBeerById(@PathVariable int beerId) {
        Beer beer = beerService.getBeerById(beerId);

        if (beer == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(beer);
    }


    @PostMapping("/add_beers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addBeer(@RequestBody Beer beer) {
        if (!beerService.isBeerNameUnique(beer.getName())) {
            return ResponseEntity.badRequest().body("Beer with the same name already exists.");
        }

        Beer addedBeer = beerService.addBeer(beer);
        return new ResponseEntity<>(addedBeer, HttpStatus.CREATED);
    }
}

