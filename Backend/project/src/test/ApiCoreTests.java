package pl.police.fines.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import pl.police.fines.model.PoliceOfficer;
import pl.police.fines.model.Driver;
import pl.police.fines.model.Offense;
import pl.police.fines.model.Fine;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ApiServiceTest {

    private static MockWebServer mockWebServer;
    private ApiService apiService;

    @BeforeAll
    static void startMockServer() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void stopMockServer() throws Exception {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setUp() {
        apiService = ApiService.getInstance();
        apiService.initialize();
        ApiService.BASE_URL = mockWebServer.url("/api").toString(); // Override base URL for testing
    }

    @Test
    void testGetPoliceOfficer_Success() throws Exception {
        // Mock response
        String mockResponse = """
            {
                "badgeNumber": "12345",
                "name": "John",
                "surname": "Doe"
            }
        """;
        mockWebServer.enqueue(new MockResponse().setBody(mockResponse).setResponseCode(200));

        // Call the method
        CompletableFuture<PoliceOfficer> future = apiService.getPoliceOfficer("12345");
        PoliceOfficer officer = future.join();

        // Assertions
        assertNotNull(officer);
        assertEquals("12345", officer.getBadgeNumber());
        assertEquals("John", officer.getName());
        assertEquals("Doe", officer.getSurname());
    }

    @Test
    void testGetPoliceOfficer_NotFound() {
        // Mock response
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        // Call the method
        CompletableFuture<PoliceOfficer> future = apiService.getPoliceOfficer("99999");

        // Assertions
        assertThrows(ApiService.ApiException.class, future::join);
    }

    @Test
    void testGetDriver_Success() throws Exception {
        // Mock response
        String mockResponse = """
            {
                "pesel": "12345678901",
                "name": "Jane",
                "surname": "Smith",
                "offenses": [
                    {
                        "id": 1,
                        "description": "Speeding",
                        "points": 5
                    }
                ]
            }
        """;
        mockWebServer.enqueue(new MockResponse().setBody(mockResponse).setResponseCode(200));

        // Call the method
        CompletableFuture<Driver> future = apiService.getDriver("12345678901");
        Driver driver = future.join();

        // Assertions
        assertNotNull(driver);
        assertEquals("12345678901", driver.getPesel());
        assertEquals("Jane", driver.getName());
        assertEquals(1, driver.getOffenses().size());
        assertEquals("Speeding", driver.getOffenses().get(0).getDescription());
    }

    @Test
    void testGetOffenses_Success() throws Exception {
        // Mock response
        String mockResponse = """
            [
                {
                    "id": 1,
                    "description": "Speeding",
                    "points": 5
                },
                {
                    "id": 2,
                    "description": "Running a red light",
                    "points": 8
                }
            ]
        """;
        mockWebServer.enqueue(new MockResponse().setBody(mockResponse).setResponseCode(200));

        // Call the method
        CompletableFuture<List<Offense>> future = apiService.getOffenses("12345678901");
        List<Offense> offenses = future.join();

        // Assertions
        assertNotNull(offenses);
        assertEquals(2, offenses.size());
        assertEquals("Speeding", offenses.get(0).getDescription());
        assertEquals(5, offenses.get(0).getPoints());
    }

    @Test
    void testGetOffenses_EmptyList() throws Exception {
        // Mock response
        String mockResponse = "[]";
        mockWebServer.enqueue(new MockResponse().setBody(mockResponse).setResponseCode(200));

        // Call the method
        CompletableFuture<List<Offense>> future = apiService.getOffenses("12345678901");
        List<Offense> offenses = future.join();

        // Assertions
        assertNotNull(offenses);
        assertTrue(offenses.isEmpty());
    }

    @Test
    void testGetFine_Success() throws Exception {
        // Mock response
        String mockResponse = """
            {
                "id": 1,
                "status": "PENDING",
                "totalAmount": 150.00
            }
        """;
        mockWebServer.enqueue(new MockResponse().setBody(mockResponse).setResponseCode(200));

        // Call the method
        CompletableFuture<Fine> future = apiService.getFine(1L);
        Fine fine = future.join();

        // Assertions
        assertNotNull(fine);
        assertEquals(1L, fine.getId());
        assertEquals("PENDING", fine.getStatus());
        assertEquals(new BigDecimal("150.00"), fine.getTotalAmount());
    }

    @Test
    void testGetFine_NotFound() {
        // Mock response
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        // Call the method
        CompletableFuture<Fine> future = apiService.getFine(999L);

        // Assertions
        assertThrows(ApiService.ApiException.class, future::join);
    }

    @Test
    void testCancelFine_Success() throws Exception {
        // Mock response
        String mockResponse = """
            {
                "id": 1,
                "status": "CANCELLED",
                "reason": "Driver appealed"
            }
        """;
        mockWebServer.enqueue(new MockResponse().setBody(mockResponse).setResponseCode(200));

        // Call the method
        CompletableFuture<Fine> future = apiService.cancelFine(1L, "Driver appealed");
        Fine fine = future.join();

        // Assertions
        assertNotNull(fine);
        assertEquals(1L, fine.getId());
        assertEquals("CANCELLED", fine.getStatus());
    }

    @Test
    void testCancelFine_AlreadyCancelled() {
        // Mock response
        mockWebServer.enqueue(new MockResponse().setResponseCode(409));

        // Call the method
        CompletableFuture<Fine> future = apiService.cancelFine(1L, "Already cancelled");

        // Assertions
        assertThrows(ApiService.ApiException.class, future::join);
    }

    @Test
    void testCreateDriver_Success() throws Exception {
        // Mock request and response
        String requestPayload = """
            {
                "pesel": "12345678901",
                "name": "Jane",
                "surname": "Doe"
            }
        """;
        String mockResponse = """
            {
                "pesel": "12345678901",
                "name": "Jane",
                "surname": "Doe"
            }
        """;
        mockWebServer.enqueue(new MockResponse().setBody(mockResponse).setResponseCode(201));

        // Call the method
        Driver driver = new Driver();
        driver.setPesel("12345678901");
        driver.setName("Jane");
        driver.setSurname("Doe");

        CompletableFuture<Driver> future = apiService.createDriver(driver);
        Driver createdDriver = future.join();

        // Assertions
        assertNotNull(createdDriver);
        assertEquals("12345678901", createdDriver.getPesel());
        assertEquals("Jane", createdDriver.getName());
        assertEquals("Doe", createdDriver.getSurname());
    }

    @Test
    void testCreateDriver_Conflict() {
        // Mock response
        mockWebServer.enqueue(new MockResponse().setResponseCode(409));

        // Call the method
        Driver driver = new Driver();
        driver.setPesel("12345678901");
        driver.setName("Jane");
        driver.setSurname("Doe");

        CompletableFuture<Driver> future = apiService.createDriver(driver);

        // Assertions
        assertThrows(ApiService.ApiException.class, future::join);
    }
}