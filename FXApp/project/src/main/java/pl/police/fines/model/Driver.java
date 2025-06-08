package pl.police.fines.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Driver {
    private Long id;
    private String pesel;
    
    @JsonProperty("licenseNumber")
    private String licenseNumber;
    
    @JsonProperty("firstName")
    private String firstName;
    
    @JsonProperty("lastName")
    private String lastName;
    
    @JsonProperty("dateOfBirth")
    private LocalDate dateOfBirth;
    
    private String address;
    private String email;
    private String phone;
    
    @JsonProperty("licenseIssueDate")
    private LocalDate licenseIssueDate;
    
    @JsonProperty("licenseExpiryDate")
    private LocalDate licenseExpiryDate;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    


    private Integer totalPoints;
    
    // Constructors
    public Driver() {}
    
    public Driver(String pesel, String licenseNumber, String firstName, String lastName) {
        this.pesel = pesel;
        this.licenseNumber = licenseNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPesel() { return pesel; }
    public void setPesel(String pesel) { this.pesel = pesel; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public LocalDate getLicenseIssueDate() { return licenseIssueDate; }
    public void setLicenseIssueDate(LocalDate licenseIssueDate) { this.licenseIssueDate = licenseIssueDate; }
    
    public LocalDate getLicenseExpiryDate() { return licenseExpiryDate; }
    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) { this.licenseExpiryDate = licenseExpiryDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Integer getTotalPoints() {

        OkHttpClient httpClient = new OkHttpClient(); // lub użyj swojego

        Request request = new Request.Builder()
                .url("http://localhost:8080/api/drivers/" + pesel + "/points")
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String body = response.body().string().trim();
            totalPoints = Integer.parseInt(body);
            return totalPoints;
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return -1; // lub -1 jeśli chcesz sygnalizować błąd
        }
//        return totalPoints != null ? totalPoints : 0;

    }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return getFullName() + " (PESEL: " + pesel + ")";
    }
}