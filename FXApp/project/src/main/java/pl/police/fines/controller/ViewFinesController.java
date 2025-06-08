package pl.police.fines.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import pl.police.fines.model.PoliceOfficer;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewFinesController implements Initializable {
    
    @FXML
    private Label placeholderLabel;
    
    private PoliceOfficer currentOfficer;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        placeholderLabel.setText("Funkcja przeglądania mandatów zostanie zaimplementowana w następnej wersji");
    }
    
    public void setCurrentOfficer(PoliceOfficer currentOfficer) {
        this.currentOfficer = currentOfficer;
    }
}