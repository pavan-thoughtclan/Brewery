package com.tc.brewery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long user_id;
    @ManyToOne
    @JsonIgnoreProperties("cartList")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ModeOfPayment modeOfPayment;

    @Enumerated(EnumType.STRING)
    private ModeOfDelivery modeOfDelivery;

    private Double totalAmount;
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    private String status;
    private String address;
    private String lat;
    private String lng;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("cart")
//    @JsonIgnore
    private List<CartItem> cartItems = new ArrayList<>();

    public Cart() {
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Cart(String status) {
        this.status = status;
    }

    public Cart(Long id, User user, ModeOfPayment modeOfPayment, ModeOfDelivery modeOfDelivery, Double totalAmount, LocalDateTime timestamp, String address, String lat, String lng, List<CartItem> cartItems) {
        this.id = id;
        this.user = user;
        this.modeOfPayment = modeOfPayment;
        this.modeOfDelivery = modeOfDelivery;
        this.totalAmount = totalAmount;
        this.timestamp = timestamp;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.cartItems = cartItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ModeOfPayment getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(ModeOfPayment modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public ModeOfDelivery getModeOfDelivery() {
        return modeOfDelivery;
    }

    public void setModeOfDelivery(ModeOfDelivery modeOfDelivery) {
        this.modeOfDelivery = modeOfDelivery;
    }

    //    @JsonIgnore
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }


    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", user=" + user +
                ", modeOfPayment=" + modeOfPayment +
                ", modeOfDelivery=" + modeOfDelivery +
                ", totalAmount=" + totalAmount +
                ", timestamp=" + timestamp +
                ", status='" + status + '\'' +
                ", address='" + address + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", cartItems=" + cartItems +
                '}';
    }
}
