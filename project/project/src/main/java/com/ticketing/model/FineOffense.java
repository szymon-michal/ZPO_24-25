package com.ticketing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fine_offenses", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"fine_id", "offense_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FineOffense {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "fine_id", nullable = false)
    private Fine fine;
    
    @ManyToOne
    @JoinColumn(name = "offense_id", nullable = false)
    private Offense offense;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private Integer points;
    
    @Column(name = "is_repeat")
    private Boolean isRepeat = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}