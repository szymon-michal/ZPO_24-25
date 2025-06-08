package pl.police.fines.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pl.police.fines.model.PoliceOfficer;
import pl.police.fines.service.ApiService;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    
    @FXML
    private TextField badgeNumberField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private ProgressIndicator loadingIndicator;
    
    private MainController mainController;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupUI();
    }
    
    private void setupUI() {
        badgeNumberField.setOnAction(e -> handleLogin());
        loadingIndicator.setVisible(false);
        statusLabel.setText("");
        
        // Input validation
        badgeNumberField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.isEmpty()) {
                statusLabel.setText("");
                statusLabel.getStyleClass().removeAll("error", "success");
            }
        });
    }
    
    @FXML
    private void handleLogin() {
        String badgeNumber = badgeNumberField.getText().trim();
        
        if (badgeNumber.isEmpty()) {
            showError("Proszę wprowadzić numer służbowy");
            return;
        }
        
        setLoading(true);
        
        Task<PoliceOfficer> loginTask = new Task<PoliceOfficer>() {
            @Override
            protected PoliceOfficer call() throws Exception {
                return ApiService.getInstance().getPoliceOfficer(badgeNumber).get();
            }
            
            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    setLoading(false);
                    PoliceOfficer officer = getValue();
                    
                    if (officer != null) {
                        showSuccess("Zalogowano pomyślnie");
                        if (mainController != null) {
                            mainController.showDashboard(officer);
                        }
                    } else {
                        showError("Nie znaleziono policjanta o podanym numerze służbowym");
                    }
                });
            }
            
            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    setLoading(false);
                    Throwable exception = getException();
                    
                    if (exception instanceof ApiService.ApiException) {
                        showError("Błąd: " + exception.getMessage());
                    } else {
                        showError("Błąd połączenia z serwerem");
                    }
                });
            }
        };
        
        Thread loginThread = new Thread(loginTask);
        loginThread.setDaemon(true);
        loginThread.start();
    }
    
    private void setLoading(boolean loading) {
        loadingIndicator.setVisible(loading);
        loginButton.setDisable(loading);
        badgeNumberField.setDisable(loading);
    }
    
    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("success");
        statusLabel.getStyleClass().add("error");
    }
    
    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("error");
        statusLabel.getStyleClass().add("success");
    }
    
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}