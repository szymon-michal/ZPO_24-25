package com.ticketing.ui.login;

import com.ticketing.model.PoliceOfficer;
import com.ticketing.service.PoliceOfficerService;
import com.ticketing.ui.BaseController;
import com.ticketing.ui.StageInitializer;
import com.ticketing.ui.dashboard.DashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginController extends BaseController {
    
    @FXML
    private TextField serviceIdField;
    
    @FXML
    private Button loginButton;
    
    private final PoliceOfficerService policeOfficerService;
    private final StageInitializer stageInitializer;
    
    public LoginController(PoliceOfficerService policeOfficerService, StageInitializer stageInitializer) {
        this.policeOfficerService = policeOfficerService;
        this.stageInitializer = stageInitializer;
    }
    
    @FXML
    public void initialize() {
        loginButton.setOnAction(this::handleLogin);
    }
    
    @FXML
    private void handleLogin(ActionEvent event) {
        String serviceId = serviceIdField.getText().trim();
        
        if (serviceId.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Login Failed", "Please enter your service ID.");
            return;
        }
        
        Optional<PoliceOfficer> officerOpt = policeOfficerService.findByServiceId(serviceId);
        
        if (officerOpt.isPresent()) {
            PoliceOfficer officer = officerOpt.get();
            // Store the logged in officer in the user session or similar
            
            // Navigate to the dashboard
            stageInitializer.loadView(stage, "/fxml/dashboard.fxml", 
                    "Traffic Violations Management System - " + officer.getFirstName() + " " + officer.getLastName());
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Login Failed", 
                    "Invalid service ID. Please try again or contact your administrator.");
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}