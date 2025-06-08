package com.ticketing.repository;

import com.ticketing.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    
    Optional<Fine> findByFineNumber(String fineNumber);
    
    List<Fine> findByDriverPesel(String pesel);
    
    List<Fine> findByPoliceOfficerServiceId(String serviceId);
    
    @Query("SELECT DISTINCT f FROM Fine f JOIN FETCH f.fineOffenses fo JOIN FETCH fo.offense WHERE f.id = :id")
    Optional<Fine> findByIdWithOffenses(Long id);
    
    @Query("SELECT DISTINCT f FROM Fine f JOIN FETCH f.fineOffenses fo JOIN FETCH fo.offense WHERE f.fineNumber = :fineNumber")
    Optional<Fine> findByFineNumberWithOffenses(String fineNumber);
}