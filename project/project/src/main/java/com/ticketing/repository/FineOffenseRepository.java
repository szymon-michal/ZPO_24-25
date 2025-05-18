package com.ticketing.repository;

import com.ticketing.model.FineOffense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineOffenseRepository extends JpaRepository<FineOffense, Long> {
    
    List<FineOffense> findByFineId(Long fineId);
    
    @Query("SELECT COUNT(fo) > 0 FROM FineOffense fo " +
           "WHERE fo.offense.id = :offenseId AND fo.fine.driver.id = :driverId AND fo.fine.status = 'ACCEPTED'")
    boolean hasDriverCommittedOffenseBefore(Long driverId, Long offenseId);
}