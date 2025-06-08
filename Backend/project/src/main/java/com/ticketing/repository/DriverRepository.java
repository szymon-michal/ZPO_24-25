package com.ticketing.repository;

import com.ticketing.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    
    Optional<Driver> findByPesel(String pesel);
    
    Optional<Driver> findByLicenseNumber(String licenseNumber);
    
    boolean existsByPesel(String pesel);
    
    boolean existsByLicenseNumber(String licenseNumber);
    
    @Query("SELECT SUM(f.totalPoints) FROM Fine f WHERE f.driver.pesel = :pesel AND f.status = 'ACCEPTED'")
    Integer getTotalPenaltyPointsByPesel(String pesel);
}