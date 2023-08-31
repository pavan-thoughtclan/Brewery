package com.tc.brewery.repository;

import com.tc.brewery.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    // You can define custom query methods here if needed
}
