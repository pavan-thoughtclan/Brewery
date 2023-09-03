package com.tc.brewery.controller;

import com.tc.brewery.entity.Beer;
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
        Long cartId = cartService.addCart(userId, cartDetails);
        if (cartId != null && cartId != -1) {
            return ResponseEntity.ok("{\"message\":\"Cart added successfully\",\"cartId\":" + cartId + "}");
        } else {
            return ResponseEntity.badRequest().body("{\"message\":\"Failed to add cart\"}");
        }
    }

    @GetMapping("/get_current_cart/{userId}")
    public ResponseEntity<Cart> getCurrentCartByUserId(@PathVariable Long userId) {
        Cart latestCart = cartService.getLatestCartByUserId(userId);
        if (latestCart == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(latestCart);
    }

    @PatchMapping("/update_cart_status/{cartId}")
    public ResponseEntity<String> updateCartStatus(@PathVariable Long cartId, @RequestBody Map<String, Object> statusMap) {
        if (statusMap.containsKey("status")) {
            String newStatus = statusMap.get("status").toString();
            boolean updated = cartService.updateCartStatus(cartId, newStatus);
            if (updated) {
                return ResponseEntity.ok("{\"message\":\"Cart status updated successfully\"}");
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.badRequest().body("{\"message\":\"Invalid request body\"}");
        }
    }

    @GetMapping("/list_all_carts")
    public ResponseEntity<List<Cart>> listAllCarts() {
        List<Cart> carts = cartService.getAllCartsWithUserId();
        return ResponseEntity.ok(carts);
    }
}


