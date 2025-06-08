package pl.police.fines.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class PoliceOfficer {
    private Long id;



    @JsonProperty("serviceId")
    private String serviceId;
    
    @JsonProperty("firstName")
    private String firstName;
    
    @JsonProperty("lastName")
    private String lastName;
    
    @JsonProperty("badgeNumber")
    private String badgeNumber;
    
    private String department;
    private String email;
    private String phone;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    
    // Constructors
    public PoliceOfficer() {}
    
    public PoliceOfficer(String serviceId, String firstName, String lastName, String badgeNumber, String department) {
        this.serviceId = serviceId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.badgeNumber = badgeNumber;
        this.department = department;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getBadgeNumber() { return badgeNumber; }
    public void setBadgeNumber(String badgeNumber) { this.badgeNumber = badgeNumber; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return getFullName() + " (Nr służbowy: " + serviceId + ")";
    }
}