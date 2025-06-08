package com.ticketing.repository;

import com.ticketing.model.PoliceOfficer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PoliceOfficerRepository extends JpaRepository<PoliceOfficer, Long> {
    
    Optional<PoliceOfficer> findByServiceId(String serviceId);
    
    boolean existsByServiceId(String serviceId);
}