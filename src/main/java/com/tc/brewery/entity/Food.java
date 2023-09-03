package com.tc.brewery.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String food_name;
    private String category;
    private String image_url;
    private BigDecimal food_price;
    private String description;
    private String calories;
    @OneToMany(mappedBy = "food",cascade= CascadeType.ALL)
    @JsonIgnoreProperties("food")
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "food",cascade= CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Rating> ratings = new ArrayList<>();
    @Column(precision = 3, scale = 1)
    private BigDecimal averageRating;

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public Food() {
    }

    public Food(int id, String food_name, String category, String image_url, BigDecimal food_price, String description, String calories, BigDecimal averageRating) {
        this.id = id;
        this.food_name = food_name;
        this.category = category;
        this.image_url = image_url;
        this.food_price = food_price;
        this.description = description;
        this.calories = calories;
        this.averageRating = averageRating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public BigDecimal getFood_price() {
        return food_price;
    }

    public void setFood_price(BigDecimal food_price) {
        this.food_price = food_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    @JsonGetter("ratings")
    public List<Map<String, Object>> getRatingsInfo() {

        List<Map<String, Object>> ratingsInfo = new ArrayList<>();
        for (Rating rating : ratings) {
            Map<String, Object> info = new HashMap<>();
            info.put("id", rating.getId());
            info.put("rating", rating.getRating());
            info.put("review", rating.getReview());
            info.put("user", getUserInfo(rating.getUser()));
            ratingsInfo.add(info);
        }
        return ratingsInfo;
    }

    private Map<String, Object> getUserInfo(User user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("firstName", user.getFirstName());
        userInfo.put("lastName", user.getLastName());
        return userInfo;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", food_name='" + food_name + '\'' +
                ", category='" + category + '\'' +
                ", image_url='" + image_url + '\'' +
                ", food_price=" + food_price +
                ", description='" + description + '\'' +
                ", calories='" + calories + '\'' +
//                ", cartItems=" + cartItems +
                ", ratings=" + ratings +
                ", averageRating=" + averageRating +
                '}';
    }
}

