package com.tc.brewery.entity;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnoreProperties("cartItems")
    @JoinColumn(name = "cart_id",referencedColumnName = "id") // Foreign key referencing Cart
    private Cart cart;

    @ManyToOne
//    @JsonIgnoreProperties("cartItems")
//    @JsonIgnore
    @JsonInclude(Include.NON_NULL)
    @JoinColumn(name = "beer_id",referencedColumnName = "id") // The foreign key column in CartItem table
    private Beer beer;
    @JsonInclude(Include.NON_NULL)
    private Integer beerQuantity;
    @JsonInclude(Include.NON_NULL)
    private Double beerVolumeInMl;
    @JsonInclude(Include.NON_NULL)
    private Double beerAmount;
    @JsonInclude(Include.NON_NULL)
    private Double amountOfEachBeer;

    @ManyToOne
//    @JsonIgnore
    @JsonInclude(Include.NON_NULL)
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private Food food;
    @JsonInclude(Include.NON_NULL)
    private Integer foodQuantity;
    @JsonInclude(Include.NON_NULL)
    private Double foodAmount;
    @JsonInclude(Include.NON_NULL)
    private Double amountOfEachFood;

    public CartItem() {
    }

    public CartItem(Long id, Cart cart, Beer beer, Integer beerQuantity, Double beerVolumeInMl, Double beerAmount, Double amountOfEachBeer, Food food, Integer foodQuantity, Double foodAmount, Double amountOfEachFood) {
        this.id = id;
        this.cart = cart;
        this.beer = beer;
        this.beerQuantity = beerQuantity;
        this.beerVolumeInMl = beerVolumeInMl;
        this.beerAmount = beerAmount;
        this.amountOfEachBeer = amountOfEachBeer;
        this.food = food;
        this.foodQuantity = foodQuantity;
        this.foodAmount = foodAmount;
        this.amountOfEachFood = amountOfEachFood;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Integer getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(Integer foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public Double getFoodAmount() {
        return foodAmount;
    }

    public void setFoodAmount(Double foodAmount) {
        this.foodAmount = foodAmount;
    }

    public Double getAmountOfEachFood() {
        return amountOfEachFood;
    }

    public void setAmountOfEachFood(Double amountOfEachFood) {
        this.amountOfEachFood = amountOfEachFood;
    }

    @JsonGetter("beer")
    public Object getBeerInfo() {
        if (beer != null) {
            return new ItemInfo(beer.getId(), beer.getName());
        }
        return null;
    }

    @JsonGetter("food")
    public Object getFoodInfo() {
        if (food != null) {
            return new ItemInfo(food.getId(), food.getFood_name());
        }
        return null;
    }
    private static class ItemInfo {
        private int id;
        private String name;

        public ItemInfo(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        // Getters for id and name
    }
    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", cart=" + cart +
                ", beer=" + beer +
                ", beerQuantity=" + beerQuantity +
                ", beerVolumeInMl=" + beerVolumeInMl +
                ", beerAmount=" + beerAmount +
                ", amountOfEachBeer=" + amountOfEachBeer +
                ", food=" + food +
                ", foodQuantity=" + foodQuantity +
                ", foodAmount=" + foodAmount +
                ", amountOfEachFood=" + amountOfEachFood +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }


    public Integer getBeerQuantity() {
        return beerQuantity;
    }

    public void setBeerQuantity(Integer beerQuantity) {
        this.beerQuantity = beerQuantity;
    }

    public Double getBeerVolumeInMl() {
        return beerVolumeInMl;
    }

    public void setBeerVolumeInMl(Double beerVolumeInMl) {
        this.beerVolumeInMl = beerVolumeInMl;
    }

    public Double getBeerAmount() {
        return beerAmount;
    }

    public void setBeerAmount(Double beerAmount) {
        this.beerAmount = beerAmount;
    }

    public Double getAmountOfEachBeer() {
        return amountOfEachBeer;
    }

    public void setAmountOfEachBeer(Double amountOfEachBeer) {
        this.amountOfEachBeer = amountOfEachBeer;
    }
}