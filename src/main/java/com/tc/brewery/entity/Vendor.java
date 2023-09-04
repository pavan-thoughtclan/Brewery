package com.tc.brewery.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;

    private String emailId;
    @Column(precision = 9, scale = 7) // Adjust precision and scale as needed
    private BigDecimal lat;
    @Column(precision = 9, scale = 7) // Adjust precision and scale as needed
    private BigDecimal lng;
    private String contactNumber;

    @Column(columnDefinition = "TEXT")
    private String companyDescription;

    public Vendor() {
    }

    public Vendor(Long id, String name, String address, String emailId, BigDecimal lat, BigDecimal lng, String contactNumber, String companyDescription) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.emailId = emailId;
        this.lat = lat;
        this.lng = lng;
        this.contactNumber = contactNumber;
        this.companyDescription = companyDescription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }
}

