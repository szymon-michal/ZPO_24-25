package pl.police.fines.controller;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.police.fines.model.*;
import pl.police.fines.service.ApiService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class NewFineController implements Initializable {
    
    @FXML
    private TextField peselField;
    
    @FXML
    private Button searchDriverButton;
    
    @FXML
    private Label driverInfoLabel;
    
    @FXML
    private TextField locationField;
    
    @FXML
    private TableView<OffenseItem> offensesTable;




    @FXML
    private TableColumn<OffenseItem, Boolean> selectedColumn;

    @FXML
    private TableColumn<OffenseItem, String> codeColumn;

    @FXML
    private TableColumn<OffenseItem, String> descriptionColumn;

    @FXML
    private TableColumn<OffenseItem, Integer> pointsColumn;

    @FXML
    private TableColumn<OffenseItem, String> amountColumn;

    @FXML
    private TableColumn<OffenseItem, Boolean> repeatColumn;

    @FXML
    private Label totalPointsLabel;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private Button issueFineButton;

    @FXML
    private Label statusLabel;

    @FXML
    private ProgressIndicator loadingIndicator;

    private PoliceOfficer currentOfficer;
    private Driver currentDriver;
    private ObservableList<OffenseItem> offenseItems = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupUI();
        loadOffenses();
    }

    private void setupUI() {
        loadingIndicator.setVisible(false);
        statusLabel.setText("");

        // Setup table columns
    selectedColumn.setCellValueFactory(cellData -> {
        OffenseItem item = cellData.getValue();
        SimpleBooleanProperty property = new SimpleBooleanProperty(item.isSelected());

        property.addListener((observable, oldValue, newValue) -> {
            item.setSelected(newValue);
            updateTotals();
        });

        return property;
    });

    selectedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(index -> {
        OffenseItem item = offensesTable.getItems().get(index);
        SimpleBooleanProperty property = new SimpleBooleanProperty(item.isSelected());

        property.addListener((observable, oldValue, newValue) -> {
            item.setSelected(newValue);
            updateTotals();
        });

        return property;
    }));
        selectedColumn.setEditable(true);
        
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amountRange"));
        
        repeatColumn.setCellValueFactory(new PropertyValueFactory<>("repeat"));
        repeatColumn.setCellFactory(CheckBoxTableCell.forTableColumn(repeatColumn));
        repeatColumn.setEditable(true);
        
        offensesTable.setItems(offenseItems);
        offensesTable.setEditable(true);
        
        // Setup event handlers
        searchDriverButton.setOnAction(e -> searchDriver());
        issueFineButton.setOnAction(e -> issueFine());
        peselField.setOnAction(e -> searchDriver());
        
        // Listen to table changes for total calculation
        offenseItems.addListener((javafx.collections.ListChangeListener<OffenseItem>) change -> updateTotals());
        
        updateTotals();
    }
    
    private void loadOffenses() {
        setLoading(true);
        
        Task<List<Offense>> loadTask = new Task<List<Offense>>() {
            @Override
            protected List<Offense> call() throws Exception {
                return ApiService.getInstance().getOffenses().get();
            }
            
            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    setLoading(false);
                    List<Offense> offenses = getValue();
                    
                    offenseItems.clear();
                    for (Offense offense : offenses) {
                        offenseItems.add(new OffenseItem(offense));
                    }
                });
            }
            
            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    setLoading(false);
                    showError("Błąd wczytywania wykroczeń: " + getException().getMessage());
                });
            }
        };
        
        Thread loadThread = new Thread(loadTask);
        loadThread.setDaemon(true);
        loadThread.start();
    }
    
    private void searchDriver() {
        String pesel = peselField.getText().trim();
        
        if (pesel.isEmpty()) {
            showError("Proszę wprowadzić numer PESEL");
            return;
        }
        
        if (pesel.length() != 11 || !pesel.matches("\\d+")) {
            showError("PESEL musi składać się z 11 cyfr");
            return;
        }
        
        setLoading(true);
        
        Task<Driver> searchTask = new Task<Driver>() {
            @Override
            protected Driver call() throws Exception {
                return ApiService.getInstance().getDriver(pesel).get();
            }
            
            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    setLoading(false);
                    Driver driver = getValue();
                    
                    if (driver != null) {
                        currentDriver = driver;
                        driverInfoLabel.setText(driver.getFullName() + " (PESEL: " + driver.getPesel() + 
                                ") | Punkty karne: " + driver.getTotalPoints());
                        showSuccess("Znaleziono kierowcę");
                    } else {
                        currentDriver = null;
                        driverInfoLabel.setText("Nie znaleziono kierowcy");
                        showError("Kierowca o podanym numerze PESEL nie został znaleziony");
                    }
                });
            }
            
            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    setLoading(false);
                    currentDriver = null;
                    driverInfoLabel.setText("Błąd wyszukiwania");
                    showError("Błąd wyszukiwania kierowcy: " + getException().getMessage());
                });
            }
        };
        
        Thread searchThread = new Thread(searchTask);
        searchThread.setDaemon(true);
        searchThread.start();
    }
    
    private void issueFine() {
        if (currentDriver == null) {
            showError("Najpierw należy wyszukać kierowcę");
            return;
        }
        
        String location = locationField.getText().trim();
        if (location.isEmpty()) {
            showError("Proszę wprowadzić miejsce wykroczenia");
            return;
        }
        
        List<OffenseItem> selectedOffenses = offenseItems.stream()
                .filter(OffenseItem::isSelected)
                .toList();
        
        if (selectedOffenses.isEmpty()) {
            showError("Proszę wybrać co najmniej jedno wykroczenie");
            return;
        }
        
        setLoading(true);
        
        Task<Fine> issueTask = new Task<Fine>() {
            @Override
            protected Fine call() throws Exception {
                ApiService.CreateFineRequest request = new ApiService.CreateFineRequest();
                request.setPoliceOfficerServiceId(currentOfficer.getServiceId());
                request.setDriverPesel(currentDriver.getPesel());
                request.setLocation(location);


                List<ApiService.OffenseRequest> offenses = new ArrayList<>();


                
                for (OffenseItem item : selectedOffenses) {
                    BigDecimal min = item.getOffense().getMinFineAmount();
                    BigDecimal max = item.getOffense().getMaxFineAmount();

                    double random = ThreadLocalRandom.current().nextDouble(min.doubleValue(), max.doubleValue());
                    BigDecimal fineAmount = BigDecimal.valueOf(random).setScale(2, RoundingMode.HALF_UP);

                    offenses.add(new ApiService.OffenseRequest(item.getOffense().getId(), fineAmount));
                    

                }



                request.setOffenses(offenses);

                
                return ApiService.getInstance().createFine(request).get();
            }
            
            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    setLoading(false);
                    Fine fine = getValue();
                    
                    showSuccess("Mandat został wystawiony. Numer: " + fine.getFineNumber());
                    clearForm();
                });
            }
            
            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    setLoading(false);
                    showError("Błąd wystawiania mandatu: " + getException().getMessage());
                });
            }
        };
        
        Thread issueThread = new Thread(issueTask);
        issueThread.setDaemon(true);
        issueThread.start();
    }
    
    private void updateTotals() {
        int totalPoints = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for (OffenseItem item : offenseItems) {
            if (item.isSelected()) {
                totalPoints += item.getPoints();
                
                BigDecimal amount = item.isRepeat() ? 
                        item.getOffense().getMaxFineAmount().multiply(BigDecimal.valueOf(2)) :
                        item.getOffense().getMaxFineAmount();
                totalAmount = totalAmount.add(amount);
            }
        }
        
        totalPointsLabel.setText("Suma punktów: " + totalPoints);
        totalAmountLabel.setText("Suma kwoty: " + totalAmount + " zł");
    }
    
    private void clearForm() {
        peselField.clear();
        locationField.clear();
        currentDriver = null;
        driverInfoLabel.setText("Brak wybranego kierowcy");
        
        for (OffenseItem item : offenseItems) {
            item.setSelected(false);
            item.setRepeat(false);
        }
        
        updateTotals();
    }
    
    private void setLoading(boolean loading) {
        loadingIndicator.setVisible(loading);
        searchDriverButton.setDisable(loading);
        issueFineButton.setDisable(loading);
        peselField.setDisable(loading);
        locationField.setDisable(loading);
        offensesTable.setDisable(loading);
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
    
    public void setCurrentOfficer(PoliceOfficer currentOfficer) {
        this.currentOfficer = currentOfficer;
    }
    
    // Helper class for table items
    public static class OffenseItem {
        private final Offense offense;

        private final BooleanProperty selected = new SimpleBooleanProperty(false);
        private final BooleanProperty repeat   = new SimpleBooleanProperty(false);

        // --- selected
        public BooleanProperty selectedProperty() { return selected; }
        public boolean isSelected()              { return selected.get(); }
        public void setSelected(boolean value)   { selected.set(value); }

        // --- repeat
        public BooleanProperty repeatProperty()  { return repeat; }
        public boolean isRepeat()                { return repeat.get(); }
        public void setRepeat(boolean value)     { repeat.set(value); }

        // pozostałe gettery
        public String  getCode()        { return offense.getCode(); }
        public String  getDescription() { return offense.getDescription(); }
        public Integer getPoints()      { return offense.getPenaltyPoints(); }
        public String  getAmountRange() { return offense.getMinFineAmount() + " - "
                + offense.getMaxFineAmount() + " zł"; }

        public Offense getOffense()     { return offense; }

        public OffenseItem(Offense offense) { this.offense = offense; }
    }
}