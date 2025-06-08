package pl.police.fines.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Fine {
    private Long id;
    
    @JsonProperty("fineNumber")
    private String fineNumber;
    
    @JsonProperty("police_officer_id")
    private Long policeOfficerId;
    
    @JsonProperty("driver_id")
    private Long driverId;
    
    @JsonProperty("issueDate")
    private LocalDateTime issueDate;
    
    private String location;
    
    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;
    
    @JsonProperty("totalPoints")
    private Integer totalPoints;
    
    private FineStatus status;
    
    @JsonProperty("acceptanceDate")
    private LocalDateTime acceptanceDate;
    
    @JsonProperty("cancellationDate")
    private LocalDateTime cancellationDate;
    
    @JsonProperty("cancellationReason")
    private String cancellationReason;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    
    // Additional fields for complete data
    @JsonProperty("police_officer")
    private PoliceOfficer policeOfficer;
    
    @JsonProperty("driver")
    private Driver driver;
    
    @JsonProperty("fineOffenses")
    private List<FineOffense> violations;
    
    // Constructors
    public Fine() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFineNumber() { return fineNumber; }
    public void setFineNumber(String fineNumber) { this.fineNumber = fineNumber; }
    
    public Long getPoliceOfficerId() { return policeOfficerId; }
    public void setPoliceOfficerId(Long policeOfficerId) { this.policeOfficerId = policeOfficerId; }
    
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    
    public LocalDateTime getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDateTime issueDate) { this.issueDate = issueDate; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }
    
    public FineStatus getStatus() { return status; }
    public void setStatus(FineStatus status) { this.status = status; }
    
    public LocalDateTime getAcceptanceDate() { return acceptanceDate; }
    public void setAcceptanceDate(LocalDateTime acceptanceDate) { this.acceptanceDate = acceptanceDate; }
    
    public LocalDateTime getCancellationDate() { return cancellationDate; }
    public void setCancellationDate(LocalDateTime cancellationDate) { this.cancellationDate = cancellationDate; }
    
    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public PoliceOfficer getPoliceOfficer() { return policeOfficer; }
    public void setPoliceOfficer(PoliceOfficer policeOfficer) { this.policeOfficer = policeOfficer; }
    
    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }
    
    public List<FineOffense> getViolations() { return violations; }
    public void setViolations(List<FineOffense> violations) { this.violations = violations; }
    
    public enum FineStatus {
        ISSUED, ACCEPTED, CANCELED
    }
}