package pl.police.fines;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pl.police.fines.service.ApiService;
import pl.police.fines.service.ConnectionMonitor;


public class FinesApplication extends Application {
    
    private static final String APP_TITLE = "System Zarządzania Mandatami - Policja";
    private static final int MIN_WIDTH = 1000;
    private static final int MIN_HEIGHT = 700;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize services
        ApiService.getInstance().initialize();
        ConnectionMonitor.getInstance().startMonitoring();
        
        // Load main view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(loader.load());
        
        // Apply CSS
        scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
        
        // Configure stage
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        
        // Set application icon
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/police-badge.png")));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
        
        // Handle application close
        primaryStage.setOnCloseRequest(event -> {
            ConnectionMonitor.getInstance().stopMonitoring();
            Platform.exit();
        });
        
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}