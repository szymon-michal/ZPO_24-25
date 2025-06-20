package com.ticketing.controller.rest;

import com.ticketing.model.PoliceOfficer;
import com.ticketing.service.PoliceOfficerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/officers")
@RequiredArgsConstructor
public class OfficerController {
    
    private final PoliceOfficerService policeOfficerService;
    
    @PostMapping//działa
    public ResponseEntity<PoliceOfficer> createOfficer(@RequestBody PoliceOfficer officer) {
        if (policeOfficerService.existsByServiceId(officer.getServiceId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(policeOfficerService.save(officer));
    }
    @GetMapping("/id/{id}")//działa
    public ResponseEntity<PoliceOfficer> getOfficerById(@PathVariable Long id) {
        return policeOfficerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/{badgeNumber}") //działa
    public ResponseEntity<PoliceOfficer> getOfficer(@PathVariable String badgeNumber) {//dzika rekurencja
        return policeOfficerService.findByServiceId(badgeNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}