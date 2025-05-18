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
    public ResponseEntity<Fine> createFine(@RequestBody FineCreationDTO fineCreationDTO) {
        Fine fine = fineService.issueFine(fineCreationDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(fine.getId())
                .toUri();
        return ResponseEntity.created(location).body(fine);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fine> getFine(@PathVariable Long id) { //dzika rekurencja z oficerem
        return fineService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<Fine> updateFineStatus(
//            @PathVariable Long id,
//            @RequestParam Fine.FineStatus status) {
//        return fineService.findById(id)
//                .map(fine -> {
//                    if (status == Fine.FineStatus.ACCEPTED) {
//                        return ResponseEntity.ok(fineService.acceptFine(fine.getFineNumber()));
//                    } else if (status == Fine.FineStatus.CANCELED) {
//                        return ResponseEntity.ok(fineService.cancelFine(fine.getFineNumber(), "Administrative cancellation"));
//                    }
//                    return ResponseEntity.badRequest().build();
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping("/{id}/cancel")
//    public ResponseEntity<Fine> cancelFine(
//            @PathVariable Long id,
//            @RequestParam(required = false) String reason) {
//        return fineService.findById(id)
//                .map(fine -> {
//                    if (fine.getStatus() == Fine.FineStatus.CANCELED) {
//                        return ResponseEntity.status(HttpStatus.CONFLICT).build();
//                    }
//                    return ResponseEntity.ok(fineService.cancelFine(
//                            fine.getFineNumber(),
//                            reason != null ? reason : "Administrative cancellation"
//                    ));
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
}