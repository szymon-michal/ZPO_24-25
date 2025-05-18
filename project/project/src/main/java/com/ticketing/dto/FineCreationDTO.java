package com.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FineCreationDTO {
    
    private String policeOfficerServiceId;
    private String driverPesel;
    private String location;
    private List<FineOffenseDTO> offenses;
}