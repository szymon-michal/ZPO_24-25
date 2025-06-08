package pl.police.fines;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pl.police.fines.service.ApiService;
import pl.police.fines.service.ConnectionMonitor;




/// todo frontend
/// okienko powinno byc przeciagalne regulowane teraz ma ograniczenie za duze!
/// informacja o zerwaniu połączenia i ponownej próbie powinna byc pop'upem usunac komunikat na dole po wykonaniu jakiejs akcji
/// dodanie ikonki samego programu lewy górny róg
/// powiekszyc dane policjanta na gorze ekranu
/// pozmieniac kolory(wszystko z chata było)
/// zrobic tak zeby tabela z wykroczeniami tez sie dopasowywala do szerokosci okna
/// dodad przeglad mandatow drugie okienko guzik u góry
/// wywalic ostatnia kolumne isreapt (serwer sprawdza)
/// dodawanie nowych kierowcow gdy nie ma takich nal iscie
/// ozywic sume kwaty i sume punktów nwm czemu nie działa




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