//package com.tc.brewery.entity;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import jakarta.persistence.*;
//import lombok.*;
//
//
//
//@Entity
//public class Address {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    @JsonIgnoreProperties("addressList")
//    private User user;
//    private String flat;
//    private String locality;
//    private String state;
//    private String city;
//
//    private String pincode;
//    private String landmark;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public String getFlat() {
//        return flat;
//    }
//
//    public void setFlat(String flat) {
//        this.flat = flat;
//    }
//
//    public String getLocality() {
//        return locality;
//    }
//
//    public void setLocality(String locality) {
//        this.locality = locality;
//    }
//
//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    public String getPincode() {
//        return pincode;
//    }
//
//    public void setPincode(String pincode) {
//        this.pincode = pincode;
//    }
//
//    public String getLandmark() {
//        return landmark;
//    }
//
//    public void setLandmark(String landmark) {
//        this.landmark = landmark;
//    }
//    // Other fields, getters/setters...
//
//
//    @Override
//    public String toString() {
//        return "Address{" +
//                "id=" + id +
////                ", user=" + user +
//                ", flat='" + flat + '\'' +
//                ", locality='" + locality + '\'' +
//                ", state='" + state + '\'' +
//                ", city='" + city + '\'' +
//                ", pincode='" + pincode + '\'' +
//                ", landmark='" + landmark + '\'' +
//                '}';
//    }
//
//    public Address(Long id, User user, String flat, String locality, String state, String city, String pincode, String landmark) {
//        this.id = id;
//        this.user = user;
//        this.flat = flat;
//        this.locality = locality;
//        this.state = state;
//        this.city = city;
//        this.pincode = pincode;
//        this.landmark = landmark;
//    }
//    public Address() {
//
//    }
//}

package com.tc.brewery.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;



@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnoreProperties("addressList")
    private User user;
    private String address;
    private String lat;
    private String lng;
    public Address() {

    }

    public Address(Long id, User user, String address, String lat, String lng) {
        this.id = id;
        this.user = user;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore // used to get only the address part
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
//                ", user=" + user +
                ", address='" + address + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                '}';
    }
}
