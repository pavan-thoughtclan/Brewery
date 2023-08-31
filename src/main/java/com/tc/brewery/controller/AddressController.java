package com.tc.brewery.controller;

import com.tc.brewery.entity.Address;
import com.tc.brewery.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping("/add_address/{userId}")
    public ResponseEntity<Void> createAddressForUser(
            @PathVariable Long userId,
            @RequestBody Address address
    ) {
        return addressService.createAddressForUser(userId, address);
    }

    @GetMapping("/address/{user_id}")
    public List<Address> getAddressesByUserId(@PathVariable("user_id") Long userId) {
        return addressService.getAddressesByUserId(userId);
    }

    @DeleteMapping("/delete_addresse/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        if (addressService.deleteAddressById(addressId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
