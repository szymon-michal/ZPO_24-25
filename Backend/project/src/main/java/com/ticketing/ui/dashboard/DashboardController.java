package com.ticketing.ui.dashboard;

import com.ticketing.model.Fine;
import com.ticketing.model.PoliceOfficer;
import com.ticketing.service.FineService;
import com.ticketing.service.PoliceOfficerService;
import com.ticketing.ui.BaseController;
import com.ticketing.ui.StageInitializer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DashboardController extends BaseController {
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Button newFineButton;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private TableView<Fine> finesTable;
    
    @FXML
    private TableColumn<Fine, String> fineNumberColumn;
    
    @FXML
    private TableColumn<Fine, String> driverNameColumn;
    
    @FXML
    private TableColumn<Fine, String> issueDateColumn;
    
    @FXML
    private TableColumn<Fine, String> statusColumn;
    
    private final FineService fineService;
    private final PoliceOfficerService policeOfficerService;
    private final StageInitializer stageInitializer;
    
    private PoliceOfficer currentOfficer;
    
    public DashboardController(FineService fineService, PoliceOfficerService policeOfficerService, 
                              StageInitializer stageInitializer) {
        this.fineService = fineService;
        this.policeOfficerService = policeOfficerService;
        this.stageInitializer = stageInitializer;
    }
    
    @FXML
    public void initialize() {
        setupButtons();
        setupTable();
    }
    
    public void setCurrentOfficer(PoliceOfficer officer) {
        this.currentOfficer = officer;
        welcomeLabel.setText("Welcome, Officer " + officer.getLastName() + " (" + officer.getBadgeNumber() + ")");
        loadFines();
    }
    
    private void setupButtons() {
        newFineButton.setOnAction(event -> openNewFineScreen());
        logoutButton.setOnAction(event -> logout());
    }
    
    private void setupTable() {
        fineNumberColumn.setCellValueFactory(new PropertyValueFactory<>("fineNumber"));
        driverNameColumn.setCellValueFactory(cellData -> {
            Fine fine = cellData.getValue();
            String fullName = fine.getDriver().getFirstName() + " " + fine.getDriver().getLastName();
            return javafx.beans.binding.Bindings.createStringBinding(() -> fullName);
        });
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        finesTable.setRowFactory(tv -> {
            TableRow<Fine> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Fine selectedFine = row.getItem();
                    openFineDetailsScreen(selectedFine);
                }
            });
            return row;
        });
    }
    
    private void loadFines() {
        if (currentOfficer != null) {
            List<Fine> fines = fineService.findByPoliceOfficerServiceId(currentOfficer.getServiceId());
            finesTable.setItems(FXCollections.observableArrayList(fines));
        }
    }
    
    private void openNewFineScreen() {
        // Implement navigation to new fine screen
        stageInitializer.loadView(stage, "/fxml/new-fine.fxml", "Issue New Fine");
    }
    
    private void openFineDetailsScreen(Fine fine) {
        // Implement navigation to fine details screen
        // Pass the selected fine to the controller
    }
    
    private void logout() {
        // Reset the current officer
        currentOfficer = null;
        
        // Navigate back to login screen
        stageInitializer.loadView(stage, "/fxml/login.fxml", "Traffic Violations Management System - Police Application");
    }
}