package com.ticketing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "offenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Offense {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String code;
    
    @Column(nullable = false)
    private String description;
    
    @Column(name = "penalty_points", nullable = false)
    private Integer penaltyPoints;
    
    @Column(name = "min_fine_amount", nullable = false)
    private BigDecimal minFineAmount;
    
    @Column(name = "max_fine_amount", nullable = false)
    private BigDecimal maxFineAmount;
    
    @Column(name = "repeatable_offense")
    private Boolean repeatableOffense = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "offense")
    private List<FineOffense> fineOffenses;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}