package com.ticketing.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "fines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "fine_number", unique = true, nullable = false)
    private String fineNumber;
    
    @ManyToOne
    @JoinColumn(name = "police_officer_id", nullable = false)
    @JsonBackReference("officer-fine")
    private PoliceOfficer policeOfficer;
    
    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    @JsonBackReference("driver-fine")
    private Driver driver;
    
    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate;
    
    @Column(nullable = false)
    private String location;
    
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;
    
    @Column(name = "total_points", nullable = false)
    private Integer totalPoints;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FineStatus status;
    
    @Column(name = "acceptance_date")
    private LocalDateTime acceptanceDate;
    
    @Column(name = "cancellation_date")
    private LocalDateTime cancellationDate;
    
    @Column(name = "cancellation_reason")
    private String cancellationReason;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "fine", cascade = CascadeType.ALL)
    @JsonManagedReference("fine-offense")
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
    
    public enum FineStatus {
        ISSUED, ACCEPTED, CANCELED
    }
}