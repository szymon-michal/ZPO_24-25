package com.ticketing.controller.rest;

import com.ticketing.dto.FineCreationDTO;
import com.ticketing.model.Fine;
import com.ticketing.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    @PostMapping
    public ResponseEntity<Fine> createFine(@RequestBody FineCreationDTO fineCreationDTO) {//racej dzial
        Fine fine = fineService.issueFine(fineCreationDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(fine.getId())
                .toUri();
        return ResponseEntity.created(location).body(fine);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fine> getFine(@PathVariable Long id) { //działa
        return fineService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/{id}/cancel")
    public ResponseEntity<Fine> cancelFine( //NIE działa będzie zmieniał status na cancelled
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        return (ResponseEntity<Fine>) fineService.findById(id)
                .map(fine -> {
                    if (fine.getStatus() == Fine.FineStatus.CANCELED) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).build();
                    }
                    return ResponseEntity.ok(fineService.cancelFine(
                            fine.getFineNumber(),
                            reason != null ? reason : "Administrative cancellation"
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/{id}/accept")
    public ResponseEntity<Fine> acceptFine(@PathVariable Long id) {//NIE działa będzie zmieniał status na accepted
        return (ResponseEntity<Fine>) fineService.findById(id)
                .map(fine -> {
                    if (fine.getStatus() == Fine.FineStatus.ACCEPTED) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).build();
                    }
                    return ResponseEntity.ok(fineService.acceptFine(fine.getFineNumber()));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}/delete")//działa usuwa rekordy
    public ResponseEntity<Fine> deleteFine(@PathVariable Long id) {
        return fineService.findById(id)
                .map(fine -> {
                    fineService.deleteById(id);
                    return ResponseEntity.ok(fine);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}