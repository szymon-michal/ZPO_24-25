package com.ticketing.service;

import com.ticketing.model.Offense;
import com.ticketing.repository.OffenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OffenseService {
    
    private final OffenseRepository offenseRepository;
    
    @Transactional(readOnly = true)
    public List<Offense> findAll() {
        return offenseRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Offense> findById(Long id) {
        return offenseRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Offense> findByCode(String code) {
        return offenseRepository.findByCode(code);
    }
    
    @Transactional
    public Offense save(Offense offense) {
        return offenseRepository.save(offense);
    }
    
    @Transactional
    public void deleteById(Long id) {
        offenseRepository.deleteById(id);
    }
}