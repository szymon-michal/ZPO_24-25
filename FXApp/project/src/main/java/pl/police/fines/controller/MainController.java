package pl.police.fines.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import pl.police.fines.model.PoliceOfficer;
import pl.police.fines.service.ConnectionMonitor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    
    @FXML
    private StackPane contentPane;
    
    @FXML
    private Label connectionStatusLabel;
    
    @FXML
    private VBox statusBar;
    
    private PoliceOfficer currentOfficer;
    private Node loginView;
    private Node dashboardView;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupConnectionMonitoring();
        loadInitialView();
    }
    
    private void setupConnectionMonitoring() {
        ConnectionMonitor monitor = ConnectionMonitor.getInstance();
        
        monitor.connectedProperty().addListener((obs, wasConnected, isConnected) -> {
            Platform.runLater(() -> {
                if (isConnected) {
                    connectionStatusLabel.setText("Połączono z serwerem");
                    connectionStatusLabel.getStyleClass().removeAll("error");
                    connectionStatusLabel.getStyleClass().add("success");
                } else {
                    // connectionStatusLabel.setText("Brak połączenia z serwerem");
                    connectionStatusLabel.getStyleClass().removeAll("success");
                    connectionStatusLabel.getStyleClass().add("error");
                }
            });
        });
        
        // Initial status
        // connectionStatusLabel.setText("Sprawdzanie połączenia...");
    }
    
    private void loadInitialView() {
        showLoginView();
    }
    
    public void showLoginView() {
        if (loginView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
                loginView = loader.load();
                
                LoginController loginController = loader.getController();
                loginController.setMainController(this);
                
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        
        contentPane.getChildren().clear();
        contentPane.getChildren().add(loginView);
    }
    
    public void showDashboard(PoliceOfficer officer) {
        this.currentOfficer = officer;
        
        if (dashboardView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
                dashboardView = loader.load();
                
                DashboardController dashboardController = loader.getController();
                dashboardController.setMainController(this);
                dashboardController.setCurrentOfficer(officer);
                
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            // Update existing dashboard with new officer
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
                dashboardView = loader.load();
                
                DashboardController dashboardController = loader.getController();
                dashboardController.setMainController(this);
                dashboardController.setCurrentOfficer(officer);
                
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        
        contentPane.getChildren().clear();
        contentPane.getChildren().add(dashboardView);
    }
    
    public void logout() {
        currentOfficer = null;
        dashboardView = null; // Force reload on next login
        showLoginView();
    }
    
    public PoliceOfficer getCurrentOfficer() {
        return currentOfficer;
    }
}