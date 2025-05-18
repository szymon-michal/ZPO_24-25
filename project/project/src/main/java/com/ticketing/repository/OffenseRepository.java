package com.ticketing.repository;

import com.ticketing.model.Offense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OffenseRepository extends JpaRepository<Offense, Long> {
    
    Optional<Offense> findByCode(String code);
}