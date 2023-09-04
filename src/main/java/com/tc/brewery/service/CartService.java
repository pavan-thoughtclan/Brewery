package com.tc.brewery.service;

import com.tc.brewery.entity.*;
import com.tc.brewery.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        return cartRepository.findCartsByUserIdOrderByTimestampDesc(userId);
    }
    @Transactional
    public Long addCart(Long userId, Map<String, Object> cartDetails) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        Double totalAmount = Double.valueOf(cartDetails.get("totalAmount").toString());
        String modeOfPayment = cartDetails.get("modeOfPayment").toString();
        String modeOfDelivery = cartDetails.get("modeOfDelivery").toString();
        String  status = cartDetails.get("status").toString();
        String address = cartDetails.get("address").toString();
        BigDecimal lat = new BigDecimal(cartDetails.get("lat").toString());
        BigDecimal lng = new BigDecimal(cartDetails.get("lng").toString());
        List<Map<String, Object>> cartItemsList = (List<Map<String, Object>>) cartDetails.get("cartItems");

        ModeOfPayment paymentMode;
        try {
            paymentMode = ModeOfPayment.valueOf(modeOfPayment);
        } catch (IllegalArgumentException e) {
            return null;
        }

        ModeOfDelivery deliveryMode;
        try {
            deliveryMode = ModeOfDelivery.valueOf(modeOfDelivery);
        } catch (IllegalArgumentException e) {
            return null;
        }

        Cart newCart = new Cart();
        newCart.setUser(user);
        newCart.setModeOfPayment(paymentMode);
        newCart.setModeOfDelivery(deliveryMode);
        newCart.setTotalAmount(totalAmount);
        newCart.setStatus(status);
        newCart.setAddress(address);
        newCart.setLat(lat);
        newCart.setLng(lng);
        newCart.setTimestamp(LocalDateTime.now());

        List<CartItem> cartItems = new ArrayList<>();
        for (Map<String, Object> itemMap : cartItemsList) {
            CartItem cartItem = new CartItem();
            cartItem.setCart(newCart);
            if (itemMap.containsKey("beerId")) {

                Integer beerId = ((Number) itemMap.get("beerId")).intValue();
                Beer beer = beerRepository.findById(beerId).orElse(null);
                if (beer == null) {
                    return null;
                }
                cartItem.setBeer(beer);
                cartItem.setBeerQuantity(Integer.valueOf(itemMap.get("beerQuantity").toString()));
                cartItem.setBeerVolumeInMl(Double.valueOf(itemMap.get("beerVolumeInMl").toString()));
                cartItem.setBeerAmount(Double.valueOf(itemMap.get("beerAmount").toString()));
                cartItem.setAmountOfEachBeer(Double.valueOf(itemMap.get("amountOfEachBeer").toString()));

            }
            else if (itemMap.containsKey("foodId")) {
                Integer foodId = ((Number) itemMap.get("foodId")).intValue();
                Food food = foodRepository.findById(foodId).orElse(null);
                if (food == null) {
                    return null;
                }
                cartItem.setFood(food);
                cartItem.setFoodQuantity(Integer.valueOf(itemMap.get("foodQuantity").toString()));
                cartItem.setFoodAmount(Double.valueOf(itemMap.get("foodAmount").toString()));
                cartItem.setAmountOfEachFood(Double.valueOf(itemMap.get("amountOfEachFood").toString()));

            }
            cartItems.add(cartItem);
        }
        newCart.setCartItems(cartItems);
        user.getCartList().add(newCart);
        userRepository.save(user);
        Address existingAddress = addressRepository.findByUserAndAddress(user, address);
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

        return cartId;
    }
    public List<Cart> getNotDeliveredCartsByUserId(Long userId) {
        return cartRepository.findAllByUserIdAndStatus(userId, "NOT DELIVERED");
    }

    public boolean updateCartStatus(Long cartId, String newStatus) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            cart.setStatus(newStatus);
            cartRepository.save(cart);
            return true;
        }
        return false;
    }
    public List<Cart> getAllCartsWithUserId() {
        List<Cart> carts = cartRepository.findAll();

        for (Cart cart : carts) {
            cart.setUser_id(cart.getUser().getId());
        }

        return carts;
    }

}



