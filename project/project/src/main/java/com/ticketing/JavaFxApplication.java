package com.ticketing;

import com.ticketing.ui.StageInitializer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = TrafficViolationsApplication.getContext();
    }

    @Override
    public void start(Stage primaryStage) {
        StageInitializer stageInitializer = applicationContext.getBean(StageInitializer.class);
        stageInitializer.initializeStage(primaryStage);
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }
}