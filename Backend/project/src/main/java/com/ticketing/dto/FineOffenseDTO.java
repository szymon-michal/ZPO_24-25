package com.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FineOffenseDTO {
    
    private Long offenseId;
    private BigDecimal amount;
}