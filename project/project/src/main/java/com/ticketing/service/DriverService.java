package com.ticketing.service;

import com.ticketing.model.Driver;
import com.ticketing.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverService {
    
    private final DriverRepository driverRepository;
    
    @Transactional(readOnly = true)
    public List<Driver> findAll() {
        return driverRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Driver> findById(Long id) {
        return driverRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Driver> findByPesel(String pesel) {
        return driverRepository.findByPesel(pesel);
    }
    
    @Transactional(readOnly = true)
    public Optional<Driver> findByLicenseNumber(String licenseNumber) {
        return driverRepository.findByLicenseNumber(licenseNumber);
    }
    
    @Transactional(readOnly = true)
    public boolean existsByPesel(String pesel) {
        return driverRepository.existsByPesel(pesel);
    }
    
    @Transactional(readOnly = true)
    public boolean existsByLicenseNumber(String licenseNumber) {
        return driverRepository.existsByLicenseNumber(licenseNumber);
    }
    
    @Transactional(readOnly = true)
    public Integer getTotalPenaltyPoints(String pesel) {
        return driverRepository.getTotalPenaltyPointsByPesel(pesel);
    }
    
    @Transactional
    public Driver save(Driver driver) {
        return driverRepository.save(driver);
    }
    
    @Transactional
    public void deleteById(Long id) {
        driverRepository.deleteById(id);
    }
}