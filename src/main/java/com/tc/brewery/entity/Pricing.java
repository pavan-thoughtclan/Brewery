package com.tc.brewery.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "beer_id", referencedColumnName = "id") // Match the column names
    private Beer beer; // Add this field

    //    @Column(name = "beer_id") // Map to the snake_case column name
//    private Long beer_id;
    private int size_ml;
    private BigDecimal price;

    private String image_url;

    public Pricing() {
    }


    public Pricing(int id, Beer beer, int size_ml, BigDecimal price, String image_url) {
        this.id = id;
        this.beer = beer;
        this.size_ml = size_ml;
        this.price = price;
        this.image_url = image_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize_ml() {
        return size_ml;
    }

    public void setSize_ml(int size_ml) {
        this.size_ml = size_ml;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
