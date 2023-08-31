package com.tc.brewery.controller;

import com.tc.brewery.entity.User;
import com.tc.brewery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/userdetails/{userId}")
    public ResponseEntity<User> getUserDetails(@PathVariable Long userId) {
        User user = userService.getUserById(userId);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }


    @GetMapping("/userwithaddress/{userId}")
    public ResponseEntity<User> getUserWithAddressById(@PathVariable Long userId) {
        User user = userService.getUserWithAddressById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}
