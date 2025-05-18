package com.ticketing.controller.rest;

import com.ticketing.model.Driver;
import com.ticketing.model.Fine;
import com.ticketing.service.DriverService;
import com.ticketing.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {
    
    private final DriverService driverService;
    private final FineService fineService;
    
    @PostMapping
    public ResponseEntity<Driver> createDriver(@RequestBody Driver driver) {//TOTEST
        if (driverService.existsByPesel(driver.getPesel())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(driverService.save(driver));
    }
    
    @GetMapping("/{pesel}")
    public ResponseEntity<Driver> getDriver(@PathVariable String pesel) {// niedziała jaks dzika rekurencja wchodzi xd
        return driverService.findByPesel(pesel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{pesel}/points")
    public ResponseEntity<Integer> getDriverPoints(@PathVariable String pesel) { // dziala
        Integer points = driverService.getTotalPenaltyPoints(pesel);
        return points != null ? ResponseEntity.ok(points) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{pesel}/tickets")
    public ResponseEntity<List<Fine>> getDriverTickets( //kolejna dzika rekurencja
            @PathVariable String pesel,
            @RequestParam(required = false) Fine.FineStatus status) {
        List<Fine> fines = fineService.findByDriverPesel(pesel);
        if (status != null) {
            fines = fines.stream()
                    .filter(fine -> fine.getStatus() == status)
                    .toList();
        }
        return ResponseEntity.ok(fines);
    }
}