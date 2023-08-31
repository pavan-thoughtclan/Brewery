package com.tc.brewery.service;

import com.tc.brewery.entity.Address;
import com.tc.brewery.entity.User;
import com.tc.brewery.repository.AddressRepository;
import com.tc.brewery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    public ResponseEntity<Void> createAddressForUser(Long userId, Address newAddress) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if the address already exists for the user
        Address existingAddress = addressRepository.findByUserAndAddressAndLatAndLng(
                user, newAddress.getAddress(), newAddress.getLat(), newAddress.getLng());

        if (existingAddress != null) {
            return ResponseEntity.badRequest().build();
        }

        newAddress.setUser(user);
        addressRepository.save(newAddress);
        return ResponseEntity.ok().build();
    }

    public List<Address> getAddressesByUserId(Long userId) {
        return addressRepository.findByUserId(userId);
    }


    public boolean deleteAddressById(Long addressId) {
        Address address = addressRepository.findAddressById(addressId);
        if (address != null) {
            addressRepository.delete(address);
            return true;
        }
        return false;
    }
}
