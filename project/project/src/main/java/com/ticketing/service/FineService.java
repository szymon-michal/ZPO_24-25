package com.ticketing.service;

import com.ticketing.dto.FineCreationDTO;
import com.ticketing.dto.FineOffenseDTO;
import com.ticketing.model.*;
import com.ticketing.repository.FineOffenseRepository;
import com.ticketing.repository.FineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FineService {
    
    private final FineRepository fineRepository;
    private final FineOffenseRepository fineOffenseRepository;
    private final PoliceOfficerService policeOfficerService;
    private final DriverService driverService;
    private final OffenseService offenseService;
    
    @Transactional(readOnly = true)
    public List<Fine> findAll() {
        return fineRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Fine> findById(Long id) {
        return fineRepository.findByIdWithOffenses(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Fine> findByFineNumber(String fineNumber) {
        return fineRepository.findByFineNumberWithOffenses(fineNumber);
    }
    
    @Transactional(readOnly = true)
    public List<Fine> findByDriverPesel(String pesel) {
        return fineRepository.findByDriverPesel(pesel);
    }
    
    @Transactional(readOnly = true)
    public List<Fine> findByPoliceOfficerServiceId(String serviceId) {
        return fineRepository.findByPoliceOfficerServiceId(serviceId);
    }
    
    @Transactional
    public Fine issueFine(FineCreationDTO fineCreationDTO) {
        // Retrieve the police officer and driver
        PoliceOfficer policeOfficer = policeOfficerService.findByServiceId(fineCreationDTO.getPoliceOfficerServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Police officer not found"));
        
        Driver driver = driverService.findByPesel(fineCreationDTO.getDriverPesel())
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        
        // Create a new fine
        Fine fine = new Fine();
        fine.setFineNumber(generateFineNumber());
        fine.setPoliceOfficer(policeOfficer);
        fine.setDriver(driver);
        fine.setIssueDate(LocalDateTime.now());
        fine.setLocation(fineCreationDTO.getLocation());
        fine.setStatus(Fine.FineStatus.ISSUED);
        
        // Calculate totals and add offenses
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalPoints = 0;
        List<FineOffense> fineOffenses = new ArrayList<>();
        
        for (FineOffenseDTO offenseDTO : fineCreationDTO.getOffenses()) {
            Offense offense = offenseService.findById(offenseDTO.getOffenseId())
                    .orElseThrow(() -> new IllegalArgumentException("Offense not found"));
            
            boolean isRepeatOffense = false;
            if (offense.getRepeatableOffense()) {
                isRepeatOffense = fineOffenseRepository.hasDriverCommittedOffenseBefore(driver.getId(), offense.getId());
            }
            
            // Calculate the amount for this offense
            BigDecimal amount = offenseDTO.getAmount();
            if (isRepeatOffense) {
                amount = amount.multiply(BigDecimal.valueOf(2)); // Double for repeat offenses
            }
            
            // Create the fine-offense relationship
            FineOffense fineOffense = new FineOffense();
            fineOffense.setFine(fine);
            fineOffense.setOffense(offense);
            fineOffense.setAmount(amount);
            fineOffense.setPoints(offense.getPenaltyPoints());
            fineOffense.setIsRepeat(isRepeatOffense);
            
            fineOffenses.add(fineOffense);
            
            // Add to totals
            totalAmount = totalAmount.add(amount);
            totalPoints += offense.getPenaltyPoints();
        }
        
        fine.setTotalAmount(totalAmount);
        fine.setTotalPoints(totalPoints);
        
        // Save the fine first
        Fine savedFine = fineRepository.save(fine);
        
        // Set the fine reference in each offense and save them
        fineOffenses.forEach(fineOffense -> {
            fineOffense.setFine(savedFine);
            fineOffenseRepository.save(fineOffense);
        });
        
        savedFine.setFineOffenses(fineOffenses);
        return savedFine;
    }
    
    @Transactional
    public Fine acceptFine(String fineNumber) {
        Fine fine = fineRepository.findByFineNumber(fineNumber)
                .orElseThrow(() -> new IllegalArgumentException("Fine not found"));
        
        if (fine.getStatus() != Fine.FineStatus.ISSUED) {
            throw new IllegalStateException("Fine cannot be accepted. Current status: " + fine.getStatus());
        }
        
        fine.setStatus(Fine.FineStatus.ACCEPTED);
        fine.setAcceptanceDate(LocalDateTime.now());
        
        return fineRepository.save(fine);
    }
    
    @Transactional
    public Fine cancelFine(String fineNumber, String reason) {
        Fine fine = fineRepository.findByFineNumber(fineNumber)
                .orElseThrow(() -> new IllegalArgumentException("Fine not found"));
        
        if (fine.getStatus() == Fine.FineStatus.CANCELED) {
            throw new IllegalStateException("Fine is already cancelled");
        }
        
        fine.setStatus(Fine.FineStatus.CANCELED);
        fine.setCancellationDate(LocalDateTime.now());
        fine.setCancellationReason(reason);
        
        return fineRepository.save(fine);
    }
    
    private String generateFineNumber() {
        // Format: "FINE-YYYYMMDD-XXXX" where XXXX is a sequential number
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = fineRepository.count() + 1;
        return String.format("FINE-%s-%04d", datePart, count);
    }
}