package com.tc.brewery.controller;

import com.tc.brewery.entity.Cart;
import com.tc.brewery.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;


    @GetMapping("/get_cart/{userId}")
    public ResponseEntity<List<Cart>> getCartsByUserId(@PathVariable Long userId) {
        List<Cart> carts = cartService.getCartsByUserId(userId);
        if (carts == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carts);
    }



    @PostMapping("/add_cart/{userId}")
    public ResponseEntity<String> addCart(@PathVariable Long userId, @RequestBody Map<String, Object> cartDetails) {
        Long cartId = cartService.addCart(userId, cartDetails); // Get the cart ID
        if (cartId != null && cartId != -1) {
            return ResponseEntity.ok("{\"message\":\"Cart added successfully\",\"cartId\":" + cartId + "}");
        } else {
            return ResponseEntity.badRequest().body("{\"message\":\"Failed to add cart\"}");
        }
    }

//    @PostMapping("/add_cart/{userId}")
//    public ResponseEntity<Void> addCart(@PathVariable Long userId, @RequestBody Map<String, Object> cartDetails) {
//        boolean added = cartService.addCart(userId, cartDetails);
//        if (added) {
//            return ResponseEntity.ok().build(); // Status code 200 without a response body
//        } else {
//            return ResponseEntity.badRequest().build(); // Status code 400 without a response body
//        }
//    }
}


