package com.ticketing.controller.rest;

import com.ticketing.model.Fine;
import com.ticketing.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {
    
    private final FineService fineService;
    
    @GetMapping("/officers/{badgeNumber}/issued") //działa
    public ResponseEntity<List<Fine>> getOfficerIssuedFines(@PathVariable String badgeNumber) { //tez dzika rekurencja ale do zmiany na OfficersController
        List<Fine> fines = fineService.findByPoliceOfficerServiceId(badgeNumber);
        return ResponseEntity.ok(fines);
    }
}