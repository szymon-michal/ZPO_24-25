package pl.police.fines.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.police.fines.model.*;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


public class ApiService {
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
    private static final String BASE_URL = "http://localhost:8080/api";
    private static final int TIMEOUT_SECONDS = 30;

    private static ApiService instance;
    private OkHttpClient httpClient;
    private ObjectMapper objectMapper;

    private ApiService() {
    }


    public static synchronized ApiService getInstance() {
        if (instance == null) {
            instance = new ApiService();
        }
        return instance;
    }

    public void initialize() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    // Police Officer operations
    public CompletableFuture<PoliceOfficer> getPoliceOfficer(String badgeNumber) {
        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url(BASE_URL + "/officers/" + badgeNumber)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    if (response.code() == 404) {
                        return null;
                    }
                    throw new ApiException("Failed to get police officer: " + response.code());
                }

                this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                return objectMapper.readValue(response.body().string(), PoliceOfficer.class);
            } catch (IOException e) {
                logger.error("Error getting police officer", e);
                throw new ApiException("Connection error: " + e.getMessage());
            }
        });
    }

    // Driver operations
    public CompletableFuture<Driver> getDriver(String pesel) {
        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url(BASE_URL + "/drivers/" + pesel)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    if (response.code() == 404) {
                        return null;
                    }
                    throw new ApiException("Failed to get driver: " + response.code());
                }

                return objectMapper.readValue(response.body().string(), Driver.class);
            } catch (IOException e) {
                logger.error("Error getting driver", e);
                throw new ApiException("Connection error: " + e.getMessage());
            }
        });
    }

    public CompletableFuture<Driver> createDriver(Driver driver) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String json = objectMapper.writeValueAsString(driver);
                RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

                Request request = new Request.Builder()
                        .url(BASE_URL + "/drivers")
                        .post(body)
                        .build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new ApiException("Failed to create driver: " + response.code());
                    }

                    return objectMapper.readValue(response.body().string(), Driver.class);
                }
            } catch (IOException e) {
                logger.error("Error creating driver", e);
                throw new ApiException("Connection error: " + e.getMessage());
            }
        });
    }

    // Offense operations
    public CompletableFuture<List<Offense>> getOffenses() {
        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url(BASE_URL + "/violations")
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new ApiException("Failed to get offenses: " + response.code());
                }

                return objectMapper.readValue(response.body().string(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Offense.class));
            } catch (IOException e) {
                logger.error("Error getting offenses", e);
                throw new ApiException("Connection error: " + e.getMessage());
            }
        });
    }

    // Fine operations
    public CompletableFuture<Fine> createFine(CreateFineRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String json = objectMapper.writeValueAsString(request);
                RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

                Request httpRequest = new Request.Builder()
                        .url(BASE_URL + "/tickets")
                        .post(body)
                        .build();

                try (Response response = httpClient.newCall(httpRequest).execute()) {
                    if (!response.isSuccessful()) {
                        throw new ApiException("Failed to create fine: " + response.code());
                    }

                    return objectMapper.readValue(response.body().string(), Fine.class);
                }
            } catch (IOException e) {
                logger.error("Error creating fine", e);
                throw new ApiException("Connection error: " + e.getMessage());
            }
        });
    }

    public CompletableFuture<Fine> getFine(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url(BASE_URL + "/tickets/" + id)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    if (response.code() == 404) {
                        return null;
                    }
                    throw new ApiException("Failed to get fine: " + response.code());
                }

                return objectMapper.readValue(response.body().string(), Fine.class);
            } catch (IOException e) {
                logger.error("Error getting fine", e);
                throw new ApiException("Connection error: " + e.getMessage());
            }
        });
    }

    public CompletableFuture<Fine> cancelFine(Long id, String reason) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                CancelFineRequest cancelRequest = new CancelFineRequest(reason);
                String json = objectMapper.writeValueAsString(cancelRequest);
                RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

                Request request = new Request.Builder()
                        .url(BASE_URL + "/tickets/" + id + "/cancel")
                        .post(body)
                        .build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new ApiException("Failed to cancel fine: " + response.code());
                    }

                    return objectMapper.readValue(response.body().string(), Fine.class);
                }
            } catch (IOException e) {
                logger.error("Error canceling fine", e);
                throw new ApiException("Connection error: " + e.getMessage());
            }
        });
    }

    // Health check
    public CompletableFuture<Boolean> checkConnection() {
        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url(BASE_URL + "/health")
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                return response.isSuccessful();
            } catch (IOException e) {
                return false;
            }
        });
    }

    // Exception class
    public static class ApiException extends RuntimeException {
        public ApiException(String message) {
            super(message);
        }
    }

    // Request DTOs
    public static class CreateFineRequest {

        private String policeOfficerServiceId;
        private String driverPesel;
        private String location;
        private List<OffenseRequest> offenses;


        // Constructors, getters, setters
        public CreateFineRequest() {
        }


        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getPoliceOfficerServiceId() {
            return policeOfficerServiceId;
        }

        public void setPoliceOfficerServiceId(String v) {
            this.policeOfficerServiceId = v;
        }

        public String getDriverPesel() {
            return driverPesel;
        }

        public void setDriverPesel(String driverPesel) {
            this.driverPesel = driverPesel;
        }

        public List<OffenseRequest> getOffenses() {
            return offenses;
        }

        public void setOffenses(List<OffenseRequest> offenses) {
            this.offenses = offenses;
        }


    }

    public static class OffenseRequest {
        private Long offenseId;
        private BigDecimal amount;

        public OffenseRequest() {
        }

        public OffenseRequest(Long offenseId, BigDecimal amount) {
            this.offenseId = offenseId;
            this.amount = amount;
        }

        public Long getOffenseId() {
            return offenseId;
        }

        public void setOffenseId(Long offenseId) {
            this.offenseId = offenseId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }

    public static class CancelFineRequest {
        private String reason;

        public CancelFineRequest() {
        }

        public CancelFineRequest(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}