package com.ticketing.ui;

import com.ticketing.ui.login.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer {
    
    private final ApplicationContext applicationContext;
    
    public StageInitializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    public void initializeStage(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent root = fxmlLoader.load();
            
            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            
            stage.setTitle("Traffic Violations Management System - Police Application");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.centerOnScreen();
            stage.show();
            
            LoginController controller = fxmlLoader.getController();
            controller.setStage(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadView(Stage stage, String fxmlPath, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent root = fxmlLoader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            
            stage.setTitle(title);
            stage.setScene(scene);
            stage.centerOnScreen();
            
            Object controller = fxmlLoader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setStage(stage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}