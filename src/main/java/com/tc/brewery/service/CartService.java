package com.tc.brewery.service;

import com.tc.brewery.entity.*;
import com.tc.brewery.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private FoodRepository foodRepository;
    public List<Cart> getCartsByUserId(Long userId) {
        return cartRepository.findCartsByUserId(userId);
    }

//    @Transactional
//    public boolean addCart(Long userId, Map<String, Object> cartDetails) {
//        User user = userRepository.findById(userId).orElse(null);
//        if (user == null) {
//            return false; // User not found
//        }
//
//        // Extract cart properties from cartDetails
//        Double totalAmount = Double.valueOf(cartDetails.get("totalAmount").toString());
//        String modeOfPayment = cartDetails.get("modeOfPayment").toString();
//        String modeOfDelivery = cartDetails.get("modeOfDelivery").toString();
//        String address = cartDetails.get("address").toString();
//        String lat = cartDetails.get("lat").toString();
//        String lng = cartDetails.get("lng").toString();
//        List<Map<String, Object>> cartItemsList = (List<Map<String, Object>>) cartDetails.get("cartItems");
//
//        // Validate and set ModeOfPayment
//        ModeOfPayment paymentMode;
//        try {
//            paymentMode = ModeOfPayment.valueOf(modeOfPayment);
//        } catch (IllegalArgumentException e) {
//            return false; // Invalid payment mode
//        }
//
//        // Validate and set ModeOfDelivery
//        ModeOfDelivery deliveryMode;
//        try {
//            deliveryMode = ModeOfDelivery.valueOf(modeOfDelivery);
//        } catch (IllegalArgumentException e) {
//            return false; // Invalid delivery mode
//        }
//
//        // Create a new Cart
//        Cart newCart = new Cart();
//        newCart.setUser(user);
//        newCart.setModeOfPayment(paymentMode);
//        newCart.setModeOfDelivery(deliveryMode);
//        newCart.setTotalAmount(totalAmount);
//        newCart.setAddress(address);
//        newCart.setLat(lat);
//        newCart.setLng(lng);
//        newCart.setTimestamp(LocalDateTime.now()); // Set the timestamp
//
//        // Create and add CartItems to the new Cart
//        List<CartItem> cartItems = new ArrayList<>();
//        for (Map<String, Object> itemMap : cartItemsList) {
//            CartItem cartItem = new CartItem();
//            cartItem.setCart(newCart); // Set the relationship
//            if (itemMap.containsKey("beerId")) {
//
//                // Set the beer_id
//                Integer beerId = ((Number) itemMap.get("beerId")).intValue();
//                Beer beer = beerRepository.findById(beerId).orElse(null);
//                if (beer == null) {
//                    return false; // Beer not found
//                }
//                cartItem.setBeer(beer);
//
//                // Set other attributes
//                cartItem.setBeerQuantity(Integer.valueOf(itemMap.get("beerQuantity").toString()));
//                cartItem.setBeerVolumeInMl(Double.valueOf(itemMap.get("beerVolumeInMl").toString()));
//                cartItem.setBeerAmount(Double.valueOf(itemMap.get("beerAmount").toString()));
//                cartItem.setAmountOfEachBeer(Double.valueOf(itemMap.get("amountOfEachBeer").toString()));
//
//            } else if (itemMap.containsKey("foodId")) {
//                // Set the beer_id
//                Integer foodId = ((Number) itemMap.get("foodId")).intValue();
//                Food food = foodRepository.findById(foodId).orElse(null);
//                if (food == null) {
//                    return false; // Beer not found
//                }
//                cartItem.setFood(food);
//
//                // Set other attributes
//                cartItem.setFoodQuantity(Integer.valueOf(itemMap.get("foodQuantity").toString()));
//                cartItem.setFoodAmount(Double.valueOf(itemMap.get("foodAmount").toString()));
//                cartItem.setAmountOfEachFood(Double.valueOf(itemMap.get("amountOfEachFood").toString()));
//
//            }
//            cartItems.add(cartItem);
//        }
//            newCart.setCartItems(cartItems);
//
//        // Add the new Cart to the user's cart list
//        user.getCartList().add(newCart);
//
//        // Save the updated user with the new Cart
//        userRepository.save(user);
//        Address existingAddress = addressRepository.findByUserAndAddressAndLatAndLng(user, address, lat, lng);
//        if (existingAddress == null) {
//            Address newAddress = new Address();
//            newAddress.setUser(user);
//            newAddress.setAddress(address);
//            newAddress.setLat(lat);
//            newAddress.setLng(lng);
//            addressRepository.save(newAddress);
//        }
//        return true;
//    }

    @Transactional
    public Long addCart(Long userId, Map<String, Object> cartDetails) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null; // User not found
        }

        // Extract cart properties from cartDetails
        Double totalAmount = Double.valueOf(cartDetails.get("totalAmount").toString());
        String modeOfPayment = cartDetails.get("modeOfPayment").toString();
        String modeOfDelivery = cartDetails.get("modeOfDelivery").toString();
        String address = cartDetails.get("address").toString();
        String lat = cartDetails.get("lat").toString();
        String lng = cartDetails.get("lng").toString();
        List<Map<String, Object>> cartItemsList = (List<Map<String, Object>>) cartDetails.get("cartItems");

        // Validate and set ModeOfPayment
        ModeOfPayment paymentMode;
        try {
            paymentMode = ModeOfPayment.valueOf(modeOfPayment);
        } catch (IllegalArgumentException e) {
            return null; // Invalid payment mode
        }

        // Validate and set ModeOfDelivery
        ModeOfDelivery deliveryMode;
        try {
            deliveryMode = ModeOfDelivery.valueOf(modeOfDelivery);
        } catch (IllegalArgumentException e) {
            return null; // Invalid delivery mode
        }

        // Create a new Cart
        Cart newCart = new Cart();
        newCart.setUser(user);
        newCart.setModeOfPayment(paymentMode);
        newCart.setModeOfDelivery(deliveryMode);
        newCart.setTotalAmount(totalAmount);
        newCart.setAddress(address);
        newCart.setLat(lat);
        newCart.setLng(lng);
        newCart.setTimestamp(LocalDateTime.now()); // Set the timestamp

        // Create and add CartItems to the new Cart
        List<CartItem> cartItems = new ArrayList<>();
        for (Map<String, Object> itemMap : cartItemsList) {
            CartItem cartItem = new CartItem();
            cartItem.setCart(newCart); // Set the relationship
            if (itemMap.containsKey("beerId")) {

                // Set the beer_id
                Integer beerId = ((Number) itemMap.get("beerId")).intValue();
                Beer beer = beerRepository.findById(beerId).orElse(null);
                if (beer == null) {
                    return null; // Beer not found
                }
                cartItem.setBeer(beer);

                // Set other attributes
                cartItem.setBeerQuantity(Integer.valueOf(itemMap.get("beerQuantity").toString()));
                cartItem.setBeerVolumeInMl(Double.valueOf(itemMap.get("beerVolumeInMl").toString()));
                cartItem.setBeerAmount(Double.valueOf(itemMap.get("beerAmount").toString()));
                cartItem.setAmountOfEachBeer(Double.valueOf(itemMap.get("amountOfEachBeer").toString()));

            } else if (itemMap.containsKey("foodId")) {
                // Set the beer_id
                Integer foodId = ((Number) itemMap.get("foodId")).intValue();
                Food food = foodRepository.findById(foodId).orElse(null);
                if (food == null) {
                    return null; // Beer not found
                }
                cartItem.setFood(food);

                // Set other attributes
                cartItem.setFoodQuantity(Integer.valueOf(itemMap.get("foodQuantity").toString()));
                cartItem.setFoodAmount(Double.valueOf(itemMap.get("foodAmount").toString()));
                cartItem.setAmountOfEachFood(Double.valueOf(itemMap.get("amountOfEachFood").toString()));

            }
            cartItems.add(cartItem);
        }
        newCart.setCartItems(cartItems);

        // Add the new Cart to the user's cart list
        user.getCartList().add(newCart);

        // Save the updated user with the new Cart
        userRepository.save(user);
        Address existingAddress = addressRepository.findByUserAndAddressAndLatAndLng(user, address, lat, lng);
        if (existingAddress == null) {
            Address newAddress = new Address();
            newAddress.setUser(user);
            newAddress.setAddress(address);
            newAddress.setLat(lat);
            newAddress.setLng(lng);
            addressRepository.save(newAddress);
        }
        Long cartId = null;
        for (Cart cart : user.getCartList()) {
            if (cart.getTimestamp() != null && cart.getTimestamp().equals(newCart.getTimestamp())) {
                cartId = cart.getId();
                break;
            }
        }

        return cartId; // Return the cart ID or null
    }
}



