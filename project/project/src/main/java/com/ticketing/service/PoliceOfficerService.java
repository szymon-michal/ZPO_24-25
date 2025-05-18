package com.ticketing.service;

import com.ticketing.model.PoliceOfficer;
import com.ticketing.repository.PoliceOfficerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PoliceOfficerService {
    
    private final PoliceOfficerRepository policeOfficerRepository;
    
    @Transactional(readOnly = true)
    public List<PoliceOfficer> findAll() {
        return policeOfficerRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<PoliceOfficer> findById(Long id) {
        return policeOfficerRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<PoliceOfficer> findByServiceId(String serviceId) {
        return policeOfficerRepository.findByServiceId(serviceId);
    }
    
    @Transactional(readOnly = true)
    public boolean existsByServiceId(String serviceId) {
        return policeOfficerRepository.existsByServiceId(serviceId);
    }
    
    @Transactional
    public PoliceOfficer save(PoliceOfficer policeOfficer) {
        return policeOfficerRepository.save(policeOfficer);
    }
    
    @Transactional
    public void deleteById(Long id) {
        policeOfficerRepository.deleteById(id);
    }
}