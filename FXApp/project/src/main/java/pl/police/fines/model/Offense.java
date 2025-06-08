package pl.police.fines.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Offense {
    private Long id;
    private String code;
    private String description;
    
    @JsonProperty("penaltyPoints")
    private Integer penaltyPoints;
    
    @JsonProperty("minFineAmount")
    private BigDecimal minFineAmount;
    
    @JsonProperty("maxFineAmount")
    private BigDecimal maxFineAmount;
    
    @JsonProperty("repeatableOffense")
    private Boolean repeatableOffense;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Offense() {}
    
    public Offense(String code, String description, Integer penaltyPoints, 
                   BigDecimal minFineAmount, BigDecimal maxFineAmount, Boolean repeatableOffense) {
        this.code = code;
        this.description = description;
        this.penaltyPoints = penaltyPoints;
        this.minFineAmount = minFineAmount;
        this.maxFineAmount = maxFineAmount;
        this.repeatableOffense = repeatableOffense;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getPenaltyPoints() { return penaltyPoints; }
    public void setPenaltyPoints(Integer penaltyPoints) { this.penaltyPoints = penaltyPoints; }
    
    public BigDecimal getMinFineAmount() { return minFineAmount; }
    public void setMinFineAmount(BigDecimal minFineAmount) { this.minFineAmount = minFineAmount; }
    
    public BigDecimal getMaxFineAmount() { return maxFineAmount; }
    public void setMaxFineAmount(BigDecimal maxFineAmount) { this.maxFineAmount = maxFineAmount; }
    
    public Boolean getRepeatableOffense() { return repeatableOffense != null ? repeatableOffense : false; }
    public void setRepeatableOffense(Boolean repeatableOffense) { this.repeatableOffense = repeatableOffense; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @Override
    public String toString() {
        return code + " - " + description + " (" + penaltyPoints + " pkt, " + 
               minFineAmount + "-" + maxFineAmount + " zł)";
    }
}