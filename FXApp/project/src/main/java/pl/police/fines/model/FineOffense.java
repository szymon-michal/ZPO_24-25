package pl.police.fines.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FineOffense {
    private Long id;
    
    @JsonProperty("fine_id")
    private Long fineId;
    
    @JsonProperty("offense_id")
    private Long offenseId;
    
    private BigDecimal amount;
    private Integer points;
    
    @JsonProperty("is_repeat")
    private Boolean isRepeat;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    // Additional field for complete offense data
    private Offense offense;
    
    // Constructors
    public FineOffense() {}
    
    public FineOffense(Long offenseId, BigDecimal amount, Integer points, Boolean isRepeat) {
        this.offenseId = offenseId;
        this.amount = amount;
        this.points = points;
        this.isRepeat = isRepeat;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getFineId() { return fineId; }
    public void setFineId(Long fineId) { this.fineId = fineId; }
    
    public Long getOffenseId() { return offenseId; }
    public void setOffenseId(Long offenseId) { this.offenseId = offenseId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    
    public Boolean getIsRepeat() { return isRepeat != null ? isRepeat : false; }
    public void setIsRepeat(Boolean isRepeat) { this.isRepeat = isRepeat; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Offense getOffense() { return offense; }
    public void setOffense(Offense offense) { this.offense = offense; }
}