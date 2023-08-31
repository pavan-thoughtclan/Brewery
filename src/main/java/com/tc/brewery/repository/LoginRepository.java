package com.tc.brewery.repository;

import com.tc.brewery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findByPhoneNumber(String phoneNumber);
}

