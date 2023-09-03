package com.tc.brewery.repository;


import com.tc.brewery.entity.Address;
import com.tc.brewery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
////    UserWithAddressDTO findUserWithAddressByUser_Id(@Param("userId") Long userId);
//    List<Address> findByUser(User user);

    List<Address> findByUserId(Long userId);
    @Query("SELECT a FROM Address a WHERE a.id = ?1")
    Address findAddressById(Long addressId);

    Address findByUserAndAddressAndLatAndLng(User user, String address, BigDecimal lat, BigDecimal lng);
    Address findByUserAndAddress(User user, String address);

}


