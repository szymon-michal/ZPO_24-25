package pl.police.fines.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import pl.police.fines.model.PoliceOfficer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Label officerInfoLabel;
    
    @FXML
    private Button newFineButton;
    
    @FXML
    private Button viewFinesButton;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private StackPane dashboardContent;
    
    private MainController mainController;
    private PoliceOfficer currentOfficer;
    private Node newFineView;
    private Node viewFinesView;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupUI();
    }
    
    private void setupUI() {
        newFineButton.setOnAction(e -> showNewFineView());
        viewFinesButton.setOnAction(e -> showViewFinesView());
        logoutButton.setOnAction(e -> handleLogout());
    }
    
    public void setCurrentOfficer(PoliceOfficer officer) {
        this.currentOfficer = officer;
        updateOfficerInfo();
        
        // Default to new fine view
        showNewFineView();
    }
    
    private void updateOfficerInfo() {
        if (currentOfficer != null) {
            welcomeLabel.setText("Witaj, " + currentOfficer.getFullName());
            officerInfoLabel.setText("Nr służbowy: " + currentOfficer.getServiceId() + 
                    " | Wydział: " + (currentOfficer.getDepartment() != null ? currentOfficer.getDepartment() : "Nie określono"));
        }
    }
    
    private void showNewFineView() {
        if (newFineView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/new-fine.fxml"));
                newFineView = loader.load();
                
                NewFineController controller = loader.getController();
                controller.setCurrentOfficer(currentOfficer);
                
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        
        dashboardContent.getChildren().clear();
        dashboardContent.getChildren().add(newFineView);
        
        // Update button states
        newFineButton.getStyleClass().add("active");
        viewFinesButton.getStyleClass().remove("active");
    }
    
    private void showViewFinesView() {
        if (viewFinesView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view-fines.fxml"));
                viewFinesView = loader.load();
                
                ViewFinesController controller = loader.getController();
                controller.setCurrentOfficer(currentOfficer);
                
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        
        dashboardContent.getChildren().clear();
        dashboardContent.getChildren().add(viewFinesView);
        
        // Update button states
        viewFinesButton.getStyleClass().add("active");
        newFineButton.getStyleClass().remove("active");
    }
    
    private void handleLogout() {
        if (mainController != null) {
            mainController.logout();
        }
    }
    
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}