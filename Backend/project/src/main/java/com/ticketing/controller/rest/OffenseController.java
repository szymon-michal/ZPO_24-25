package com.ticketing.controller.rest;

import com.ticketing.model.Offense;
import com.ticketing.service.OffenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/violations")
@RequiredArgsConstructor
public class OffenseController {
    
    private final OffenseService offenseService;
    
    @GetMapping//działa
    public ResponseEntity<List<Offense>> getAllOffenses() {
        return ResponseEntity.ok(offenseService.findAll());
    }
    
    @PostMapping("/new")//działa
    public ResponseEntity<Offense> createOffense(@RequestBody Offense offense) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(offenseService.save(offense));
    }
    
    @GetMapping("/{id}")//działa
    public ResponseEntity<Offense> getOffense(@PathVariable Long id) { //kolejna dzika rekurencja
        return offenseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")//raczej dziala ale jeszcze nie 100%
    public ResponseEntity<Offense> updateOffense(@PathVariable Long id, @RequestBody Offense offense) {
        if (!offenseService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        offense.setId(id);
        return ResponseEntity.ok(offenseService.save(offense));
    }
}